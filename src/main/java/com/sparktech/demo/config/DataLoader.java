package com.sparktech.demo.config;

import com.sparktech.demo.model.Employee;
import com.sparktech.demo.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Seeds a few employees when the app runs with the "dev" profile. */
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final EmployeeRepository repository;

    public DataLoader(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                new Employee("Rohan", "rohan@sparktech.com", "Engineering", new BigDecimal("85000")),
                new Employee("Anita", "anita@sparktech.com", "HR", new BigDecimal("60000")),
                new Employee("Vikram", "vikram@sparktech.com", "Finance", new BigDecimal("72000"))));
        log.info("Seeded {} employees", repository.count());
    }
}
