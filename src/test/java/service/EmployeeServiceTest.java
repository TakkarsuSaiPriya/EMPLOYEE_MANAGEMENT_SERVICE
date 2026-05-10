package com.cts.employee.service;

import com.cts.employee.dto.EmployeeRequest;
import com.cts.employee.messaging.EmployeeEventProducer;
import com.cts.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService service;

    @MockBean
    private EmployeeEventProducer producer;

    @MockBean
    private EmployeeRepository repository;

    @Test
    void shouldCreateEmployeeAndSendEvent() {

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Mockito User");
        request.setEmail("mock@test.com");
        request.setDepartment("IT");
        request.setDateOfJoining(LocalDate.now());

        Mockito.when(repository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var employee = service.create(request);

        assertNotNull(employee);
        Mockito.verify(producer).send(any());
    }
}