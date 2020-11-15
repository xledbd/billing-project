package com.xledbd.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "contract")
public class Contract implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	@OneToOne
	@JoinColumn(name = "id_service")
	private Service service;

	@Column(name = "balance")
	private double balance;

	@Column(name = "date_from")
	private LocalDate dateFrom;

	@Column(name = "date_to")
	private LocalDate dateTo;

	@Column(name = "last_invoice")
	private LocalDate lastInvoice;

	@Column(name = "is_active")
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
