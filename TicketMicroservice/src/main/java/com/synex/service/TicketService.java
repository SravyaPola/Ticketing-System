package com.synex.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.synex.domain.Action;
import com.synex.domain.Category;
import com.synex.domain.Priority;
import com.synex.domain.Status;
import com.synex.domain.Ticket;
import com.synex.domain.TicketHistory;
import com.synex.repository.TicketHistoryRepository;
import com.synex.repository.TicketRepository;

@Service
public class TicketService {

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	TicketHistoryRepository historyRepository;

	public void saveTicket(JsonNode json) {
		Ticket ticket = new Ticket();
		ticket.setTitle(json.get("title").asText());
		ticket.setDescription(json.get("description").asText());
		ticket.setPriority(Priority.valueOf(json.get("priority").asText().toUpperCase()));
		ticket.setCategory(Category.valueOf(json.get("category").asText().toUpperCase()));
		if (json.has("fileAttachments") && json.get("fileAttachments").isArray()) {
			List<String> attachments = new ArrayList<>();
			json.get("fileAttachments").forEach(node -> attachments.add(node.asText()));
			ticket.setFileAttachmentPath(attachments);
		}
		ticket.setStatus(Status.OPEN);
		ticket.setCreatedBy(json.get("employee").asText());
		ticket.setManagerId(json.get("managerId").asLong());
		ticketRepository.save(ticket);
		TicketHistory history = new TicketHistory();
		history.setTicket(ticket);
		history.setAction(Action.CREATED);
		history.setActionBy(json.get("employee").asText());
		history.setComments("created ticket by user");
		String role = json.get("role").asText();
		history.setRole(role);
		historyRepository.save(history);

	}

	public List<Ticket> getAllTickets(String name) {
		List<Ticket> tickets = ticketRepository.findByCreatedBy(name);
		return (tickets != null) ? tickets : new ArrayList<>();
	}

	public Optional<Ticket> getOneTicket(String id) {
		return ticketRepository.findById(Long.parseLong(id));
	}

	public void updateUserTicket(JsonNode json) {
		Ticket ticket = ticketRepository.findById(json.get("id").asLong())
				.orElseThrow(() -> new RuntimeException("Ticket not found"));

		if (json.hasNonNull("priority")) {
			ticket.setPriority(Priority.valueOf(json.get("priority").asText().toUpperCase()));
		}
		if (json.hasNonNull("category")) {
			ticket.setCategory(Category.valueOf(json.get("category").asText().toUpperCase()));
		}
		if (json.hasNonNull("description")) {
			ticket.setDescription(json.get("description").asText());
		}
		if (json.hasNonNull("status")) {
			ticket.setStatus(Status.valueOf(json.get("status").asText().toUpperCase()));
		}
		if (json.hasNonNull("assignee")) {
			ticket.setAssignee(json.get("assignee").asText());
		}
		if (json.has("attachments") && json.get("attachments").isArray()) {
			List<String> newAttachments = new ArrayList<>();
			json.get("attachments").forEach(node -> {
				if (node != null && !node.isNull()) {
					newAttachments.add(node.asText());
				}
			});
			List<String> existingAttachments = ticket.getFileAttachmentPath();
			if (existingAttachments == null) {
				existingAttachments = new ArrayList<>();
			}
			existingAttachments.addAll(newAttachments);
			ticket.setFileAttachmentPath(existingAttachments);
		}
		ticketRepository.save(ticket);
	}

	public void updateTicketHistory(JsonNode json) {
		Ticket ticket = ticketRepository.findById(json.get("id").asLong())
				.orElseThrow(() -> new RuntimeException("Ticket not found"));
		TicketHistory history = new TicketHistory();
		history.setTicket(ticket);
		String status = json.get("status").asText().toUpperCase();
		history.setAction(Action.valueOf(status));
		history.setActionBy(json.get("employee").asText());
		String role = json.get("role").asText();
		history.setRole(role);
		if (status.equals("APPROVED")) {
			history.setComments("Approved ticket by manager");
		}
		if (status.equals("REJECTED")) {
			history.setComments("Rejected ticket by manager");
		}
		if (role.equals("USER") && status.equals("CLOSED")) {
			history.setComments("Closed ticket by user");
		}
		if (role.equals("USER") && status.equals("REOPENED")) {
			history.setComments("Reopened ticket by user");
		}
		if (role.equals("ADMIN") && status.equals("CLOSED")) {
			history.setComments("Closed ticket by admin");
		}
		if (role.equals("ADMIN") && status.equals("REOPENED")) {
			history.setComments("Reopened ticket by admin");
		}
		if (status.equals("RESOLVED")) {
			history.setComments("Resolved ticket by admin");
		}
		if (status.equals("ASSIGNED")) {
			history.setComments("Assigned ticket to admin by manager");
		}
		historyRepository.save(history);
	}

	public List<Ticket> getAllTicketsToApprove(String id) {
		List<Status> toApprove = Arrays.asList(Status.OPEN, Status.REOPENED, Status.APPROVED);
		return ticketRepository.findByManagerIdAndStatusIn(Long.parseLong(id), toApprove);
	}

	public List<TicketHistory> getTicketHistory(Long id) {
		return historyRepository.findByTicketId(id);
	}

	public List<Ticket> getAllTicketsToResolve(String name) {
		return ticketRepository.findByStatusAndAssignee(Status.ASSIGNED, name);
	}

}
