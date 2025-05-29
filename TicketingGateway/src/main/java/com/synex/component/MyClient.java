package com.synex.component;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synex.model.TicketDto;

@Component
public class MyClient {

	private static final String urlToCreateTicket = "http://localhost:8383/user/create-ticket";
	private static final String urlToGetListOfTickets = "http://localhost:8383/user/ticket-list";
	private static final String urlToGetOneTicket = "http://localhost:8383/user/view-ticket";
	private static final String urlToUpdateTicketByUser = "http://localhost:8383/user/update-ticket";
	private static final String urlToGetListOfAllTicketsToApprove = "http://localhost:8383/manager/tickets-to-approve";
	private static final String urlToGetListOfAllTicketsToResolve = "http://localhost:8383/admin/tickets-to-resolve";
	private static final String urlToGetTicketHistory = "http://localhost:8383/ticket-history";
	private static final String urlToUpdateTicketByManager = "http://localhost:8383/manager/update-ticket";
	private static final String urlToUpdateTicketByAdmin = "http://localhost:8383/admin/update-ticket";
	private static final String urlToUpdateTicketToAssign = "http://localhost:8383/manager/assign-ticket";
	private static final String urlToSendForApproval = "http://localhost:8383/user/send-for-approval";

	public String sendToCreateTicket(TicketDto ticketDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<TicketDto> requestEntity = new HttpEntity<>(ticketDto, headers);
		try {
			restTemplate.postForEntity(urlToCreateTicket, requestEntity, String.class);
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			System.err.println("Error response: " + ex.getResponseBodyAsString());
			return "Failed";
		}
	}

	public String sendToGetListOfTickets(String name) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(name, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetListOfTickets, requestEntity,
				String.class);
		String response = responseEntity.getBody();
		return response;

	}

	public String sendToGetOneTicket(String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(id, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetOneTicket, requestEntity,
				String.class);
		String response = responseEntity.getBody();
		return response;
	}

	public String sendToUpdateTicketByUser(String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		try {
			restTemplate.postForEntity(urlToUpdateTicketByUser, requestEntity, String.class);
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			System.err.println("Error response: " + ex.getResponseBodyAsString());
			return "Failed";
		}

	}

	public String sendToUpdateTicketByManager(String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		try {
			restTemplate.postForEntity(urlToUpdateTicketByManager, requestEntity, String.class);
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			System.err.println("Error response: " + ex.getResponseBodyAsString());
			return "Failed";
		}

	}

	public String sendToUpdateTicketByAdmin(String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		try {
			restTemplate.postForEntity(urlToUpdateTicketByAdmin, requestEntity, String.class);
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			System.err.println("Error response: " + ex.getResponseBodyAsString());
			return "Failed";
		}

	}

	public String sendToGetListOfAllTicketsToApprove(String id) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(id, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetListOfAllTicketsToApprove,
				requestEntity, String.class);
		String response = responseEntity.getBody();
		return response;
	}

	public String sendToGetTicketHistory(String id) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(id, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetTicketHistory, requestEntity,
				String.class);
		String response = responseEntity.getBody();
		return response;
	}

	public String sendToGetListOfAllTicketsToResolve(String name) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(name, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetListOfAllTicketsToResolve,
				requestEntity, String.class);
		String response = responseEntity.getBody();
		return response;
	}

	public String assignTicketToAdmin(Long ticketId, String assignee, String employee) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> body = new HashMap<>();
		body.put("id", ticketId);
		body.put("assignee", assignee);
		body.put("status", "ASSIGNED");
		body.put("role", "MANAGER");
		body.put("employee", employee);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody;
		try {
			jsonBody = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			return "Failed";
		}
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForEntity(urlToUpdateTicketToAssign, requestEntity, String.class);
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			return "Failed";
		}

	}

}