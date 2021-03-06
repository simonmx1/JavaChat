package com.example.javachat;

import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.javachat.App.NOTIF_CHANNEL;

public class ChatActivity extends AppCompatActivity implements ChatClientThread.Refresh, PopupMenu.OnMenuItemClickListener,
        ChatClientThread.Users, ChatClientThread.Finish{
    public static final String TAG = "Chat";


    private RecyclerView view;
    private ChatAdapter adapter;
    private ArrayList<Text> chat = new ArrayList<>();
    private ImageView b_send;
    private TextView field;
    private String user;

    private BufferedReader in;
    private PrintStream out;
    private Thread t;
    private Socket client = null;
    private TextView users;

    private NotificationManagerCompat notifManager;

    public static final String IP = "192.168.1.121";
    static ArrayList<String> online_users;
    public static final int PORT = 65535;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        notifManager = NotificationManagerCompat.from(this);

        users = findViewById(R.id.t_users);
        users.setText(1 + " Users connected");
        users.setGravity(Gravity.RIGHT);

        if (getIntent().hasExtra("user")) {
            user = getIntent().getStringExtra("user");
        } else {
            finish();
        }

        //set user in toolbar
        TextView t_user = findViewById(R.id.t_user);
        t_user.setText(user);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRecyclerView();


        field = findViewById(R.id.c_text);

        b_send = findViewById(R.id.c_send);
        b_send.setOnClickListener(v -> {
            final String cont = field.getText().toString().trim();
            if (!cont.matches("")) {
                chat.add(new Text(cont, "you", true));
                field.setText("");
                adapter.notifyItemInserted(chat.size());
                view.scrollToPosition(chat.size() - 1);
                new Thread() {
                    @Override
                    public void run() {
                        out.println(cont);
                    }
                }.start();

            } else {
                Log.i("", "false");
            }
        });
        t = new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "run: start Thread");
                try {

                    client = App.getSocket();

                    if (client == null) {
                        Log.i(TAG, "run: client null");
                        finish();
                    }
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    out = new PrintStream(client.getOutputStream());


                    //sending the name of the client to the server
                    Log.i("", "user send: " + user);
                    out.println(user);

                    String firstMSG = in.readLine();

                    //check for Username
                    if (firstMSG.equals("NAME_USED")) {
                        try {
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Username unavailable",
                                    Toast.LENGTH_LONG).show());
                            finish();
                        }
                    }

                    //get number of Users
                    if (firstMSG.startsWith("SERVER_INF:")) {
                        onChange(Integer.parseInt(firstMSG.substring(firstMSG.indexOf(':') + 1, firstMSG.lastIndexOf(':'))));

                        if (Integer.parseInt(firstMSG.substring(firstMSG.indexOf(':') + 1, firstMSG.lastIndexOf(':'))) != 0) {

                            firstMSG = firstMSG.substring(firstMSG.indexOf('[') + 1, firstMSG.indexOf(']'));

                            online_users = new ArrayList<>(Arrays.asList(firstMSG.split(",")));
                            Log.i("", firstMSG);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                online_users.replaceAll(String::trim);
                                online_users.removeIf(String::isEmpty);
                            } else {
                                ArrayList<String> trimmed = new ArrayList<>();
                                for (String s : online_users) {
                                    s = s.trim();
                                    if (!s.isEmpty())
                                        trimmed.add(s);
                                }
                                online_users = trimmed;
                            }
                        }
                    }
                    new ChatClientThread(user, in, chat, ChatActivity.this, ChatActivity.this, ChatActivity.this)
                            .start();

                } catch (IOException e) {
                    Log.i("ERROR", e.getClass().getName() + ": " + e.getMessage());
                }
            }
        };
        t.start();
    }

    @Override
    public void onBackPressed() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    private void setRecyclerView() {
        view = findViewById(R.id.c_view);

        view.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);

        adapter = new ChatAdapter(this, chat);
        view.setAdapter(adapter);
    }


    @Override
    public void onSend(final Text text) {
        runOnUiThread(() -> {
            adapter.notifyItemInserted(chat.size());
            view.scrollToPosition(chat.size() - 1);
            if (!App.isActivityVisible())
                sendNotif(text);
        });

    }

    @Override
    public void onChange(final int size) {
        runOnUiThread(() -> users.setText(size + " Users connected"));
    }

    public void sendNotif(Text text) {
        Notification notif = new NotificationCompat.Builder(this, NOTIF_CHANNEL)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(text.getUser())
                .setContentText(text.getContent())
                .build();
        notifManager.notify(1, notif);
    }


    @Override
    protected void onResume() {
        super.onResume();
        App.activityResumed();
        notifManager.cancelAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.activityPaused();
    }


    public void showPopup(View v) {
        Log.i(TAG, "showPopup: ");
        PopupMenu pop = new PopupMenu(this, v);
        pop.setOnMenuItemClickListener(this);
        pop.getMenu().clear();
        for (String s : online_users) {
            pop.getMenu().add(s);
        }
        pop.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public void onConnectionLost() {
        Log.i("", "Connection lost");
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Connection lost",
                Toast.LENGTH_LONG).show());
        finish();
    }

    @Override
    public void onFinish() {
        runOnUiThread(() -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        finish();
    }



}
