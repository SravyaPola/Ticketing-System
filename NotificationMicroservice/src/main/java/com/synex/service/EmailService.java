package com.synex.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.synex.component.EmailClientService;
import com.synex.component.PdfGenerator;
import com.synex.domain.TicketEvent;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	EmailClientService ecs;

	@Autowired
	private PdfGenerator pdfGenerator;

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(TicketEvent event) {
		String greeting;
		String to;
		if (event.getManagerId() != null) {
			greeting = "Manager";
			to = ecs.getEmailDetails(event.getManagerId());
		} else if (event.getAdminId() != null) {
			greeting = event.getAdminId();
			to = ecs.getEmailDetails(event.getAdminId());
		} else {
			greeting = event.getEmployeeId();
			to = ecs.getEmailDetails(event.getEmployeeId());
		}

		StringBuilder body = new StringBuilder();
		body.append("Hi ").append(greeting).append(",\n\n");
		body.append("Status: ").append(event.getStatus()).append("\n").append("Message: ").append(event.getMessage())
				.append("\n").append("Time: ")
				.append(event.getReceivedAt().formatted(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.append("\n\n");
		body.append("Thanks and Regards,").append("\n");
		body.append("SynergisticIT");

		SimpleMailMessage msg = new SimpleMailMessage();
		System.out.println(to);
		msg.setTo(to);
		msg.setSubject("Ticket Update: " + event.getStatus());
		msg.setText(body.toString());
		mailSender.send(msg);

	}

	public void sendToUser(TicketEvent event) {
		String to = ecs.getEmailDetails(event.getEmployeeId());
		String greeting = event.getEmployeeId();

		StringBuilder body = new StringBuilder();
		body.append("Hi ").append(greeting).append(",\n\n");
		body.append("Your ticket has been automatically closed.\n\n");
		body.append("Status: ").append(event.getStatus()).append("\n").append("Message: ").append(event.getMessage())
				.append("\n").append("Time: ").append(LocalDateTime.parse(event.getReceivedAt())
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.append("\n\n");
		body.append("If you have any questions, please let us know.\n\n");
		body.append("Thanks and Regards,").append("\n");
		body.append("SynergisticIT");

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject("Ticket Update: Auto-Closed");
		msg.setText(body.toString());
		mailSender.send(msg);
	}

	public void sendToManager(TicketEvent event) {
		String to = ecs.getEmailDetails(event.getManagerId());
		String greeting = "Manager";
		StringBuilder body = new StringBuilder();

		body.append("Hi ").append(greeting).append(",\n\n");
		body.append("The following ticket has been pending your approval for more than 7 days:\n\n");
		body.append("Ticket ID:     ").append(event.getTicketId()).append("\n");
		body.append("Status:        ").append(event.getStatus()).append("\n");
		body.append("Message:       ").append(event.getMessage()).append("\n");
		body.append("Detected At:   ").append(
				LocalDateTime.parse(event.getReceivedAt()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.append("\n\n");
		body.append("Please review and take the necessary action at your earliest convenience.\n\n");
		body.append("Thanks and Regards,").append("\n");
		body.append("SynergisticIT");
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject("Ticket Pending Approval: " + event.getTicketId());
		msg.setText(body.toString());
		mailSender.send(msg);

	}

	public void sendTicketResolutionReport(TicketEvent event) {
		try {

			byte[] pdfBytes = pdfGenerator.generatePdf(event);

			String fileName = "Resolution-" + event.getTicketId() + ".pdf";
			Path docsFolder = Paths.get(System.getProperty("user.home"), "Documents", "resolutions");
			Files.createDirectories(docsFolder);
			Path pdfPath = docsFolder.resolve(fileName);
			Files.write(pdfPath, pdfBytes);

			String managerName = event.getEmployeeId();
			String ticketId = event.getTicketId();
			String resolvedAt = LocalDateTime.parse(event.getReceivedAt())
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			StringBuilder body = new StringBuilder();
			body.append("Hi ").append(managerName).append(",\n\n");
			body.append("Your ticket ").append(ticketId).append(" has been resolved on ").append(resolvedAt)
					.append(".\n\n");
			body.append("Please find the attached resolution report for full details.\n\n");
			body.append("If you have any further questions, feel free to reach out.\n\n");
			body.append("Thanks and Regards,\n");
			body.append("SynergisticIT");
			String bodyText = body.toString();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(ecs.getEmailDetails(event.getEmployeeId()));
			helper.setSubject("Ticket Resolution Report: " + ticketId);
			helper.setText(bodyText, false);

			FileSystemResource fsr = new FileSystemResource(pdfPath.toFile());
			helper.addAttachment(fileName, fsr);

			mailSender.send(message);
			System.out.println("Sent resolution email with attachment: " + pdfPath);
		} catch (Exception ex) {
			throw new IllegalStateException("Failed to send email", ex);
		}
	}
	
	public void sendFeedbackRequestEmail(TicketEvent event) throws Exception {
        
        String feedbackEmail = "sravyapola2000@gmail.com";
        String subject = "Feedback for Ticket " + event.getTicketId();
        String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8);
        StringBuilder html = new StringBuilder();
        html.append("<html><body style=\"font-family:Helvetica,Arial,sans-serif;\">");
        html.append("<p>Hi,</p>");
        html.append("<p>We’d love your feedback on ticket <strong>").append(event.getTicketId()).append("</strong>. ");
        html.append("Please click a rating below to open your email app:</p>");
        html.append("<p style=\"font-size:1.2em;\">");
        for (int i = 1; i <= 5; i++) {
            String bodyParam = URLEncoder.encode(
                "Ticket: " + event.getTicketId() + "\nRating: " + i + "\nComments: ",
                StandardCharsets.UTF_8
            );
            String mailto = String.format(
                "mailto:%s?subject=%s&body=%s",
                feedbackEmail, encodedSubject, bodyParam
            );
            html.append(String.format(
                "<a href=\"%s\" style=\"margin:0 6px;text-decoration:none;\">%d</a>",
                mailto, i
            ));
        }
        html.append("</p>");
        html.append("<p>Or just hit “Reply” and type your comments.</p>");
        html.append("<p>Thanks and regards,<br/>SynergisticIT</p>");
        html.append("</body></html>");

        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setTo(ecs.getEmailDetails(event.getEmployeeId()));
        helper.setSubject("We Value Your Feedback for Ticket " + event.getTicketId());
        helper.setText(html.toString(), true);
        mailSender.send(msg);
    }

}
