package com.cts.employee.dto;

import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.time.LocalDate;

@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class EmployeeRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String department;

    @PastOrPresent
    private LocalDate dateOfJoining;
}