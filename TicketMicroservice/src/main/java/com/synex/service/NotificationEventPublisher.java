package com.synex.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.synex.domain.TicketEvent;

import java.time.LocalDateTime;

@Service
public class NotificationEventPublisher {

    private final JmsTemplate jms;

    public NotificationEventPublisher(JmsTemplate jms) {
        this.jms = jms;
    }

    /**
     * Publish a TicketEvent to the "ticket.events" destination.
     *
     * @param ticketId   the ticket’s database ID
     * @param userId     the owner/requester’s user ID
     * @param userEmail  the owner/requester’s email address
     * @param type       the event type (CREATED, RESOLVED, CLOSED, etc.)
     * @param eventTime  when the event occurred
     * @param details    any free-form notes or resolution text
     */
    public void publishTicketEvent(Long ticketId,
                                   Long userId,
                                   String userEmail,
                                   TicketEvent.Type type,
                                   LocalDateTime eventTime,
                                   String details) {
        TicketEvent evt = new TicketEvent(
            ticketId,
            userId,
            userEmail,
            type,
            eventTime,
            details
        );
        jms.convertAndSend("ticket.events", evt);
    }
}