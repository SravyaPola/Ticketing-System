package com.synex.model;

import java.util.List;

public class TicketDto {

	private String title;
	private String description;
	private String priority;
	private String category;
	private List<String> fileAttachments;
	private String employee;
	private String assignee;
	private String role;
	private String email;

	public TicketDto() {
		super();
	}

	public TicketDto(String title, String description, String priority, String category, List<String> fileAttachments) {
		super();
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.category = category;
		this.fileAttachments = fileAttachments;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getFileAttachments() {
		return fileAttachments;
	}

	public void setFileAttachments(List<String> fileAttachments) {
		this.fileAttachments = fileAttachments;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
