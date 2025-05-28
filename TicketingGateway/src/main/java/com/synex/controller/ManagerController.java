package com.synex.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synex.component.MyClient;
import com.synex.domain.Employee;
import com.synex.model.TicketDto;
import com.synex.model.TicketHistoryDto;
import com.synex.repository.EmployeeRepository;

@Controller
public class ManagerController {

	@Autowired
	MyClient client;

	@Autowired
	private EmployeeRepository employeeRepository;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@GetMapping("/manager/tickets-to-approve")
	public String showListOfTicketsToManager(Model model, Principal principal) {
		Employee employee = employeeRepository.findByName(principal.getName());
		String jsonString = client.sendToGetListOfAllTicketsToApprove(employee.getId().toString());
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<TicketDto> ticketList = null;
		try {
			ticketList = mapper.readValue(jsonString, new TypeReference<List<TicketDto>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Employee> admins = employeeRepository.findByRoles_Name("ADMIN");
		model.addAttribute("admins", admins);
		model.addAttribute("activeRole", "MANAGER");
		model.addAttribute("ticketList", ticketList);
		return "ticket-list";
	}

	@GetMapping("/manager/view-ticket/{id}")
	public String viewTicketToManager(@PathVariable String id, Model model) {
		String jsonString = client.sendToGetOneTicket(id);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TicketDto ticket = null;
		try {
			ticket = mapper.readValue(jsonString, TicketDto.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectMapper mapperToAttachments = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = mapperToAttachments.readTree(jsonString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		List<String> attachments = new ArrayList<>();
		JsonNode attachmentNode = rootNode.get("fileAttachmentPath");
		if (attachmentNode != null && attachmentNode.isArray()) {
			for (JsonNode node : attachmentNode) {
				attachments.add(node.asText());
			}
		}
		ticket.setFileAttachments(attachments);
		model.addAttribute("activeRole", "MANAGER");
		model.addAttribute("ticket", ticket);
		return "view-ticket";
	}

	@PostMapping("/manager/approve-ticket/{id}")
	public String approveTicket(@PathVariable String id, Model model, TicketDto ticketDto, Principal principal) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("status", "APPROVED");
		jsonMap.put("id", ticketDto.getId());
		jsonMap.put("role", "MANAGER");
		jsonMap.put("employee", principal.getName());
		model.addAttribute("activeRole", "MANAGER");
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "view-ticket";
		}
		String result = client.sendToUpdateTicketByManager(json);
		if (result.equals("Success")) {
			model.addAttribute("message", "Successfully Approved Ticket");
		} else {
			model.addAttribute("message", "Failed To Approve Ticket");
		}
		return "view-ticket";
	}

	@PostMapping("/manager/assign-ticket/{ticketId}")
	public ResponseEntity<String> assignTicket(@PathVariable Long ticketId, @RequestParam String assignee,
			Principal principal) {
		try {
			client.assignTicketToAdmin(ticketId, assignee, principal.getName());
			return ResponseEntity.ok("Assigned successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Assignment failed");
		}
	}

	@PostMapping("/manager/reject-ticket/{id}")
	public String rejectTicket(@PathVariable String id, @RequestParam("reason") String reason, TicketDto ticketDto,
			Model model, Principal principal) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("status", "REJECTED");
		jsonMap.put("id", ticketDto.getId());
		jsonMap.put("role", "MANAGER");
		jsonMap.put("employee", principal.getName());
		jsonMap.put("reason", reason);
		System.out.println(reason);
		model.addAttribute("activeRole", "MANAGER");
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "view-ticket";
		}
		String result = client.sendToUpdateTicketByManager(json);
		if (result.equals("Success")) {
			model.addAttribute("message", "Successfully Rejected Ticket");
		} else {
			model.addAttribute("message", "Failed To Reject Ticket");
		}
		return "view-ticket";
	}

	@GetMapping("/manager/ticket-history/{id}")
	public String showTicketHistory(@PathVariable String id, Model model, Principal principal) {
		String jsonString = client.sendToGetTicketHistory(id);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<TicketHistoryDto> ticketHistoryList = null;
		try {
			ticketHistoryList = mapper.readValue(jsonString, new TypeReference<List<TicketHistoryDto>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("activeRole", "MANAGER");
		model.addAttribute("ticketHistoryList", ticketHistoryList);
		return "ticket-history";
	}

}
