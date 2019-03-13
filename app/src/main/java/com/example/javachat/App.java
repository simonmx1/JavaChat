package com.example.javachat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String NOTIF_CHANNEL = "channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotifChannels();
    }

    private void createNotifChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL, "Notificatien",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


}
