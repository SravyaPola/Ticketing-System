package com.synex.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
import com.synex.service.AuthService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Value("${file.upload.dir}")
	private String uploadDir;

	@Autowired
	MyClient client;

	@Autowired
	private AuthService authService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/user/create-ticket")
	public String showCreateTicketPage(Model model) {
		return "create-ticket";
	}

	@PostMapping("/user/submit-ticket")
	public String createTicket(@ModelAttribute TicketDto ticketDto, @RequestParam("files") MultipartFile[] files,
			Model model, Principal principal) {
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
						String fileName = principal.getName() + LocalDateTime.now() + "_" + file.getOriginalFilename();
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
		Employee employee = employeeRepository.findByName(principal.getName());
		ticketDto.setManagerId(employee.getManagerId());
		ticketDto.setRole("USER");
		String message = client.sendToCreateTicket(ticketDto);
		if (!message.equals("Failed")) {
			model.addAttribute("message", "Token Successfully Created");
		} else {
			model.addAttribute("message", "Token Failed to Create");
		}
		model.addAttribute("ticket", ticketDto);
		return "create-ticket";

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@GetMapping("/user/ticket-list")
	public String showListOfTickets(Model model, Principal principal, HttpSession session) {
		String jsonString = client.sendToGetListOfTickets(principal.getName());
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<TicketDto> ticketList = null;
		try {
			ticketList = mapper.readValue(jsonString, new TypeReference<List<TicketDto>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("activeRole", "USER");
		model.addAttribute("ticketList", ticketList);
		return "ticket-list";
	}

	@GetMapping("/user/view-ticket/{id}")
	public String viewTicket(@PathVariable String id, Model model, HttpSession session) {
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
		model.addAttribute("activeRole", "USER");
		model.addAttribute("ticket", ticket);
		return "view-ticket";
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@GetMapping("/user/update-ticket/{id}")
	public String viewUpdateTicket(@PathVariable String id, Model model) {
		String jsonString = client.sendToGetOneTicket(id);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TicketDto ticket = null;
		try {
			ticket = mapper.readValue(jsonString, TicketDto.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("ticket", ticket);
		return "update-ticket";
	}

	@PostMapping("/user/close-ticket/{id}")
	public String closeTicket(@PathVariable String id, Model model, Principal principal) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("status", "CLOSED");
		jsonMap.put("id", id);
		jsonMap.put("role", "USER");
		jsonMap.put("employee", principal.getName());
		model.addAttribute("activeRole", "USER");
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "view-ticket";
		}
		String result = client.sendToUpdateTicketByUser(json);
		if (result.equals("Success")) {
			model.addAttribute("message", "Successfully Closed Ticket");
		} else {
			model.addAttribute("message", "Failed To Close Ticket");
		}
		return "view-ticket";
	}

	@PostMapping("/user/reopen-ticket/{id}")
	public String reopenTicket(@PathVariable String id, Model model, Principal principal) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("status", "REOPENED");
		jsonMap.put("role", "USER");
		jsonMap.put("id", id);
		jsonMap.put("employee", principal.getName());
		model.addAttribute("activeRole", "USER");
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "view-ticket";
		}
		String result = client.sendToUpdateTicketByUser(json);
		if (result.equals("Success")) {
			model.addAttribute("message", "Successfully Reopened Ticket");
		} else {
			model.addAttribute("message", "Failed To Reopen Ticket");
		}
		return "view-ticket";
	}

	@PostMapping("/user/update-ticket/{id}")
	public String updateTicket(@PathVariable String id, @ModelAttribute TicketDto ticketDto,
			@RequestParam("files") MultipartFile[] files, Model model, Principal principal) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("priority", ticketDto.getPriority());
		jsonMap.put("category", ticketDto.getCategory());
		jsonMap.put("description", ticketDto.getDescription());
		jsonMap.put("id", ticketDto.getId());
		List<String> fileNames = new ArrayList<>();
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					try {
						File dir = new File(uploadDir);
						if (!dir.exists())
							dir.mkdirs();
						String fileName = principal.getName() + "_" + LocalDateTime.now() + "_"
								+ file.getOriginalFilename();
						String fullPath = uploadDir + "/" + fileName;
						file.transferTo(new File(fullPath));
						fileNames.add(fullPath);
					} catch (IOException e) {
						e.printStackTrace();
						model.addAttribute("message", "Failed to save file: " + file.getOriginalFilename());
						return "update-ticket";
					}
				}
			}
		}

		jsonMap.put("attachments", fileNames);
		jsonMap.put("employee", principal.getName());
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed to convert from String to Json");
			return "update-ticket";
		}
		String response = client.sendToUpdateTicketByUser(json);
		if (response.equals("Success")) {
			model.addAttribute("message", "Successfully Updated Ticket");
		} else {
			model.addAttribute("message", "Failed To Update Ticket");
		}
		return "update-ticket";
	}

	@GetMapping("/user/ticket-history/{id}")
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
		model.addAttribute("activeRole", "USER");
		model.addAttribute("ticketHistoryList", ticketHistoryList);
		return "ticket-history";
	}

}
