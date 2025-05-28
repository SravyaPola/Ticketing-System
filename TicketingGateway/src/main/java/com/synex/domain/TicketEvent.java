package com.synex.domain;

import java.io.Serializable;

public class TicketEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private String employeeId;
	private String managerId;
	private String adminId;
	private String message;
	private String status;
	private String receivedAt;
	private String ticketId;
	private boolean autoTrigger;

	public TicketEvent() {
		super();
	}

	public TicketEvent(String employeeId, String managerId, String adminId, String message, String status,
			String receivedAt, String ticketId, boolean autoTrigger) {
		super();
		this.employeeId = employeeId;
		this.managerId = managerId;
		this.adminId = adminId;
		this.message = message;
		this.status = status;
		this.receivedAt = receivedAt;
		this.ticketId = ticketId;
		this.autoTrigger = autoTrigger;
	}

	public boolean isAutoTrigger() {
		return autoTrigger;
	}

	public void setAutoTrigger(boolean autoTrigger) {
		this.autoTrigger = autoTrigger;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

}
