package com.example.javachat;

import android.text.format.Time;
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

    ChatClientThread(String user, BufferedReader in, ArrayList<Text> chat, Refresh r, Users u) {
        this.user = user;
        this.in = in;
        this.chat = chat;
        this.r = r;
        this.u = u;
    }

    @Override
    public void run() {
        try {
            String user = null;
            String msg;
            Timeout t = new Timeout(7000);
            while (!t.stopped) {
                String line = in.readLine();
                System.out.println(line);

                if (!line.equals(":A:")) {
                    if (!Character.isAlphabetic(line.charAt(0))) {

                        user = "Server Message";
                        msg = line.substring(line.indexOf(':') + 1);
                        u.onChange(Integer.parseInt(line.substring(0, line.indexOf(':'))));

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
                    };
                } else {
                    t.resetTimeout();
                }
            }
        } catch (SocketException e) {
            System.out.println("Connection to ChatServer lost, ignore exception");
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

