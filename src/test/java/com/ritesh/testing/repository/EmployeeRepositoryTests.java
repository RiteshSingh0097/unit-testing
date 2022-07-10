package com.ritesh.testing.repository;

import com.ritesh.testing.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

  @Autowired private EmployeeRepository employeeRepository;

  private Employee employee;

  @BeforeEach
  public void setup() {
    employee =
        Employee.builder()
            .firstName("Ritesh")
            .lastName("Singh")
            .email("riteshsingh893@gmail.com")
            .build();
    employeeRepository.save(employee);
  }

  // Junit test for Employee operations
  @DisplayName("Employee operations")
  @Test
  void givenEmployeeObject_whenSaved_thenReturnSavedEmployee() {

    // then - verify the output
    assertThat(employee).isNotNull();
    assertThat(employee.getId()).isPositive();
  }

  // Junit test for get All employee operation
  @DisplayName("Get all employees operation")
  @Test
  void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {

    // given - precondition or setup
    Employee employee1 =
        Employee.builder()
            .firstName("Deep")
            .lastName("Singh")
            .email("deep.singh@gmail.com")
            .build();

    employeeRepository.save(employee1);

    // when - action or the behavior that we are going to test
    List<Employee> employees = employeeRepository.findAll();

    // then - verify the output
    assertThat(employees).isNotNull().hasSize(2);
  }

  // Junit test for employee by id operation
  @DisplayName("Get employee by id operation")
  @Test
  void givenEmployeeObject_whenSave_thenFindEmployeeById() {
    // when - action or the behavior that we are going to test
    Employee employeeDb = employeeRepository.findById(employee.getId()).orElse(null);

    // then - verify the output
    assertThat(employeeDb).isNotNull();
  }

  // JUnit test for employee by email operation
  @DisplayName("Get employee by employee's email")
  @Test
  void givenEmployeeObject_whenSave_thenFindEmployeeByEmail() {
    // then - verify the output
    Employee employeeDb = employeeRepository.findByEmail(employee.getEmail()).orElse(null);
    assertThat(employeeDb).isNotNull();
  }

  // JUnit test for update employee operation
  @DisplayName("Update employee operation")
  @Test
  void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
    // when - action or the behavior that we are going to test
    Employee savedEmployee = employeeRepository.findById(employee.getId()).orElse(null);

    assertThat(savedEmployee).isNotNull();
    savedEmployee.setEmail("rit@gmail.com");
    savedEmployee.setFirstName("Ram");

    employeeRepository.save(savedEmployee);

    // then - verify the output
    assertThat(savedEmployee.getEmail()).isEqualTo("rit@gmail.com");
    assertThat(savedEmployee.getFirstName()).isEqualTo("Ram");
  }

  // Junit test for delete employee operation
  @DisplayName("Delete employee operation")
  @Test
  void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
    // when - action or the behavior that we are going to test
    employeeRepository.delete(employee);

    Employee deleteEmployee = employeeRepository.findById(employee.getId()).orElse(null);

    assertThat(deleteEmployee).isNull();
  }

  //JUnit test for custom query using JPQL with index
  @DisplayName("Custom query using JPQL with index")
  @Test
  void givenFirstNameAndLastName_whenFindByJPQL_thenEmployeeObject(){
    // when - action or the behavior that we are going to test
    Employee employeeFromDb = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());

    //then - verify the output
    assertThat(employeeFromDb).isNotNull();
  }

  //JUnit test for custom query using JPQL with named params
  @DisplayName("Custom query using JPQL with named params")
  @Test
  void givenFirstNameAndLastName_whenFindByNamedParamsQuery_thenReturnEmployeeObject(){
    // when - action or the behavior that we are going to test
    Employee employeeFromDb = employeeRepository.findByCustomQueryWithNamedParams(employee.getFirstName(), employee.getLastName());

    //then - verify the output
    assertThat(employeeFromDb).isNotNull();
  }

  //JUnit test for custom query using native query with index
  @DisplayName("Custom query using native query with index")
  @Test
  void givenFirstNameAndLastName_whenFindByNativeQuery_thenReturnEmployeeObject(){
    // when - action or the behavior that we are going to test
    Employee employeeFromDb = employeeRepository.findByFirstNameAndLastNameWithNativeQuery(employee.getFirstName(), employee.getLastName());

    //then - verify the output
    assertThat(employeeFromDb).isNotNull();
  }

  //JUnit test for custom query using native query with index
  @DisplayName("Custom query using native query with params")
  @Test
  void givenFirstNameAndLastName_whenFindByNativeQueryWithParams_thenReturnEmployeeObject(){
    // when - action or the behavior that we are going to test
    Employee employeeFromDb = employeeRepository.findByFirstNameAndLastNameWithNativeQueryParams(employee.getFirstName(), employee.getLastName());

    //then - verify the output
    assertThat(employeeFromDb).isNotNull();
  }
}
