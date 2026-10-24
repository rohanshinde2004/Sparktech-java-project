package com.sparktech.demo.service;

import com.sparktech.demo.dto.EmployeeRequest;
import com.sparktech.demo.dto.EmployeeResponse;
import com.sparktech.demo.exception.DuplicateResourceException;
import com.sparktech.demo.exception.ResourceNotFoundException;
import com.sparktech.demo.model.Employee;
import com.sparktech.demo.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll(String department) {
        List<Employee> employees = (department == null || department.isBlank())
                ? repository.findAll()
                : repository.findByDepartmentIgnoreCase(department);
        return employees.stream().map(EmployeeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        return EmployeeResponse.from(findOrThrow(id));
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Employee with email " + request.email() + " already exists");
        }
        Employee employee = new Employee(
                request.name(), request.email(), request.department(), request.salary());
        return EmployeeResponse.from(repository.save(employee));
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = findOrThrow(id);
        boolean emailChanged = !employee.getEmail().equalsIgnoreCase(request.email());
        if (emailChanged && repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Employee with email " + request.email() + " already exists");
        }
        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setDepartment(request.department());
        employee.setSalary(request.salary());
        return EmployeeResponse.from(repository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    private Employee findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }
}
