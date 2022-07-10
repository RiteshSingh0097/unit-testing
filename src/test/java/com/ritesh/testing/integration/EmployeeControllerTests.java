package com.ritesh.testing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritesh.testing.model.Employee;
import com.ritesh.testing.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerTests {

  Employee employee;
  @Autowired private MockMvc mockMvc;
  @Autowired private EmployeeRepository employeeRepository;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    employeeRepository.deleteAll();
    employee =
        Employee.builder()
            .id(1)
            .firstName("Ritesh")
            .lastName("Singh")
            .email("riteshsingh893@gmail.com")
            .build();
  }

  // JUnit test for createEmployee Rest Api
  @DisplayName("Create employee Rest Api")
  @Test
  void giveEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

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

  // JUnit test for getAllEmployees Rest Api
  @DisplayName("GetAllEmployees Rest Api")
  @Test
  void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {

    // given - precondition or setup
    List<Employee> employeeList =
        List.of(
            employee,
            Employee.builder()
                .id(2)
                .firstName("Deep")
                .lastName("Singh")
                .email("deep.singh@gmail.com")
                .build());

    employeeRepository.saveAll(employeeList);

    // when - action or the behavior that we are going to test
    ResultActions response = mockMvc.perform(get("/api/employee"));

    // then - verify the output
    response
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", equalTo(employeeList.size())));
  }
}
