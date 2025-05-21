package com.synex.service;

import org.springframework.stereotype.Service;

import com.synex.domain.TicketEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TicketEventStore {

	private final List<TicketEvent> unread = new CopyOnWriteArrayList<>();
    private final List<TicketEvent> read   = new CopyOnWriteArrayList<>();

    /** Called from your @JmsListener */
    public void add(TicketEvent e) {
        unread.add(e);
    }

    /** For your notifications page: ALL events */
    public List<TicketEvent> findAll() {
        List<TicketEvent> all = new CopyOnWriteArrayList<>(read);
        all.addAll(unread);
        return all;
    }

    /** How many are still unread */
    public int getUnreadCount() {
        return unread.size();
    }

    /** After you show /user/notifications, mark them read */
    public void markAllRead() {
        read.addAll(unread);
        unread.clear();
    }
}
