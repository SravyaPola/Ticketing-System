package com.synex.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TicketEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Type {
		CREATED, UPDATED, RESOLVED, CLOSED
	}

	private Long ticketId;
	private Long userId;
	private String userEmail;
	private Type type;
	private LocalDateTime eventTime;
	private String details;

	public TicketEvent() {
	}

	public TicketEvent(Long ticketId, Long userId, String userEmail, Type type, LocalDateTime eventTime,
			String details) {
		this.ticketId = ticketId;
		this.userId = userId;
		this.userEmail = userEmail;
		this.type = type;
		this.eventTime = eventTime;
		this.details = details;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public LocalDateTime getEventTime() {
		return eventTime;
	}

	public void setEventTime(LocalDateTime eventTime) {
		this.eventTime = eventTime;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "TicketEvent{" + "ticketId=" + ticketId + ", userId=" + userId + ", userEmail='" + userEmail + '\''
				+ ", type=" + type + ", eventTime=" + eventTime + ", details='" + details + '\'' + '}';
	}

	public NotificationRecord toNotificationRecord() {
		// TODO Auto-generated method stub
		return null;
	}
}