package com.synex.model;

import java.util.Date;

public class TicketHistoryDto {

	private Long id;
	private String action;
	private String actionDate;
	private String comments;
	private String actionBy;
	private String role;
	private Long ticketid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}

	public Long getTicketid() {
		return ticketid;
	}

	public void setTicketid(Long ticketid) {
		this.ticketid = ticketid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getActionDateAsDate() {
		try {
			return new Date(Long.parseLong(actionDate));
		} catch (Exception e) {
			return null;
		}
	}

}
