package com.synex.component;

import org.springframework.jms.annotation.JmsListener;
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
	public void onNotification(TicketEvent notif) {
		System.out.println("Received persisted notification: " + notif);
		store.add(notif);
	}
}
