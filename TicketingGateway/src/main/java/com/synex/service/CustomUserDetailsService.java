package com.synex.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import com.synex.domain.Employee;
import com.synex.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRepository.findByName(username);
		if (employee == null) {
			throw new UsernameNotFoundException("User not exists by Username or Email");
		}

		Set<GrantedAuthority> authorities = employee.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toSet());
		System.out.println(employee.getName());
		return new org.springframework.security.core.userdetails.User(username, employee.getPassword(), authorities);
	}

}
