package com.synex.controller;

import com.synex.domain.TicketEvent;
import com.synex.service.TicketEventStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class TicketEventController {

	private final TicketEventStore store;

	public TicketEventController(TicketEventStore store) {
		this.store = store;
	}
	
	  @GetMapping("/user/notifications")
	  public String showNotifications(Model model) {
	    List<TicketEvent> all = store.findAll();
	    model.addAttribute("notifications", all);
	    store.markAllRead();
	    return "user-notifications"; 
	  }
}
