package com.xledbd.entity;

import java.io.Serializable;

public class Service implements Serializable {

	private int id;
	private String name;
	private double price;
	private String description;

	public Service() { }

	public Service(String name, double price) {
		setName(name);
		setPrice(price);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return name;
	}
}
