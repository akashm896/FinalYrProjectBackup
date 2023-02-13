package com.gorankitic.springboot.crudthymeleaf.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gorankitic.springboot.crudthymeleaf.entity.Employee;
import com.gorankitic.springboot.crudthymeleaf.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {		//constructor injection
		this.employeeService = employeeService;
	}

	@GetMapping("/list")
	public String listEmployees(Model model) {
		
		List<Employee> employees = employeeService.findAll();
		model.addAttribute("employees", employees);
		return "employees/list-employees";
	}
	
	@GetMapping("/showFormAdd")
	public String showFormAdd(Model model) {
		
		//create a model attribute to bind form data
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "employees/employee-form";
	}
	
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {
		employeeService.save(employee);
		return "redirect:/employees/list";		//use redirect to prevent a duplicate submissions
	}
	
	@GetMapping("/showFormUpdate")
	public String showFormUpdate(@RequestParam("employeeId") int id, Model model) {
		
		//get employee form service
		Employee employee = employeeService.findById(id);
		//set employee as a model attribute to prepopulate the form
		model.addAttribute("employee", employee);
		//send over to form
		return "employees/employee-form";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") int id) {
		employeeService.deleteById(id);
		return "redirect:/employees/list";
	}

}