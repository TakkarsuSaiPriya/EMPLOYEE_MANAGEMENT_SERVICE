package com.cts.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private String department;
    private LocalDate dateOfJoining;
}
