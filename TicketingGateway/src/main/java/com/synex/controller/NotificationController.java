package com.synex.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.synex.domain.Employee;
import com.synex.domain.TicketEvent;
import com.synex.repository.EmployeeRepository;
import com.synex.service.TicketEventStore;

@Controller
public class NotificationController {

	private final TicketEventStore store;

	public NotificationController(TicketEventStore store) {
		this.store = store;
	}

	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping("/user/notifications")
	public String showNotificationsToUser(Model model, Principal principal) {
		List<TicketEvent> user = store.findUnreadFor(principal.getName());
		model.addAttribute("notifications", user);
		store.markAllReadFor(principal.getName());
		return "user-notifications";
	}

	@GetMapping("/manager/notifications")
	public String showNotificationsToManager(Model model, Principal principal) {
		Employee employee = employeeRepository.findByName(principal.getName());
		List<TicketEvent> manager = store.findUnreadFor(employee.getId().toString());
		model.addAttribute("notifications", manager);
		store.markAllReadFor(employee.getId().toString());
		return "manager-notifications";
	}

	@GetMapping("/admin/notifications")
	public String showNotificationsToAdmin(Model model, Principal principal) {
		List<TicketEvent> admin = store.findUnreadFor(principal.getName());
		model.addAttribute("notifications", admin);
		store.markAllReadFor(principal.getName());
		return "admin-notifications";
	}

}
