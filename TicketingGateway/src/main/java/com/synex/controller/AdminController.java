package com.synex.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synex.component.MyClient;
import com.synex.model.TicketDto;
import com.synex.model.TicketHistoryDto;
import com.synex.service.AuthService;

@Controller
public class AdminController {

	@Autowired
	MyClient client;

	@Autowired
	private AuthService authService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@GetMapping("/admin/tickets-to-resolve")
	public String showListOfTicketsToAdmin(Model model) {
		String jsonString = client.sendToGetListOfAllTicketsToResolve();
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<TicketDto> ticketList = null;
		try {
			ticketList = mapper.readValue(jsonString, new TypeReference<List<TicketDto>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("activeRole", "ADMIN");
		model.addAttribute("ticketList", ticketList);
		return "ticket-list";
	}

	@GetMapping("/admin/view-ticket/{id}")
	public String viewTicketToAdmin(@PathVariable String id, Model model) {
		String jsonString = client.sendToGetOneTicket(id);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TicketDto ticket = null;
		try {
			ticket = mapper.readValue(jsonString, TicketDto.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("files", ticket.getFileAttachments());
		model.addAttribute("activeRole", "ADMIN");
		model.addAttribute("ticket", ticket);
		return "view-ticket";
	}

	@PostMapping("/admin/resolve-ticket/{id}")
    public String approveTicket(@PathVariable String id, Model model, TicketDto ticketDto, Principal principal) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("status", "RESOLVED");
		jsonMap.put("id", ticketDto.getId());
		jsonMap.put("role", "ADMIN");
		jsonMap.put("employee", principal.getName());
		model.addAttribute("activeRole", "ADMIN");
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "view-ticket";
		}
		System.out.println(json);
        String result = client.sendToUpdateTicket(json);
        if(result.equals("Success")) {
			model.addAttribute("message", "Successfully Resolved Ticket");
		} else {
			model.addAttribute("message", "Failed To Resolve Ticket");
		}
        return "view-ticket";
    }

    @PostMapping("/admin/assign-ticket/{id}")
    public String rejectTicket(@PathVariable String id,TicketDto ticketDto, Model model, Principal principal) {
    	Map<String, Object> jsonMap = new HashMap<>();
    	jsonMap.put("status", "ASSIGNED");
		jsonMap.put("id", ticketDto.getId());
		jsonMap.put("role", "ADMIN");
		jsonMap.put("employee", principal.getName());
		model.addAttribute("activeRole", "ADMIN");
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "view-ticket";
		}
        String result = client.sendToUpdateTicket(json);
        if(result.equals("Success")) {
			model.addAttribute("message", "Successfully Assigned Ticket");
		} else {
			model.addAttribute("message", "Failed To Assign Ticket");
		}
        return "view-ticket";
    }
    
    @GetMapping("/admin/ticket-history/{id}")
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
		model.addAttribute("activeRole", "ADMIN");
		model.addAttribute("ticketHistoryList", ticketHistoryList);
		return "ticket-history";
	}

}
