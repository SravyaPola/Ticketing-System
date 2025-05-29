package com.synex.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import com.synex.domain.TicketEvent;

@Service
public class TicketEventStore {

	private final ConcurrentHashMap<String, CopyOnWriteArrayList<TicketEvent>> unreadMap = new ConcurrentHashMap<>();

	public void add(TicketEvent e) {
		Stream.of(e.getEmployeeId(), e.getManagerId(), e.getAdminId()).filter(Objects::nonNull)
				.forEach(userId -> unreadMap.computeIfAbsent(userId, id -> new CopyOnWriteArrayList<>()).add(e));
	}

	public List<TicketEvent> findUnreadFor(String userId) {
		return Collections.unmodifiableList(unreadMap.getOrDefault(userId, new CopyOnWriteArrayList<>()));
	}

	public int getUnreadCountFor(String userId) {
		return findUnreadFor(userId).size();
	}

	public void markAllReadFor(String userId) {
		unreadMap.remove(userId);
	}
}
