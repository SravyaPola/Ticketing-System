package com.synex.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.synex.domain.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByName(String name);

	Optional<Employee> findById(Long id);

	boolean existsByEmail(String email);

	boolean existsByName(String name);

	List<Employee> findByRoles_Name(String roleName);

}
