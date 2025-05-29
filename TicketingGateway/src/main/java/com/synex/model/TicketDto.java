package com.synex.model;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketDto {

	private Long id;
	private String title;
	private String description;
	private String priority;
	private String category;
	private String status;
	private List<String> fileAttachments;
	private String employee;
	private String assignee;
	private Long managerId;
	private String role;
	private String email;
	private Date creationDate;
	private String createdBy;

	public TicketDto() {
		super();
	}

	public TicketDto(Long id, String title, String description, String priority, String category, String status,
			List<String> fileAttachments, String employee, String assignee, Long managerId, String role, String email,
			Date creationDate, String createdBy) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.category = category;
		this.status = status;
		this.fileAttachments = fileAttachments;
		this.employee = employee;
		this.assignee = assignee;
		this.managerId = managerId;
		this.role = role;
		this.email = email;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

}
