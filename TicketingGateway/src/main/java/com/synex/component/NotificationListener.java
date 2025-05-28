package com.synex.component;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.synex.domain.TicketEvent;
import com.synex.service.TicketEventStore;

@Component
public class NotificationListener {

	private final TicketEventStore store;

	public NotificationListener(TicketEventStore store) {
		this.store = store;
	}

	@JmsListener(destination = "notifications.queue", containerFactory = "myFactory")
	public void handleTicketEvent(TicketEvent e, @Header(name = "userId", required = false) String userId,
			@Header(name = "managerId", required = false) String managerId,
			@Header(name = "adminId", required = false) String adminId) {
		System.out.println("Got notification event: " + e);
		store.add(e);
	}
}
