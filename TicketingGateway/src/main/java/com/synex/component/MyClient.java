package com.synex.component;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.synex.model.TicketDto;

@Component
public class MyClient {

	private static final String urlToCreateTicket = "http://localhost:8383/user/create-ticket";
	private static final String urlToGetListOfTickets = "http://localhost:8383/user/ticket-list";
	private static final String urlToGetOneTicket = "http://localhost:8383/user/view-ticket";
	private static final String urlToUpdateTicket = "http://localhost:8383/user/update-ticket";
	private static final String urlToGetListOfAllTickets = "http://localhost:8383/manager/tickets-to-approve";

	public String sendToCreateTicket(TicketDto ticketDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<TicketDto> requestEntity = new HttpEntity<>(ticketDto, headers);
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(urlToCreateTicket, requestEntity,
					String.class);
			System.out.println("Response from auth service: " + response.getBody());
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			System.err.println("Error response: " + ex.getResponseBodyAsString());
			return "Failed";
		}
	}

	public String sendToGetListOfTickets(String name) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(name, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetListOfTickets, requestEntity,
				String.class);
		String response = responseEntity.getBody();
		return response;
	}

	public String sendToGetOneTicket(String id) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(id, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetOneTicket, requestEntity,
				String.class);
		String response = responseEntity.getBody();
		return response;
	}

	public String sendToUpdateTicket(String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(urlToUpdateTicket, requestEntity,
					String.class);
			System.out.println("Response from auth service: " + response.getBody());
			return "Success";
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			System.err.println("Error response: " + ex.getResponseBodyAsString());
			return "Failed";
		}
		
	}

	public String sendToGetListOfAllTicketsToApprove() {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlToGetListOfAllTickets, requestEntity,
				String.class);
		String response = responseEntity.getBody();
		return response;
	}

}