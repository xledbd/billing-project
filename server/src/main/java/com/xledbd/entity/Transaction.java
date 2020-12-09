package com.xledbd.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "id_contract_from")
	private Contract contractFrom;

	@ManyToOne
	@JoinColumn(name = "id_contract_to")
	private Contract contractTo;

	@Column(name = "amount")
	private double amount;

	@Column(name = "date")
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

	@Transient
	public String getType() {
		if (contractFrom == null) return "Пополнение счета";
		else return "Оплата";
	}

	@Transient
	public String getService() {
		Contract contract;
		if (contractFrom != null) contract = contractFrom;
		else contract = contractTo;
		return contract.getServiceName();
	}
}
