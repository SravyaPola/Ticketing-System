package com.synex.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.synex.domain.Category;
import com.synex.domain.Priority;
import com.synex.domain.Status;
import com.synex.domain.Ticket;
import com.synex.repository.TicketRepository;

@Service
public class TicketService {

	@Autowired
	TicketRepository ticketRepository;

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
		ticketRepository.save(ticket);
	}
}
