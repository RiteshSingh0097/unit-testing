package com.ritesh.testing.controller;

import com.ritesh.testing.exception.ResourceNotFoundException;
import com.ritesh.testing.model.Employee;
import com.ritesh.testing.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Employee createEmployee(@RequestBody Employee employee) {
    return employeeService.saveEmployee(employee);
  }

  @GetMapping
  public List<Employee> getEmployees() {
    return employeeService.getAllEmployees();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id) {
    try {
      Employee employee = employeeService.findEmployeeById(id);
      return new ResponseEntity<>(employee, HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(
      @PathVariable("id") Integer employeeId, @RequestBody Employee employee) {
    try {
      Employee saveEmployee = employeeService.findEmployeeById(employeeId);
      employee.setId(employeeId);
      employeeService.updateEmployee(employee);
      return new ResponseEntity<>(employee, HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer employeeId) {
    employeeService.deleteEmployee(employeeId);
    return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
  }
}
