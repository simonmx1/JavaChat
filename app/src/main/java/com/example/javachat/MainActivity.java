package com.example.javachat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //public static final int PORT = 65535;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView u = findViewById(R.id.m_user);
        final TextView i = findViewById(R.id.m_ip);
        final Button b_c = findViewById(R.id.m_join);
        b_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                if(!i.getText().toString().matches(""))
                    intent.putExtra("ip", i.getText().toString());
                if(!u.getText().toString().matches("")) {
                    intent.putExtra("user", u.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

