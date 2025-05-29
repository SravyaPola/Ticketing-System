package com.synex.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.synex.domain.TicketEvent;

@Service
public class JmsProducer {
	private final JmsTemplate jmsTemplate;

	public JmsProducer(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void send(TicketEvent event) {
		jmsTemplate.convertAndSend("ticket.queue", event, message -> {
			if (event.getEmployeeId() != null) {
				message.setStringProperty("userId", event.getEmployeeId());
			} else if (event.getManagerId() != null) {
				message.setStringProperty("managerId", event.getManagerId());
			} else if (event.getAdminId() != null) {
				message.setStringProperty("adminId", event.getAdminId());
			}
			return message;
		});
		System.out.println("Sent message: " + event);
	}
}
