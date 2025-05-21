package com.synex.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synex.domain.Ticket;
import com.synex.domain.TicketEvent;
import com.synex.domain.TicketHistory;
import com.synex.service.JmsProducer;
import com.synex.service.TicketService;

@Controller
public class TicketController {

	@Autowired
	TicketService ticketService;

	private final JmsProducer producer;

	public TicketController(JmsProducer producer) {
		this.producer = producer;
	}

	@PostMapping("/user/create-ticket")
	public ResponseEntity<String> createTicket(@RequestBody JsonNode json) {
		ticketService.saveTicket(json);
		TicketEvent e = new TicketEvent();
		e.setEmployeeId("1");
		e.setMessage("Created");
		e.setStatus("CREATED");
		producer.send(e);
		return ResponseEntity.ok("Successfully Token was created");
	}

	@PostMapping("/user/ticket-list")
	public ResponseEntity<String> getAllTickets(@RequestBody String name) {
		List<Ticket> allTickets = ticketService.getAllTickets(name);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(allTickets);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error serializing ticket list: " + e.getMessage());
		}
	}

	@PostMapping("/user/view-ticket")
	public ResponseEntity<String> getOneTicket(@RequestBody String id) {
		Optional<Ticket> Ticket = ticketService.getOneTicket(id);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(Ticket.get());
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping("/user/update-ticket")
	public ResponseEntity<String> updateTicketByUser(@RequestBody JsonNode json) {
		ticketService.updateUserTicket(json);
		if (json.has("status")) {
			ticketService.updateTicketHistory(json);
		}
		return ResponseEntity.ok("Successfully Ticket was Updated");
	}

	@PostMapping("/manager/update-ticket")
	public ResponseEntity<String> updateTicketByManager(@RequestBody JsonNode json) {
		ticketService.updateUserTicket(json);
		ticketService.updateTicketHistory(json);
		return ResponseEntity.ok("Successfully Ticket was Updated");
	}

	@PostMapping("/admin/update-ticket")
	public ResponseEntity<String> updateTicketByAdmin(@RequestBody JsonNode json) {
		ticketService.updateUserTicket(json);
		ticketService.updateTicketHistory(json);
		return ResponseEntity.ok("Successfully Ticket was Updated");
	}

	@PostMapping("/manager/tickets-to-approve")
	public ResponseEntity<String> getAllTicketsToApprove(@RequestBody String id) {
		List<Ticket> allTickets = ticketService.getAllTicketsToApprove(id);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(allTickets);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping("/admin/tickets-to-resolve")
	public ResponseEntity<String> getAllTicketsToResolve(@RequestBody String name) {
		List<Ticket> allTickets = ticketService.getAllTicketsToResolve(name);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(allTickets);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping("/ticket-history")
	public ResponseEntity<String> getTicketHistory(@RequestBody String id) {
		List<TicketHistory> allTickets = ticketService.getTicketHistory(Long.parseLong(id));
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(allTickets);
			return ResponseEntity.ok(json);
		} catch (Exception e) {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping("/manager/assign-ticket")
	public ResponseEntity<String> updateTicketByManagerToAssign(@RequestBody JsonNode json) {
		ticketService.updateUserTicket(json);
		ticketService.updateTicketHistory(json);
		return ResponseEntity.ok("Successfully Ticket was Updated");
	}

}
