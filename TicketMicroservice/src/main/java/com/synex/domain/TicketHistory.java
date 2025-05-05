package com.synex.domain;

import java.util.Date;
import jakarta.persistence.Entity;

@Entity
public class TicketHistory {

	private Long id;
	private Ticket ticket;
	private String action;
	private Employee actionBy;
	private Date actionDate;
	private String comments;

	public TicketHistory() {
		super();
	}

	public TicketHistory(Long id, Ticket ticket, String action, Employee actionBy, Date actionDate, String comments) {
		super();
		this.id = id;
		this.ticket = ticket;
		this.action = action;
		this.actionBy = actionBy;
		this.actionDate = actionDate;
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Employee getActionBy() {
		return actionBy;
	}

	public void setActionBy(Employee actionBy) {
		this.actionBy = actionBy;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
