package com.xledbd;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread implements Runnable {

	private final Socket socket;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		String msg;
		try {
			App.print_log("New user connected...");
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			msg = (String)inputStream.readObject();
			while (!"exit".equals(msg)) {

				msg = (String)inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			App.print_log("User disconnected...");
		}
	}
}
