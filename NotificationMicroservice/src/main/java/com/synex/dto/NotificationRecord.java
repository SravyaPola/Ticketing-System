package com.synex.dto;

import java.time.LocalDateTime;

public class NotificationRecord {
	private Long ticketId;
	private String details;
	private LocalDateTime eventTime;

	public NotificationRecord() {
	}

	public NotificationRecord(Long ticketId, String details, LocalDateTime eventTime) {
		this.ticketId = ticketId;
		this.details = details;
		this.eventTime = eventTime;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDateTime getEventTime() {
		return eventTime;
	}

	public void setEventTime(LocalDateTime eventTime) {
		this.eventTime = eventTime;
	}
}