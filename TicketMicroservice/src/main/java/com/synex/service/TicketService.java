package com.synex.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.synex.domain.TicketEvent;
import com.synex.domain.TicketHistory;
import com.synex.repository.TicketHistoryRepository;
import com.synex.repository.TicketRepository;

@Service
public class TicketService {

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	TicketHistoryRepository historyRepository;

	private final JmsProducer producer;

	public TicketService(JmsProducer producer) {
		this.producer = producer;
	}

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
		Ticket ticketSaved = ticketRepository.save(ticket);

		TicketHistory history = new TicketHistory();
		history.setTicket(ticket);
		history.setAction(Action.CREATED);
		history.setActionBy(json.get("employee").asText());
		history.setComments("created ticket by user.");
		String role = json.get("role").asText();
		history.setRole(role);
		historyRepository.save(history);

		TicketEvent eventForUser = new TicketEvent();
		eventForUser.setEmployeeId(json.get("employee").asText());
		eventForUser.setMessage("You have Created A Ticket with id : " + ticketSaved.getId()
				+ ". Please save it for futher reference.");
		eventForUser.setStatus("CREATED");
		String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		eventForUser.setReceivedAt(now);
		producer.send(eventForUser);

		TicketEvent eventForManager = new TicketEvent();
		eventForManager.setReceivedAt(now);
		eventForManager.setStatus(Status.OPEN.toString());
		eventForManager.setTicketId(ticketSaved.getId().toString());
		eventForManager.setManagerId(ticketSaved.getManagerId().toString());
		eventForManager.setMessage(
				"Ticket " + ticket.getId() + " was created by " + ticketSaved.getCreatedBy() + ". Please approve it.");
		producer.send(eventForManager);

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

		TicketEvent eventForUser = new TicketEvent();
		String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		eventForUser.setReceivedAt(now);
		eventForUser.setStatus(Action.valueOf(status).toString());
		eventForUser.setTicketId(ticket.getId().toString());
		eventForUser.setEmployeeId(ticket.getCreatedBy());

		if (status.equals("APPROVED")) {
			history.setComments("Ticket " + ticket.getId() + " was approved by " + json.get("employee").asText());
			eventForUser.setMessage("Ticket " + ticket.getId() + " was approved by " + json.get("employee").asText());
		}
		if (status.equals("REJECTED")) {
			history.setComments(json.get("reason").asText());
			eventForUser.setMessage("Ticket " + ticket.getId() + " was rejected by " + json.get("employee").asText()
					+ ". Please check your email for rejection reason. Further Steps are enclosed in it.");
		}
		if (role.equals("USER") && status.equals("CLOSED")) {
			history.setComments("Ticket " + ticket.getId() + " was closed by user.");
			eventForUser.setMessage("Ticket " + ticket.getId() + " was closed by you.");
		}
		if (status.equals("PENDING_FOR_APPROVAL")) {
			history.setComments("Ticket " + ticket.getId() + " was send for approval to your manager.");
			eventForUser.setMessage("Ticket " + ticket.getId() + " was send for approval to your manager.");
		}
		if (role.equals("USER") && status.equals("REOPENED")) {
			history.setComments("Ticket " + ticket.getId() + " was reopened by user.");
			eventForUser.setMessage("Ticket " + ticket.getId() + " was reopened by you.");
		}
		if (role.equals("ADMIN") && status.equals("CLOSED")) {
			history.setComments("Ticket " + ticket.getId() + " was closed by " + json.get("employee").asText());
			eventForUser.setMessage("Ticket " + ticket.getId() + " was closed by " + json.get("employee").asText());
		}
		if (role.equals("ADMIN") && status.equals("REOPENED")) {
			history.setComments("Ticket " + ticket.getId() + " was reopened by " + json.get("employee").asText());
			eventForUser.setMessage("Ticket " + ticket.getId() + " was reopened by " + json.get("employee").asText());
		}
		if (status.equals("RESOLVED")) {
			history.setComments(json.get("comments").asText());
			eventForUser.setMessage("Ticket " + ticket.getId() + " was resolved by " + json.get("employee").asText());
		}
		if (status.equals("ASSIGNED")) {
			history.setComments("Ticket " + ticket.getId() + " was assigned by " + json.get("employee").asText());
			eventForUser.setMessage("Ticket " + ticket.getId() + " was assigned by " + json.get("employee").asText());
		}
		historyRepository.save(history);
		producer.send(eventForUser);
		if (status.equals("APPROVED")) {
			TicketEvent eventForManager = new TicketEvent();
			eventForManager.setReceivedAt(now);
			eventForManager.setStatus(Action.valueOf(status).toString());
			eventForManager.setTicketId(ticket.getId().toString());
			eventForManager.setManagerId(ticket.getManagerId().toString());
			eventForManager.setMessage("Ticket " + ticket.getId() + " was approved. Please Assign it to Admin.");
			producer.send(eventForManager);
		}
		if (status.equals("PENDING_FOR_APPROVAL")) {
			TicketEvent eventForManager = new TicketEvent();
			eventForManager.setReceivedAt(now);
			eventForManager.setStatus(Action.valueOf(status).toString());
			eventForManager.setTicketId(ticket.getId().toString());
			eventForManager.setManagerId(ticket.getManagerId().toString());
			eventForManager.setMessage("Ticket " + ticket.getId() + " was send for approval by "
					+ json.get("employee").asText() + ". Please Approve it.");
			producer.send(eventForManager);
		}
		String admin = ticket.getAssignee();
		if (admin != null) {
			if (status.equals("REOPENED") || status.equals("ASSIGNED")) {
				TicketEvent eventForAdmin = new TicketEvent();
				eventForAdmin.setReceivedAt(now);
				eventForAdmin.setStatus(Action.valueOf(status).toString());
				eventForAdmin.setTicketId(ticket.getId().toString());
				eventForAdmin.setAdminId(admin);
				if (status.equals("REOPENED")) {
					eventForAdmin.setMessage("Ticket " + ticket.getId() + " was reopened by "
							+ json.get("employee").asText() + ". Please resolve it.");
				}
				if (status.equals("ASSIGNED")) {
					eventForAdmin.setMessage("Ticket " + ticket.getId() + " was assigned to you by "
							+ json.get("employee").asText() + ". Please resolve it.");
				}
				producer.send(eventForAdmin);
			}
		}
	}

	public List<Ticket> getAllTicketsToApprove(String id) {
		List<Status> toApprove = Arrays.asList(Status.OPEN, Status.APPROVED, Status.PENDING_FOR_APPROVAL);
		return ticketRepository.findByManagerIdAndStatusIn(Long.parseLong(id), toApprove);
	}

	public List<TicketHistory> getTicketHistory(Long id) {
		return historyRepository.findByTicketId(id);
	}

	public List<Ticket> getAllTicketsToResolve(String name) {
		List<Status> toResolve = Arrays.asList(Status.REOPENED, Status.ASSIGNED);
		return ticketRepository.findByAssigneeAndStatusIn(name, toResolve);
	}

}
