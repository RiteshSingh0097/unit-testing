package com.ritesh.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritesh.testing.exception.ResourceNotFoundException;
import com.ritesh.testing.model.Employee;
import com.ritesh.testing.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

  Employee employee;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;
  @MockBean private EmployeeService employeeService;

  @BeforeEach
  public void setup() {
    employee =
        Employee.builder()
            .id(1)
            .firstName("Ritesh")
            .lastName("Singh")
            .email("riteshsingh893@gmail.com")
            .build();
  }

  // Junit test for createEmployee Rest Api
  @DisplayName("Create employee Rest Api")
  @Test
  void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
    // given - precondition or setup
    given(employeeService.saveEmployee(any(Employee.class)))
        .willAnswer((invocation -> invocation.getArgument(0)));

    // when - action or the behavior that we are going to test
    ResultActions response =
        mockMvc.perform(
            post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

    // then - verify the output
    response
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
        .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
        .andExpect(jsonPath("$.email", is(employee.getEmail())));
  }

  // Junit test for getAllEmployees Rest API
  @DisplayName("GetAllEmployee Rest API")
  @Test
  void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
    // given - precondition or setup
    List<Employee> employeeList =
        List.of(
            employee,
            Employee.builder()
                .id(2)
                .firstName("Ritesh")
                .lastName("Singh")
                .email("riteshsingh893@gmail.com")
                .build());

    given(employeeService.getAllEmployees()).willReturn(employeeList);

    // when - action or the behavior that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employee"));

    // then - verify the output
    response
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", equalTo(employeeList.size())));
  }

  // Positive scenario
  // Junit test for GET employee by id Rest API
  @DisplayName("Get employee by id Rest Api (positive)")
  @Test
  void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
    // given - precondition or setup
    Integer employeeId = 1;
    given(employeeService.findEmployeeById(employeeId)).willReturn(employee);

    // when - action or behavior that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));

    // then - verify the output
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.firstName", is(employee.getFirstName())));
  }

  // Negative scenario
  // Junit test for GET employee by id Rest API
  @DisplayName("Get employee by id Rest Api (negative)")
  @Test
  void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
    // given - precondition or setup
    Integer employeeId = 1;
    given(employeeService.findEmployeeById(employeeId)).willThrow(ResourceNotFoundException.class);

    // when - action or behavior that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));

    // then - verify the output
    response.andExpect(status().isNotFound()).andDo(print());
  }

  // Positive scenario
  // Junit test for update employee Rest API
  @DisplayName("Update employee Rest Api (positive)")
  @Test
  void givenUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
    // given - precondition or setup
    Integer employeeId = 1;
    Employee updatedEmployee =
        Employee.builder()
            .firstName("Ritesh")
            .lastName("Singhaniya")
            .email("riteshsingh893@gmail.com")
            .build();

    given(employeeService.findEmployeeById(employeeId)).willReturn(employee);
    given(employeeService.updateEmployee(any(Employee.class)))
        .willAnswer(invocation -> invocation.getArgument(0));

    // when - action or behavior that we are going to test
    ResultActions response =
        mockMvc.perform(
            put("/api/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

    // then - verify the output
    response
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())));
  }

  // Negative scenario
  // Junit test for update employee Rest API
  @DisplayName("Update employee Rest Api (negative)")
  @Test
  void givenUpdateEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
    // given - precondition or setup
    Integer employeeId = 1;
    Employee updatedEmployee =
        Employee.builder()
            .firstName("Ritesh")
            .lastName("Singhaniya")
            .email("riteshsingh893@gmail.com")
            .build();

    given(employeeService.findEmployeeById(employeeId)).willThrow(ResourceNotFoundException.class);
    given(employeeService.updateEmployee(any(Employee.class)))
        .willAnswer(invocation -> invocation.getArgument(0));

    // when - action or behavior that we are going to test
    ResultActions response =
        mockMvc.perform(
            put("/api/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

    // then - verify the output
    response.andExpect(status().isNotFound()).andDo(print());
  }

  // Junit test for delete employee Rest API
  @DisplayName("Delete employee Rest Api (negative)")
  @Test
  void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
    // given - precondition or setup
    Integer employeeId = 1;
    willDoNothing().given(employeeService).deleteEmployee(employeeId);

    // when - action or behavior that we are going to test
    ResultActions response = mockMvc.perform(delete("/api/employee/{id}", employeeId));

    // then - verify the output
    response.andExpect(status().isOk()).andDo(print());
  }
}
