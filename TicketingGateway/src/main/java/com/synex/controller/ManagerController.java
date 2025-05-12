package com.synex.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synex.component.MyClient;
import com.synex.model.TicketDto;
import com.synex.service.AuthService;

@Controller
public class ManagerController {

	@Autowired
	MyClient client;

	@Autowired
	private AuthService authService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@GetMapping("/manager/tickets-to-approve")
	public String showListOfTicketsToManager(Model model) {
		String jsonString = client.sendToGetListOfAllTicketsToApprove();
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<TicketDto> ticketList = null;
		try {
			ticketList = mapper.readValue(jsonString, new TypeReference<List<TicketDto>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
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

		model.addAttribute("activeRole", "MANAGER");
		model.addAttribute("ticket", ticket);
		return "view-ticket";
	}

	@GetMapping("/manager/approve-ticket/{id}")
	public String approveTicket(@PathVariable String id, Model model, TicketDto ticketDto) {
		model.addAttribute("ticket", ticketDto);
		model.addAttribute("message", "Ticket Approved");
		return "view-ticket";
	}

	@GetMapping("/manager/reject-ticket/{id}")
	public String rejectTicket(@PathVariable String id, @RequestParam("reason") String reason,Model model,  TicketDto ticketDto) {
		model.addAttribute("ticket", ticketDto);
		model.addAttribute("message", "Ticket Rejected");
		return "view-ticket";
	}

}
