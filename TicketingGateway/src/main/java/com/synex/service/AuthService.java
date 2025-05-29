package com.synex.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.synex.domain.Role;
import com.synex.model.LoginDto;
import com.synex.model.RegisterDto;

@Service
public interface AuthService {

	String register(RegisterDto registerDto) throws Exception;

	String login(LoginDto loginDto);

	List<Role> getEmployeeRolesByName(String username);

	String getEmail(String username);
}