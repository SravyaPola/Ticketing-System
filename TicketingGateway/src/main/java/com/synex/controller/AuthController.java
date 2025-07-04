package com.synex.controller;

import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.synex.domain.Employee;
import com.synex.domain.Role;
import com.synex.model.LoginDto;
import com.synex.model.RegisterDto;
import com.synex.model.RoleForm;
import com.synex.repository.EmployeeRepository;
import com.synex.service.AuthService;
import com.synex.service.TicketEventStore;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private EmployeeRepository employeeRepository;

	private final TicketEventStore store;

	public AuthController(TicketEventStore store) {
		this.store = store;
	}

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		List<Employee> managers = employeeRepository.findByRoles_Name("MANAGER");
		model.addAttribute("managers", managers);
		model.addAttribute("registerDto", new RegisterDto());
		return "register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("registerDto") RegisterDto registerDto, Model model) throws Exception {
		String response = authService.register(registerDto);
		if (response == "success") {
			model.addAttribute("success", response);
			model.addAttribute("loginDto", new LoginDto());
			return "login";
		} else {
			model.addAttribute("error", response);
			return "register";
		}
	}

	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("errorMsg", "Invalid username or password.");
		}
		model.addAttribute("loginDto", new LoginDto());
		return "login";
	}

	@GetMapping("/home")
	public String homePage(Model model, Principal principal) {
		model.addAttribute("roleForm", new RoleForm());
		model.addAttribute("username", principal.getName());
		return "home";
	}

	@PostMapping("/home")
	public String handleRoleChoice(@ModelAttribute RoleForm roleForm, Principal principal, HttpSession session,
			Model model) {

		String username = principal.getName();
		String selectedRole = roleForm.getRoleChoice();
		List<Role> actualRoles = authService.getEmployeeRolesByName(username);
		boolean hasRole = actualRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(selectedRole));
		if (hasRole) {
			session.setAttribute("activeRole", selectedRole);
			if ("ADMIN".equalsIgnoreCase(selectedRole))
				return "redirect:/admin/home";
			if ("USER".equalsIgnoreCase(selectedRole))
				return "redirect:/user/home";
			if ("MANAGER".equalsIgnoreCase(selectedRole))
				return "redirect:/manager/home";
		}
		model.addAttribute("error", "You are not authorized as " + selectedRole);
		model.addAttribute("username", username);
		return "home";
	}

	@GetMapping("/user/home")
	public String showUserHome(Model model, Principal principal, HttpSession session) {
		String username = principal.getName();
		model.addAttribute("name", username);
		model.addAttribute("activeRole", session.getAttribute("activeRole"));
		model.addAttribute("unreadCount", store.getUnreadCountFor(username));
		return "user-dashboard";
	}

	@GetMapping("/admin/home")
	public String showAdminHome(Model model, Principal principal, HttpSession session) {
		String username = principal.getName();
		model.addAttribute("name", username);
		model.addAttribute("activeRole", session.getAttribute("activeRole"));
		model.addAttribute("unreadCount", store.getUnreadCountFor(username));
		return "admin-dashboard";
	}

	@GetMapping("/manager/home")
	public String showManagerHome(Model model, Principal principal, HttpSession session) {
		String username = principal.getName();
		model.addAttribute("name", username);
		model.addAttribute("activeRole", session.getAttribute("activeRole"));
		Employee employee = employeeRepository.findByName(username);
		model.addAttribute("unreadCount", store.getUnreadCountFor(employee.getId().toString()));
		return "manager-dashboard";
	}

	@PostMapping("/logout-success")
	public String logoutPage() {
		return "logout";
	}

	@GetMapping("/get-email-details/{username}")
	public ResponseEntity<String> getEmployeeEmail(@PathVariable String username) {
		String email;
		if (employeeRepository.existsByName(username)) {
			Employee emp = employeeRepository.findByName(username);
			email = emp.getEmail();
		} else {
			Long id;
			try {
				id = Long.parseLong(username);
			} catch (NumberFormatException e) {
				return ResponseEntity.badRequest().body("Invalid employee identifier: must be name or numeric ID");
			}
			Optional<Employee> optEmp = employeeRepository.findById(id);
			if (!optEmp.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			email = optEmp.get().getEmail();
		}
		if (email == null || email.trim().isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(email);
	}
}
