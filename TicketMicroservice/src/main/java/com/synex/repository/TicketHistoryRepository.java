package com.synex.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.synex.domain.TicketHistory;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {

	@Query("SELECT th FROM TicketHistory th WHERE th.ticket.id = :ticketId")
	List<TicketHistory> findByTicketId(@Param("ticketId") Long ticketId);
}
