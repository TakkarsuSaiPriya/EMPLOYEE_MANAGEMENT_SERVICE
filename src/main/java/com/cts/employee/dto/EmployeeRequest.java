package com.cts.employee.dto;

import com.cts.employee.config.LocalDateAdapter;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;

import java.time.LocalDate;

@Data
@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String department;

    @PastOrPresent
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateOfJoining;
}