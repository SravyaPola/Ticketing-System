package com.synex.service;

import java.util.Map;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.synex.domain.Notification;
import com.synex.dto.NotificationRecord;
import com.synex.repository.NotificationRepository;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {
	private final NotificationRepository repo;
	private final JmsTemplate jms;
	private final JavaMailSender mailSender;
	private final PdfGenerator pdfGen;
	private final SimpMessagingTemplate ws;

	public NotificationService(NotificationRepository repo, JmsTemplate jms, JavaMailSender mailSender,
			PdfGenerator pdfGen, SimpMessagingTemplate ws) {
		this.repo = repo;
		this.jms = jms;
		this.mailSender = mailSender;
		this.pdfGen = pdfGen;
		this.ws = ws;
	}

	public void createNotification(Long userId, String message, String url) {
		Notification n = new Notification();
		n.setUserId(userId);
		n.setMessage(message);
		repo.save(n);
		jms.convertAndSend("app.notifications", Map.of("id", n.getId(), "userId", userId, "message", message, "url",
				url, "createdAt", n.getCreatedAt().toString()));
		ws.convertAndSend("/topic/notify/" + userId, n);
	}

	public void sendEmailWithPdf(Long userId, String email, NotificationRecord rec) throws Exception {
		byte[] pdf = pdfGen.generateResolutionReport(rec);
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo(email);
		helper.setSubject("Your Ticket #" + rec.getTicketId() + " Resolved");
		helper.setText("Find the attached resolution report.");
		helper.addAttachment("resolution.pdf", new ByteArrayResource(pdf), "application/pdf");
		mailSender.send(msg);
	}

}
