package com.ritesh.testing.service;

import com.ritesh.testing.exception.ResourceNotFoundException;
import com.ritesh.testing.model.Employee;
import com.ritesh.testing.repository.EmployeeRepository;
import com.ritesh.testing.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmployeeServiceImplTests {

  private EmployeeService employeeService;
  private EmployeeRepository employeeRepository;
  private Employee employee;

  @BeforeEach
  public void setup() {
    employeeRepository = Mockito.mock(EmployeeRepository.class);
    employeeService = new EmployeeServiceImpl(employeeRepository);

    employee =
        Employee.builder()
            .id(1)
            .firstName("Ritesh")
            .lastName("Singh")
            .email("riteshsingh893@gmail.com")
            .build();
  }

  // JUnit test for save employee object
  @DisplayName("Save employee object")
  @Test
  void givenEmployeeObject_whenSave_thenReturnEmployee() {
    // given - precondition or setup
    // stubbing
    BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
        .willReturn(Optional.empty());
    // stubbing
    BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

    // when - action or the behavior that we are going to test
    Employee saveEmployee = employeeService.saveEmployee(employee);

    // then - verify the output
    assertThat(saveEmployee).isNotNull();
    assertThat(saveEmployee.getId()).isEqualTo(1);
  }

  // JUnit test for to save employee object
  @DisplayName("Save employee object which throws exception")
  @Test
  void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
    // given - precondition or setup
    BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
        .willReturn(Optional.of(employee));

    // then - verify the output
    assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
  }

  // Junit test for to get all employee method
  @DisplayName("Get all employees")
  @Test
  void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {

    // Given - precondition or setup
    BDDMockito.given(employeeRepository.findAll())
        .willReturn(
            List.of(
                employee,
                Employee.builder()
                    .id(2)
                    .firstName("Deep")
                    .lastName("Singh")
                    .email("deep.dingh@gmail.com")
                    .build()));

    // when - action or the behavior that we are going to test
    List<Employee> employeeList = employeeService.getAllEmployees();

    // then - verify the output
    assertThat(employeeList).isNotNull().hasSize(2);
  }

  // Junit test for to get all employee method
  @DisplayName("Get all employees (negative scenario)")
  @Test
  void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
    // given - precondition or setup
    BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

    // when - action or the behavior that we are going to test
    List<Employee> employeeList = employeeService.getAllEmployees();

    // then - verify the output
    assertThat(employeeList).isEmpty();
  }

  // JUnit test for get employee by id
  @DisplayName("Get employee by id")
  @Test
  void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

    // given - precondition or setup
    BDDMockito.given(employeeRepository.findById(employee.getId()))
        .willReturn(Optional.of(employee));

    // when - action or the behavior that we are going to test
    Employee savedEmployee = employeeService.findEmployeeById(employee.getId());

    // then - verify the output
    assertThat(savedEmployee.getId()).isNotNull();
  }

  // JUnit test for update employee
  @DisplayName("Update employee")
  @Test
  void giveEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {
    // given - precondition or setup
    BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
        .willReturn(Optional.of(employee));

    BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
    employee.setFirstName("Ritesh893");

    // when - action or the behavior that we are going to test
    Employee updatedEmployee = employeeService.updateEmployee(employee);

    // then - verify the output
    assertThat(updatedEmployee).isNotNull();
    assertThat(updatedEmployee.getFirstName()).isEqualTo("Ritesh893");
  }

  // Junit test for update employee
  @DisplayName("Update employee")
  @Test
  void giveEmployeeObject_whenDeleteEmployee_thenThrowException() {
    // given - precondition or setup
    BDDMockito.willDoNothing().given(employeeRepository).deleteById(1);
    // when - action or the behavior that we are going to test
    employeeService.deleteEmployee(employee.getId());

    // then - verify the output
    verify(employeeRepository, times(1)).deleteById(employee.getId());
  }
}
