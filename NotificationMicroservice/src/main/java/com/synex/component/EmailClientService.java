package com.synex.component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.Map;
import java.util.*;

@Component
public class EmailClientService {

	private static final String URL_GET_EMAIL_DETAILS = "http://localhost:8282/get-email-details/{username}";
	private static final String URL_GET_HISTORY_FOR_RESOLUTION = "http://localhost:8383/ticket-history/{id}";

	private final RestTemplate restTemplate;

	public EmailClientService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public String getEmailDetails(String username) {
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(URL_GET_EMAIL_DETAILS, String.class, username);
			return response.getBody();
		} catch (RestClientException ex) {
			throw new RuntimeException("Error fetching email details for key=" + username, ex);
		}
	}

	public List<Map<String, Object>> getTicketHistory(String id) {
		try {
			ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(URL_GET_HISTORY_FOR_RESOLUTION,
					HttpMethod.GET, null, new ParameterizedTypeReference<>() {
					}, id);
			return response.getBody();
		} catch (RestClientException ex) {
			throw new RuntimeException("Error fetching history details for key=" + id, ex);
		}
	}

}
