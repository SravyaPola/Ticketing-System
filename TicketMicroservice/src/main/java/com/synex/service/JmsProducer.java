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
        jmsTemplate.convertAndSend("messages.queue", event);
        System.out.println("Sent message: " + event);
    }
}
