package com.example.javachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = new Intent(LoadingActivity.this, ChatActivity.class);


        if (getIntent().hasExtra("user")) {
            user = getIntent().getStringExtra("user");
        } else {
            finish();
        }
        if (getIntent().hasExtra("ip")) {
            intent.putExtra("ip", getIntent().getStringExtra("ip"));
        }

        startActivity(intent);


    }
}
