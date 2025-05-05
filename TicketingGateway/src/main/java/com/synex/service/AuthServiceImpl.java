package com.synex.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.synex.domain.Role;
import com.synex.domain.Employee;
import com.synex.model.LoginDto;
import com.synex.model.RegisterDto;
import com.synex.repository.EmployeeRepository;
import com.synex.repository.RoleRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public String register(RegisterDto registerDto) {
		if (employeeRepository.existsByName(registerDto.getName())) {
			return "User name is already exists!.";
		} else if (employeeRepository.existsByEmail(registerDto.getEmail())) {
			return "Email is already exists!.";
		} else {

			Employee employee = new Employee();
			employee.setName(registerDto.getName());
			employee.setEmail(registerDto.getEmail());
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			employee.setPassword(passwordEncoder.encode(registerDto.getPassword()));
			List<Role> roles = new ArrayList<Role>();
			if (registerDto.getRoles() != null) {
				for (String str : registerDto.getRoles()) {
					roles.add(roleRepository.findByName(str));
				}
			}
			employee.setRoles(roles);
			employee.setDepartment(registerDto.getDepartment());
			employee.setProject(registerDto.getProject());
			employee.setManagerId(registerDto.getManagerId());
			employeeRepository.save(employee);

			return "success";
		}
	}

	@Override
	public String login(LoginDto loginDto) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "Login Successful";
	}

	@Override
	public List<Role> getEmployeeRolesByName(String username) {
		Employee employee = employeeRepository.findByName(username);
		return employee.getRoles();
	}
}
