package com.cts.employee.repository;

import com.cts.employee.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void shouldSaveEmployee() {

        Employee emp = new Employee();
        emp.setName("Repo Test");
        emp.setEmail("repo_" + System.currentTimeMillis() + "@test.com");
        emp.setDepartment("HR");
        emp.setDateOfJoining(LocalDate.now());
        emp.setStatus("ACTIVE");

        Employee saved = repository.save(emp);

        assertNotNull(saved.getId());
    }
}