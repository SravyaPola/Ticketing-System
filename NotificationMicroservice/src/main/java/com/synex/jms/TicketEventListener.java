package com.synex.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.synex.dto.TicketEvent;
import com.synex.service.NotificationService;

@Component
public class TicketEventListener {

	private final NotificationService notifSvc;

	public TicketEventListener(NotificationService notifSvc) {
		this.notifSvc = notifSvc;
	}

	@JmsListener(destination = "ticket.events")
	public void onTicketEvent(TicketEvent evt) {

		notifSvc.createNotification(evt.getUserId(), "Ticket #" + evt.getTicketId() + " " + evt.getType(),
				"/tickets/" + evt.getTicketId());

		if (evt.getType() == TicketEvent.Type.RESOLVED) {
			try {
				notifSvc.sendEmailWithPdf(evt.getUserId(), evt.getUserEmail(), evt.toNotificationRecord());
			} catch (Exception e) {

			}
		}
	}
}