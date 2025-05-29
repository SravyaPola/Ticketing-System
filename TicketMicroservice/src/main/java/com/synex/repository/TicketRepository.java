package com.synex.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.synex.domain.Status;
import com.synex.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByCreatedBy(String name);

	List<Ticket> findByAssigneeAndStatusIn(String assignee, List<Status> statuses);

	List<Ticket> findByManagerIdAndStatusIn(Long id, List<Status> statuses);

	List<Ticket> findByStatusAndCreationDateBefore(Status status, LocalDateTime sevenDaysAgo);

	List<Ticket> findByStatusAndUpdationDateBefore(Status status, LocalDateTime fiveDaysAgo);
}
