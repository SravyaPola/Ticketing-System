package com.synex.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.synex.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

	long countByUserIdAndReadFalse(Long userId);
}
