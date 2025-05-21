package com.synex.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import com.synex.domain.TicketEvent;
import com.synex.repository.NotificationRepository;

@Component
public class TicketEventListener {
	private final JmsTemplate jms;

	public TicketEventListener(JmsTemplate jms) {

		this.jms = jms;
	}

	@Autowired
	NotificationRepository notificationRepository;

	@JmsListener(destination = "messages.queue", containerFactory = "myFactory")
	public void receive(TicketEvent event) {
		System.out.println("Received message: " + event);
		TicketEvent saved = notificationRepository.save(event);
		jms.convertAndSend("notifications.queue", saved);
	}
}
