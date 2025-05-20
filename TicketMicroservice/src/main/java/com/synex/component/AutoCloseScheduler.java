package com.synex.component;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.synex.domain.Status;
import com.synex.domain.Ticket;
import com.synex.repository.TicketRepository;
import com.synex.service.NotificationEventPublisher;

import jakarta.transaction.Transactional;

@Component
public class AutoCloseScheduler {

	private final TicketRepository ticketRepo;
	private final NotificationEventPublisher eventPublisher;

	public AutoCloseScheduler(TicketRepository ticketRepo, NotificationEventPublisher eventPublisher) {
		this.ticketRepo = ticketRepo;
		this.eventPublisher = eventPublisher;
	}

	/**
     * Runs every day at 1:00 AM.
     * Finds tickets in RESOLVED state with resolvedDate ≤ now − 5 days,
     * sets status to CLOSED, saves, and emits a JMS event.
     */
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void autoCloseResolvedTickets() {
        LocalDate cutoff = LocalDate.now().minusDays(5);
        List<Ticket> toClose = ticketRepo.findByStatusAndActionDate(
            Status.RESOLVED, cutoff);
        
        for (Ticket ticket : toClose) {
            ticket.setStatus(Status.CLOSED);
            ticketRepo.save(ticket);

            // publish an event so Notification Service can pick up & email
//            eventPublisher.publishTicketEvent(
//                ticket.getId(),
//                ticket.getManagerId()
//                ticket.getAssignee(),
//                ticket.getStatus(),
//                LocalDateTime.now(),
//                "Auto-closed after 5 days"
//            );
        }
    }
}