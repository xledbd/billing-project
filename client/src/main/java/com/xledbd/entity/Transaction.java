package com.xledbd.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {

	private int id;
	private Contract contractFrom;
	private Contract contractTo;
	private double amount;
	private LocalDateTime date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Contract getContractFrom() {
		return contractFrom;
	}

	public void setContractFrom(Contract contractFrom) {
		this.contractFrom = contractFrom;
	}

	public Contract getContractTo() {
		return contractTo;
	}

	public void setContractTo(Contract contractTo) {
		this.contractTo = contractTo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
