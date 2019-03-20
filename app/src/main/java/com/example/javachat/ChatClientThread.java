package com.example.javachat;

import android.app.Activity;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatClientThread extends Thread {

    private BufferedReader in;
    private String user;
    private ArrayList chat;
    private Refresh r;
    private Users u;
    private Activity a;

    ChatClientThread(String user, BufferedReader in, ArrayList<Text> chat, Refresh r, Users u) {
        this.user = user;
        this.in = in;
        this.chat = chat;
        this.r = r;
        this.u = u;
        this.a = a;
    }

    @Override
    public void run() {
        try {
            String msg, user = null;

            while (true) {
                String line = in.readLine();
                System.out.println(line);
                if (line == null)
                    break;
                if (!Character.isAlphabetic(line.charAt(0))) {

                    user = "Server Message";
                    msg = line.substring(line.indexOf(':') + 1);
                    u.onChange(Integer.parseInt(line.substring(0, line.indexOf(':'))));
                    if (msg.contains("signed in"))
                        ChatActivity.online_users.add(msg.substring(0, msg.indexOf(' ')));
                    if (msg.contains("signed out"))
                        ChatActivity.online_users.remove(msg.substring(0, msg.indexOf(' ')));

                } else {

                    if (line.indexOf(':') != -1)
                        user = line.substring(0, line.indexOf(':'));
                    msg = line.substring(line.indexOf(':') + 1);
                }

                if (!this.user.equals(user)) {
                    if (!(user.equals("Server Message") && msg.startsWith(this.user + ' '))) {
                        Text text = new Text(msg, user, false);
                        chat.add(text);
                        r.onSend(text);
                    }
                }
            }
            System.out.print("IN = NULLLLLLLLLLLLLLL");
        } catch (SocketException e) {
            System.out.print("gali");
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public interface Refresh {
        void onSend(Text text);
    }

    public interface Users {
        void onChange(int size);
    }

}

