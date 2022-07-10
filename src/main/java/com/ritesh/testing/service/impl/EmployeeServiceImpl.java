package com.ritesh.testing.service.impl;

import com.ritesh.testing.exception.ResourceNotFoundException;
import com.ritesh.testing.model.Employee;
import com.ritesh.testing.repository.EmployeeRepository;
import com.ritesh.testing.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Employee saveEmployee(Employee employee) {
    employeeRepository
        .findByEmail(employee.getEmail())
        .ifPresentOrElse(
            employeeObject -> {
              throw new ResourceNotFoundException(
                  "Employee already exist with email :: " + employeeObject.getEmail());
            },
            () -> employeeRepository.save(employee));
    return employee;
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public Employee findEmployeeById(Integer id) {
    return employeeRepository
        .findById(id)
        .orElseThrow(
            () -> {
              throw new ResourceNotFoundException("Employee not found with id :: " + id);
            });
  }

  @Override
  public Employee updateEmployee(Employee employee) {
    employeeRepository
        .findByEmail(employee.getEmail())
        .ifPresentOrElse(
            employeeObject -> employeeRepository.save(employee),
            () -> {
              throw new ResourceNotFoundException(
                  "Employee not found for email :: " + employee.getEmail());
            });
    return employee;
  }

  @Override
  public void deleteEmployee(Integer id) {
    employeeRepository.deleteById(id);
  }
}
