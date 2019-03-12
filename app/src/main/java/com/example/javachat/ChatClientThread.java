package com.example.javachat;

import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatClientThread extends Thread
{

	private BufferedReader in = null;

	//Ausgabefeld:
	private ArrayList chat;
	private PrintStream out;

	ChatClientThread(BufferedReader in, PrintStream out, ArrayList<Text> chat) {
		this.in = in;
		this.out = out;
		this.chat = chat;
	}

	@Override
	public void run() {
		try {
			String user = null;
			String msg;
			while (true) {
				String line = in.readLine();
				System.out.println(line);
				if (line.indexOf(':') != -1)
					user = line.substring(0, line.indexOf(':'));
				msg = line.substring(line.indexOf(':')+1);
				chat.add(new Text(msg, user, false));
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatServer lost, ignore exception");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

}

