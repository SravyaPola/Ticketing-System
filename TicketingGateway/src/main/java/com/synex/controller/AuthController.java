package com.synex.controller;

import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.synex.domain.Role;
import com.synex.model.LoginDto;
import com.synex.model.RegisterDto;
import com.synex.model.RoleForm;
import com.synex.service.AuthService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

	@Autowired
	private AuthService authService;

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
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
		model.addAttribute("name", principal.getName());
		model.addAttribute("activeRole", session.getAttribute("activeRole"));
		return "user-dashboard";
	}

	@GetMapping("/admin/home")
	public String showAdminHome(Model model, Principal principal, HttpSession session) {
		model.addAttribute("name", principal.getName());
		model.addAttribute("activeRole", session.getAttribute("activeRole"));
		return "admin-dashboard";
	}

	@GetMapping("/manager/home")
	public String showManagerHome(Model model, Principal principal, HttpSession session) {
		model.addAttribute("name", principal.getName());
		model.addAttribute("activeRole", session.getAttribute("activeRole"));
		return "manager-dashboard";
	}

	@PostMapping("/logout-success")
	public String logoutPage() {
		return "logout";
	}
}
