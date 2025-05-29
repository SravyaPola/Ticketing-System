package com.synex.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import com.synex.domain.TicketEvent;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

@Service
public class NotificationService {
	private final JmsTemplate jms;
	private final MessageConverter converter;

	public NotificationService(JmsTemplate jmsTemplate) {
		this.jms = jmsTemplate;
		this.converter = jmsTemplate.getMessageConverter();
	}

	public List<TicketEvent> receiveAll(String idProperty, String idValue) {
		List<TicketEvent> all = new ArrayList<>();
		String selector = idProperty + " = '" + idValue + "'";
		Message raw;
		try {
			while ((raw = jms.receiveSelected("notifications.queue", selector)) != null) {
				all.add((TicketEvent) converter.fromMessage(raw));
			}
		} catch (JMSException e) {
			throw new IllegalStateException("JMS receive failed", e);
		}
		return all;
	}
}
