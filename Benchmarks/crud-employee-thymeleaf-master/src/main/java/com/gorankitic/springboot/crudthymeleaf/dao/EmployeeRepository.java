package com.gorankitic.springboot.crudthymeleaf.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorankitic.springboot.crudthymeleaf.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	List<Employee> findAllByOrderByLastNameAsc();

}
