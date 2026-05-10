package com.cts.employee.messaging;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class EmployeeEvent implements Serializable {

    // ✅ REQUIRED for Serializable
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String action;
    private Instant timestamp;
}