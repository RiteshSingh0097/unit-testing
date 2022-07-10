package com.ritesh.testing.service;

import com.ritesh.testing.model.Employee;

import java.util.List;

public interface EmployeeService {

  Employee saveEmployee(Employee employee);

  List<Employee> getAllEmployees();

  Employee findEmployeeById(Integer id);

  Employee updateEmployee(Employee employee);

  void deleteEmployee(Integer id);
}
