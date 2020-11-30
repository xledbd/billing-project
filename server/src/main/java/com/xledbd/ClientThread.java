package com.xledbd;

import com.xledbd.dao.DAOFactory;
import com.xledbd.entity.User;
import org.hibernate.JDBCException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

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
				switch (msg) {
					case "signin" -> signin();
				}
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

	private void signin() throws Exception {
		User user = (User)inputStream.readObject();
		App.print_log("Trying to sign in as " + user.getLogin() + "...");
		User compare = null;
		boolean res = false;
		try {
			List<User> list = DAOFactory.getUserDAO().getList();
			for (User u: list) {
				if (compare != null) break;
				if (u.getLogin().equals(user.getLogin()))
					compare = u;
			}
			if (compare != null) {
				user.setBanStatus(compare.getBanStatus());
				user.setAccessLevel(compare.getAccessLevel());
				res = user.getPassword().equals(compare.getPassword());
			}
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (res) {
			App.print_log("Sign in successful...");
			outputStream.writeObject("true");
			outputStream.writeObject(compare);
		}
		else {
			App.print_log("Sign in failed...");
			outputStream.writeObject("false");
		}
	}
}
