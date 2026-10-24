package com.sparktech.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparktech.demo.dto.EmployeeRequest;
import com.sparktech.demo.dto.EmployeeResponse;
import com.sparktech.demo.exception.ResourceNotFoundException;
import com.sparktech.demo.service.EmployeeService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService service;

    @Test
    void getAll_returnsEmployees() throws Exception {
        when(service.findAll(null)).thenReturn(List.of(
                new EmployeeResponse(1L, "Rohan", "rohan@sparktech.com", "Engineering", new BigDecimal("85000"))));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rohan"));
    }

    @Test
    void getById_whenMissing_returns404() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Employee not found with id 99"));

        mockMvc.perform(get("/api/v1/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id 99"));
    }

    @Test
    void create_withValidBody_returns201() throws Exception {
        EmployeeRequest request =
                new EmployeeRequest("Rohan", "rohan@sparktech.com", "Engineering", new BigDecimal("85000"));
        when(service.create(any(EmployeeRequest.class))).thenReturn(
                new EmployeeResponse(1L, "Rohan", "rohan@sparktech.com", "Engineering", new BigDecimal("85000")));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_withInvalidBody_returns400() throws Exception {
        String invalidJson = "{\"name\":\"\",\"email\":\"not-an-email\",\"department\":\"HR\",\"salary\":-5}";

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
