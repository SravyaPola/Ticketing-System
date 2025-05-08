package com.synex.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.synex.component.MyClient;
import com.synex.model.TicketDto;
import com.synex.service.AuthService;

@Controller
public class TicketController {

	@Value("${file.upload.dir}")
	private String uploadDir;

	@Autowired
	MyClient client;

	@Autowired
	private AuthService authService;

	@GetMapping("user/create-ticket")
	public String showCreateTicketPage(Model model) {
		return "create-ticket";
	}

	@GetMapping("user/ticket-list")
	public String showListOfTickets(Model model) {
		return "ticket-list";
	}

	@GetMapping("user/ticket-history")
	public String showTicketHistory(Model model) {
		return "ticket-history";
	}

	@GetMapping("user/view-ticket/id")
	public String viewTicket(Model model) {
		return "view-ticket";
	}

	@GetMapping("user/update-ticket/id")
	public String updateTicket(Model model) {
		return "update-ticket";
	}

	@PostMapping("/submitTicket")
	public String createTicket(@ModelAttribute TicketDto ticketDto,
			@RequestParam("files") MultipartFile[] files, Model model, Principal principal) {
		ticketDto.setEmployee(principal.getName());
		ticketDto.setRole("USER");
		ticketDto.setEmail(authService.getEmail(principal.getName()));
		List<String> savedPaths = new ArrayList<>();
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					try {
						File dir = new File(uploadDir);
						if (!dir.exists())
							dir.mkdirs();
						String fileName = principal.getName() +"_" + file.getOriginalFilename();
						String fullPath = uploadDir + "/" + fileName;
						file.transferTo(new File(fullPath));
						savedPaths.add(fullPath);
					} catch (IOException e) {
						e.printStackTrace();
						model.addAttribute("message", "Failed to save file: " + file.getOriginalFilename());
						return "create-ticket";
					}
				}
			}
		}

		ticketDto.setFileAttachments(savedPaths);
		String message = client.sendToCreateTicket(ticketDto);
		if (!message.equals("Failed")) {
			model.addAttribute("message", "Token Successfully Created");
		} else {
			model.addAttribute("message", "Token Failed to Create");
		}
		model.addAttribute("ticket", ticketDto);
		return "create-ticket";

	}

}
