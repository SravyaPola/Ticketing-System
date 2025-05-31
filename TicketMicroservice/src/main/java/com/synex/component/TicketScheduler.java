package com.synex.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.synex.domain.Action;
import com.synex.domain.Status;
import com.synex.domain.Ticket;
import com.synex.domain.TicketEvent;
import com.synex.domain.TicketHistory;
import com.synex.repository.TicketHistoryRepository;
import com.synex.repository.TicketRepository;
import com.synex.service.JmsProducer;

@Component
public class TicketScheduler {

	private static final Logger log = LoggerFactory.getLogger(TicketScheduler.class);

	private final TicketRepository ticketRepo;
	private final TicketHistoryRepository historyRepository;
	private final JmsProducer producer;

	public TicketScheduler(TicketRepository ticketRepo, JmsProducer producer,
			TicketHistoryRepository historyRepository) {
		this.ticketRepo = ticketRepo;
		this.producer = producer;
		this.historyRepository = historyRepository;
	}

	@Scheduled(cron = "0 0 12 * * *")
	public void notifyLongPendingTickets() {
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
		List<Ticket> stalePendings = ticketRepo.findByStatusAndCreationDateBefore(Status.OPEN, sevenDaysAgo);

		if (!stalePendings.isEmpty()) {
			log.info("notifyLongPendingTickets");
			for (Ticket ticket : stalePendings) {
				TicketEvent eventForManager = new TicketEvent();
				eventForManager.setReceivedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				eventForManager.setStatus(Status.OPEN.toString());
				eventForManager.setTicketId(ticket.getId().toString());
				eventForManager.setManagerId(ticket.getManagerId().toString());
				eventForManager.setMessage("Found that ticket with id : " + ticket.getId()
						+ " is pending_for_approval for > 7 days which was created by " + ticket.getCreatedBy()
						+ ". Please approve it.");
				eventForManager.setAutoTrigger(true);
				producer.send(eventForManager);
			}
		} else {
			System.out.println("No Pending tickets");
		}
	}

	@Scheduled(cron = "0 0 12 * * *")
	public void autoCloseResolvedTickets() {
		LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
		List<Ticket> staleResolved = ticketRepo.findByStatusAndUpdationDateBefore(Status.RESOLVED, fiveDaysAgo);

		if (!staleResolved.isEmpty()) {
			log.info("notifyAutoCloseResolvedTickets");
			for (Ticket ticket : staleResolved) {
				TicketEvent eventForUser = new TicketEvent();
				eventForUser.setEmployeeId(ticket.getCreatedBy());
				eventForUser.setMessage(
						"Your ticket " + ticket.getId() + " has been auto-closed after 5 days in RESOLVED status.");
				eventForUser.setTicketId(ticket.getId().toString());
				eventForUser.setStatus("CLOSED");
				eventForUser.setAutoTrigger(true);
				String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
				eventForUser.setReceivedAt(now);
				producer.send(eventForUser);
				ticket.setStatus(Status.CLOSED);
				ticketRepo.save(ticket);
				TicketHistory history = new TicketHistory();
				history.setTicket(ticket);
				history.setAction(Action.CLOSED);
				history.setActionBy("Auto-triggers");
				history.setComments("Ticket" + ticket.getId() + " was auto-closed");
				historyRepository.save(history);
			}
		}
	}
}