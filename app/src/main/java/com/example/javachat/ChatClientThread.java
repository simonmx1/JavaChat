package com.example.javachat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class ChatClientThread extends Thread
{
	private BufferedReader in = null;

	//Ausgabefeld:
	private out area;
	//Chatverlauf:
    private verlauf scrollPane;

	public ChatClientThread(BufferedReader in) {
		this.in = in;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String line = in.readLine();
				System.out.println(line);
				if (line.contains("smile")) {
					area.setText(area.getText() + ":) \n");
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				} else {
					area.setText(area.getText() + line + "\n");
					scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				}
				// System.out.println(line);
			}
		} catch (SocketException e) {
			System.out.println("Connection to ChatServer lost, ignore exception");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}
}

