package com.example.javachat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatClientThread.Refresh{

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

    public static final int PORT = 65535;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent().hasExtra("user")) {
            user = getIntent().getStringExtra("user");
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        addText();
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
                            out.println(cont+user);
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
                    InetAddress ip = Inet4Address.getByName("192.168.1.184");
                    try {
                        client = new Socket(ip, PORT);
                    }catch (Exception e){
                        try{client = new Socket("localhost", PORT);
                        }catch (Exception ex){}
                    }

                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new PrintStream(client.getOutputStream());
                    //consoleIn = new BufferedReader(new InputStreamReader(System.in));
                    // sending the name of the client to the server
                    Log.i("", "user send: " + user);
                    out.println(user);

                    new ChatClientThread(in, out, chat, ChatActivity.this).start();
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
}
