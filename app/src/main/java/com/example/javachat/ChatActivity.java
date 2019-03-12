package com.example.javachat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatClientThread.Refresh, ChatClientThread.Users{

    private RecyclerView view;
    private ChatAdapter adapter;
    private ArrayList<Text> chat = new ArrayList<>();
    private ImageView b_send;
    private TextView field;
    private String user;

    private BufferedReader in;
    private PrintStream out;
    private Thread t;
    private Socket client;
    private TextView users;

    public String ip = "10.0.2.2";
    //public static final String IP = "192.168.1.184";
    public static final int PORT = 65535;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        users = findViewById(R.id.useres);
        users.setText(1 + " Users connected");
        users.setGravity(Gravity.RIGHT);



        if (getIntent().hasExtra("user")) {
            user = getIntent().getStringExtra("user");
        }else{finish();}
        if (getIntent().hasExtra("ip")) {
            ip = getIntent().getStringExtra("ip");
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //addText();
        setRecyclerView();


        field = findViewById(R.id.c_text);

        b_send = findViewById(R.id.c_send);
        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cont = field.getText().toString().trim();
                if (!cont.matches("")) {
                    chat.add(new Text(cont , user, true));
                    field.setText("");
                    adapter.notifyItemInserted(chat.size());
                    view.scrollToPosition(chat.size() - 1);
                    new Thread(){
                        @Override
                        public void run() {
                            out.println(cont);
                        }
                    }.start();

                } else {
                    Log.i("", "false");
                }
            }
        });
        t = new Thread() {
            @Override
            public void run() {
                try {
                    InetAddress ipa = Inet4Address.getByName(ip);
                    try {
                        client = new Socket(ipa, PORT);
                    } catch (Exception e) {
                        try {
                            Toast.makeText(getApplicationContext(), "Trying Localhost!", Toast.LENGTH_LONG);
                            client = new Socket("localhost", PORT);
                        } catch (Exception ex) {
                            Log.i("", "run: connection failed");
                            Toast.makeText(getApplicationContext(), "Connection failed!", Toast.LENGTH_LONG);
                        }
                    }
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new PrintStream(client.getOutputStream());
                    //consoleIn = new BufferedReader(new InputStreamReader(System.in));
                    // sending the name of the client to the server
                    Log.i("", "user send: " + user);
                    out.println(user);

                    if (in.readLine().equals("NAME_USED"))
                        try {
                            client.close();
                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Username unavailable", Toast.LENGTH_LONG).show();
                                }
                            });
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Username unavailable", Toast.LENGTH_LONG).show();
                                }
                            });
                            finish();
                        }

                    new ChatClientThread(user, in , chat, ChatActivity.this, ChatActivity.this).start();
                } catch (IOException e) {
                    Log.i("", e.getClass().getName() + ": " + e.getMessage());
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

    @Deprecated
    private void addText() {
        chat.add(new Text("this is a test", "simons", false));
        chat.add(new Text("this is not a test", "simons", false));
        chat.add(new Text("this is a test lol", "you", true));
        chat.add(new Text("this is a test", "felix", false));
    }


    @Override
    public void onSend() {
        adapter.notifyItemInserted(chat.size());
        //view.scrollToPosition(chat.size() - 1);
    }

    @Override
    public void onChange(int size) {
        users.setText(size + " Users connected");
    }
}
