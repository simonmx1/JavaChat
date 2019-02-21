package com.example.javachat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Simon", "onCreate: ");
        }
    }
}
