package com.synex.model;

import java.util.List;

public class RegisterDto {

	private String name;
	private String email;
	private String password;
	private List<String> roles;
	private String department;
	private String project;
	private Long managerId;

	public RegisterDto() {
	}

	public RegisterDto(String name, String email, String password, List<String> roles, String department,
			String project, Long managerId) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.department = department;
		this.project = project;
		this.managerId = managerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
}