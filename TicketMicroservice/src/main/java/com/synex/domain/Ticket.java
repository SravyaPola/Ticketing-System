package com.synex.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.JoinColumn;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	@Enumerated(EnumType.STRING)
	private Priority priority;
	@Enumerated(EnumType.STRING)
	private Status status;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false, nullable = false)
	private Date creationDate;
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private Date updationDate;
	@Enumerated(EnumType.STRING)
	private Category category;
	@ElementCollection
	@CollectionTable(name = "ticket_attachments", joinColumns = @JoinColumn(name = "ticket_id"))
	@Column(name = "file_path")
	private List<String> fileAttachmentPath = new ArrayList<>();;
	private String createdBy;
	private String assignee;
	private Long managerId;

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<TicketHistory> history;

	public Ticket() {
		super();
	}

	public Ticket(Long id, String title, String description, Priority priority, Status status, Date creationDate,
			Date updationDate, Category category, List<String> fileAttachmentPath, String createdBy, String assignee,
			List<TicketHistory> history, Long managerId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.creationDate = creationDate;
		this.category = category;
		this.fileAttachmentPath = fileAttachmentPath;
		this.createdBy = createdBy;
		this.updationDate = updationDate;
		this.assignee = assignee;
		this.history = history;
		this.managerId = managerId;
	}

	public Date getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
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

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<String> getFileAttachmentPath() {
		return fileAttachmentPath;
	}

	public void setFileAttachmentPath(List<String> fileAttachmentPath) {
		this.fileAttachmentPath = fileAttachmentPath;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public List<TicketHistory> getHistory() {
		return history;
	}

	public void setHistory(List<TicketHistory> history) {
		this.history = history;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

}
