package com.synex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.synex.domain.TicketEvent;

public interface NotificationRepository extends JpaRepository<TicketEvent, Integer>{

}
