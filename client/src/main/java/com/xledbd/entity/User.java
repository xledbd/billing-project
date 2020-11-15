package com.xledbd.entity;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class User implements Serializable {

	private int id;
	private String login;
	private String password;
	private String email;
	private LocalDateTime registrationDate;
	private int accessLevel;

	public User() {	}

	public User(String login, String password) {
		setLogin(login);
		setPassword(password);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String generatedHash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			byte[] bytes = digest.digest();
			// Convert from dec to hex format
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedHash = builder.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.password = generatedHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
}
