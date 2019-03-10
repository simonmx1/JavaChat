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
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView view;
    private ChatAdapter adapter;
    private ArrayList<Text> chat = new ArrayList<>();
    private ImageView b_send;
    private TextView field;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if(getIntent().hasExtra("user")){
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
                if(!field.getText().toString().matches("")) {
                    chat.add(new Text(field.getText().toString(), user, true));
                    field.setText("");
                    adapter.notifyItemInserted(chat.size());
                    view.scrollToPosition(chat.size() - 1);

                }else{
                    Log.i("", "false");
                }
            }
        });


        Socket client = null;
        try {
            client = new Socket("localhost", 65535);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            BufferedReader consoleIn =
                    new BufferedReader(new InputStreamReader(System.in));

            // sending the name of the client to the server
            out.println("Client");

            //new ChatClientThread(in).start();

            while (true) {
                String line = consoleIn.readLine();
                if (line == null)
                    // pressed [Ctrl]+Z to sign out
                    break;
                out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (Exception e1) {
            }
        }

    }

    private void setRecyclerView(){
        view = findViewById(R.id.c_view);

        view.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);

        adapter = new ChatAdapter(this, chat);
        view.setAdapter(adapter);
    }

    private void addText(){

        chat.add(new Text("this is a test", "simons", false));
        chat.add(new Text("this is not a test", "simons", false));
        chat.add(new Text("this is a test lol", "you", true));
        chat.add(new Text("this is a test", "felix", false));
    }



}
