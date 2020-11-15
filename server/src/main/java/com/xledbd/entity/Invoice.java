package com.xledbd.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "id_contract")
	private Contract contract;

	@OneToOne
	@JoinColumn(name = "id_transaction")
	private Transaction transaction;

	@Column(name = "amount")
	private double amount;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "result")
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
}
