package com.sparktech.demo.dto;

import com.sparktech.demo.model.Employee;
import java.math.BigDecimal;

public record EmployeeResponse(Long id, String name, String email, String department, BigDecimal salary) {

    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary());
    }
}
