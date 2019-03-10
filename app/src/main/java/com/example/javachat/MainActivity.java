package com.example.javachat;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    //public static final int PORT = 65535;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView u = findViewById(R.id.m_user);
        final Button b_c = findViewById(R.id.m_join);
        b_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("user", u.getText().toString());
                startActivity(intent);
            }
        });

        /*
        Socket client = null;
        try {
            client = new Socket("localhost", PORT);
            BufferedReader in =
                    new BufferedReader( new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            BufferedReader consoleIn =
                    new BufferedReader(new InputStreamReader(System.in));

            // sending the name of the client to the server
            out.println("Client");

            new ChatClientThread(in).start();

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
            try { client.close(); } catch (Exception e1) { ; }
        }
        */



    }
}

