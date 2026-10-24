package com.sparktech.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparktech.demo.model.Employee;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void existsByEmail_findsSavedEmployee() {
        repository.save(new Employee("Anita", "anita@sparktech.com", "HR", new BigDecimal("60000")));

        assertTrue(repository.existsByEmail("anita@sparktech.com"));
        assertFalse(repository.existsByEmail("nobody@sparktech.com"));
    }

    @Test
    void findByDepartmentIgnoreCase_ignoresCase() {
        repository.save(new Employee("Vikram", "vikram@sparktech.com", "Finance", new BigDecimal("72000")));
        repository.save(new Employee("Anita", "anita@sparktech.com", "HR", new BigDecimal("60000")));

        List<Employee> result = repository.findByDepartmentIgnoreCase("finance");

        assertEquals(1, result.size());
        assertEquals("Vikram", result.get(0).getName());
    }
}
