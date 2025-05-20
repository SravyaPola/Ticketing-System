package com.synex.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.synex.domain.Notification;
import com.synex.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationRepository repo;

	public NotificationController(NotificationRepository repo) {
		this.repo = repo;
	}

	@GetMapping("/count")
	public long count(@RequestParam Long userId) {
		return repo.countByUserIdAndReadFalse(userId);
	}

	@GetMapping
	public List<Notification> list(@RequestParam Long userId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		return repo.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
	}

	@PostMapping("/{id}/read")
	public void markRead(@PathVariable Long id, @RequestParam Long userId) {
		Notification n = repo.findById(id).filter(x -> x.getUserId().equals(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		n.setRead(true);
		repo.save(n);
	}
}