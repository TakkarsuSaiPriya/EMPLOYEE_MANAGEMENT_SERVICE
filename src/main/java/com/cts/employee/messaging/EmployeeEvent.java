package com.cts.employee.messaging;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class EmployeeEvent {
    private Long employeeId;
    private String action;
    private Instant timestamp;
}