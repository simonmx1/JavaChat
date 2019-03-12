package com.example.javachat;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatClientThread extends Thread
{

	private BufferedReader in = null;

	//Ausgabefeld:
	private ArrayList out;


	ChatClientThread(BufferedReader in, ArrayList<Text> chat) {
		this.in = in;
		this.out = chat;
	}

	@Override
	public void run() {
		try {
			String user = null;
			String msg;
			while (true) {
				String line = in.readLine();
				System.out.println(line);
				if (line.contains("smile")) {
					user = line;
				} else {
					msg = line;
					if (user != null)
						out.add(new Text(msg, user, false));
				}
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatServer lost, ignore exception");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

}

