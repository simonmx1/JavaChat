package com.example.javachat;

import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatClientThread extends Thread
{

	private BufferedReader in = null;
	private String user;
	//Ausgabefeld:
	private ArrayList chat;
	//private PrintStream out;
	private Refresh r;
	private Users u;

	ChatClientThread(String user, BufferedReader in, ArrayList<Text> chat, Refresh r, Users u) {
		this.in = in;
		//this.out = out;
		this.chat = chat;
		this.r = r;
		this.u = u;
		this.user = user;
	}

	@Override
	public void run() {
		try {
			String user = null;
			String msg;
			while (true) {
				String line = in.readLine();
				System.out.println(line);

				if (!Character.isAlphabetic(line.charAt(0))) {

					user = "Server Message";
					msg = line.substring(line.indexOf(':') + 1);
					u.onChange(Integer.parseInt(line.substring(0, line.indexOf(':'))));

				} else {
					//if message
					if (line.indexOf(':') != -1)
						user = line.substring(0, line.indexOf(':'));
					msg = line.substring(line.indexOf(':') + 1);
				}


				if (!this.user.equals(user)) {
					Text text = new Text(msg, user, false);
					chat.add(text);
					r.onSend(text);
				}


			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatServer lost, ignore exception");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public interface Refresh{
		void onSend(Text text);
	}

	public interface Users{
		void onChange(int size);
	}

}

