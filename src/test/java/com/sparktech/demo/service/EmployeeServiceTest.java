package com.sparktech.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparktech.demo.dto.EmployeeRequest;
import com.sparktech.demo.dto.EmployeeResponse;
import com.sparktech.demo.exception.DuplicateResourceException;
import com.sparktech.demo.exception.ResourceNotFoundException;
import com.sparktech.demo.model.Employee;
import com.sparktech.demo.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private EmployeeRequest request() {
        return new EmployeeRequest("Rohan", "rohan@sparktech.com", "Engineering", new BigDecimal("85000"));
    }

    @Test
    void getById_whenMissing_throwsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void create_whenEmailExists_throwsDuplicate() {
        when(repository.existsByEmail("rohan@sparktech.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.create(request()));
        verify(repository, never()).save(any(Employee.class));
    }

    @Test
    void create_savesAndReturnsEmployee() {
        when(repository.existsByEmail("rohan@sparktech.com")).thenReturn(false);
        when(repository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse response = service.create(request());

        assertEquals("Rohan", response.name());
        assertEquals("Engineering", response.department());
    }

    @Test
    void findAll_withDepartment_filtersByDepartment() {
        Employee employee = new Employee("Anita", "anita@sparktech.com", "HR", new BigDecimal("60000"));
        when(repository.findByDepartmentIgnoreCase("hr")).thenReturn(List.of(employee));

        List<EmployeeResponse> result = service.findAll("hr");

        assertEquals(1, result.size());
        assertEquals("Anita", result.get(0).name());
    }
}
