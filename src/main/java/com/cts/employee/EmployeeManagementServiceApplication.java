package com.cts.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class EmployeeManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementServiceApplication.class, args);
    }
}