package com.xledbd.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Invoice implements Serializable {

	private int id;
	private Contract contract;
	private Transaction transaction;
	private double amount;
	private LocalDate date;
	private boolean result;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getService() {
		return contract.getServiceName();
	}

	public LocalDateTime getTdate() {
		if (transaction != null)
			return transaction.getDate();
		else
			return LocalDateTime.of(date, LocalTime.of(23,59,59));
	}

	public String getTransactionId() {
		if (transaction != null)
			return Integer.toString(transaction.getId());
		else
			return "";
	}

	public String getResultString() {
		if (result)
			return "Оплачено";
		else
			return "Ошибка";
	}
}
