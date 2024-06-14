package com.ninjaone.dundie_awards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void shouldReturnAllEmployees() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize((int) employeeRepository.count())));
    }

    @Test
    public void shouldCreateNewEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void shouldReturnEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee = employeeRepository.save(employee);

        mockMvc.perform(get("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("OldName");
        employee.setLastName("LastName");
        employee = employeeRepository.save(employee);

        employee.setFirstName("NewName");

        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("NewName")));

        Optional<Employee> updatedEmployee = employeeRepository.findById(employee.getId());
        assertThat(updatedEmployee.isPresent()).isTrue();
        assertThat(updatedEmployee.get().getFirstName()).isEqualTo("NewName");
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("DeleteName");
        employee.setLastName("DeleteLastName");
        employee = employeeRepository.save(employee);

        mockMvc.perform(delete("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));

        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());
        assertThat(deletedEmployee.isPresent()).isFalse();
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
