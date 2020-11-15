package com.xledbd.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class Contract implements Serializable {

	private int id;
	private User user;
	private Service service;
	private double balance;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private LocalDate lastInvoice;
	private boolean active;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public LocalDate getLastInvoice() {
		return lastInvoice;
	}

	public void setLastInvoice(LocalDate lastInvoice) {
		this.lastInvoice = lastInvoice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
