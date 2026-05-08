package com.cts.employee.messaging;

import com.cts.employee.entity.AuditLog;
import com.cts.employee.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeEventListener {

    private final AuditLogRepository repo;

    @JmsListener(destination = "employee.events")
    public void receive(EmployeeEvent event) {
        AuditLog log = new AuditLog();
        log.setEmployeeId(event.getEmployeeId());
        log.setAction(event.getAction());
        log.setSource("JMS");
        repo.save(log);
    }
}