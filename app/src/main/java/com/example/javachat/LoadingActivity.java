package com.example.javachat;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoadingActivity extends AppCompatActivity {

    private String user;
    private String ip = "10.0.2.2";
    private Thread timeout;
    private Thread sockThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = new Intent(LoadingActivity.this, ChatActivity.class);


        if (getIntent().hasExtra("user")) {
            user = getIntent().getStringExtra("user");
            intent.putExtra("user", user);
        } else {
            finish();
        }
        if (getIntent().hasExtra("ip")) {
            ip = getIntent().getStringExtra("ip");
        }

        final Intent inte = intent;


        try {
            final InetAddress ipa = Inet4Address.getByName(ip);
            //App.setSocket(new Socket(ipa, ChatActivity.PORT));
            sockThread = new Thread(() -> {
                try {
                    App.setSocket(new Socket(ipa, ChatActivity.PORT));
                    startActivity(inte);

                } catch (IOException e) {
                    e.printStackTrace();
                    throwError("connection failed");
                } finally {
                    finish();
                    timeout.interrupt();
                }
            });
            timeout = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    throwError("Timeout");
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            timeout.start();

            sockThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            throwError("ip wrong");
            finish();
        }
    }


    private void throwError(final String msg) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        sockThread.interrupt();
        super.onBackPressed();
    }
}
