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

}