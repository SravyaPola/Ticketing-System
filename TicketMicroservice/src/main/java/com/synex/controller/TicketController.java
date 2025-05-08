package com.synex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.fasterxml.jackson.databind.JsonNode;
import com.synex.service.TicketService;

@Controller
public class TicketController {

	@Autowired
	TicketService ticketService;

	@PostMapping("user/create-ticket")
	public ResponseEntity<String> register(@RequestBody JsonNode json) {
		ticketService.saveTicket(json);
		return ResponseEntity.ok("Successfully Token was created");
	}

}
