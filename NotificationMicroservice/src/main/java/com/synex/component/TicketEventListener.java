package com.synex.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import com.synex.domain.TicketEvent;
import com.synex.repository.NotificationRepository;
import com.synex.service.EmailService;

@Component
public class TicketEventListener {
	private final JmsTemplate jms;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	EmailService emailService;

	public TicketEventListener(JmsTemplate jms) {
		this.jms = jms;
	}

	@JmsListener(destination = "ticket.queue", containerFactory = "myFactory")
	public void receive(TicketEvent event) {
		System.out.println("Received message: " + event);
		TicketEvent saved = notificationRepository.save(event);
		emailService.sendEmail(saved);
		if(event.isAutoTrigger()) {
			if(saved.getEmployeeId() != null) {
				emailService.sendToUser(saved);
			}
			if(saved.getManagerId() != null) {
				emailService.sendToManager(saved);
			}
		}
		if(event.getEmployeeId() != null && event.getStatus().equals("RESOLVED")) {
			System.out.println("called");
			emailService.sendTicketResolutionReport(event);
		}
		if(event.getEmployeeId() != null && event.getStatus().equals("CLOSED")){
			try {
				emailService.sendFeedbackRequestEmail(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jms.convertAndSend("notifications.queue", saved, msg -> {
			if (saved.getEmployeeId() != null) {
				msg.setStringProperty("employeeId", saved.getEmployeeId());
			}
			if (saved.getManagerId() != null) {
				msg.setStringProperty("managerId", saved.getManagerId());
			}
			if (saved.getAdminId() != null) {
				msg.setStringProperty("adminId", saved.getAdminId());
			}
			return msg;
		});
	}
}
