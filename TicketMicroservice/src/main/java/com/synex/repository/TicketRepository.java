package com.synex.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synex.domain.Status;
import com.synex.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByCreatedBy(String name);

	List<Ticket> findByStatusAndAssignee(Status status, String name);

	List<Ticket> findByManagerIdAndStatusIn(Long id, List<Status> statuses);
	
	List<Ticket> findByStatusAndCreationDateBefore(
		    Status status, LocalDate cutoff);
}
