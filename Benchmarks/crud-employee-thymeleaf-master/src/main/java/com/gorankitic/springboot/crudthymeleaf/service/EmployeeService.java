package com.gorankitic.springboot.crudthymeleaf.service;

import java.util.List;

import com.gorankitic.springboot.crudthymeleaf.entity.Employee;

public interface EmployeeService {

	public List<Employee> findAll();
	
	public Employee findById(int id);
	
	public void save(Employee employee);
	
	public void deleteById(int id);
	
}
