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
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView view;
    private ChatAdapter adapter;
    private ArrayList<Text> chat = new ArrayList<>();
    private ImageView b_send;
    private TextView field;
    private String user;

    private BufferedReader in, consoleIn;
    private PrintStream out;

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
                if (!field.getText().toString().matches("")) {
                    chat.add(new Text(field.getText().toString(), user, true));
                    field.setText("");
                    adapter.notifyItemInserted(chat.size());
                    view.scrollToPosition(chat.size() - 1);

                } else {
                    Log.i("", "false");
                }
            }
        });


        new Thread() {
            public void run() {
                Socket client = null;
                try {
                    Inet4Address ad = (Inet4Address) Inet4Address.getByName("localhost");
                    client = new Socket(ad, 35565);
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new PrintStream(client.getOutputStream());
                    consoleIn = new BufferedReader(new InputStreamReader(System.in));
                    // sending the name of the client to the server
                    out.println(user);
                    new ChatClientThread(in, chat).start();
                } catch (
                        IOException e) {
                    System.out.println(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }.start();
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


}
