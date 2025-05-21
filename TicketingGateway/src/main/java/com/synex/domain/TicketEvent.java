package com.synex.domain;

import java.io.Serializable;

public class TicketEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private String employeeId;
	private String message;
	private String status;

	public TicketEvent() {
		super();
	}

	public TicketEvent(String employeeId, String message, String status) {
		super();
		this.employeeId = employeeId;
		this.message = message;
		this.status = status;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

}
