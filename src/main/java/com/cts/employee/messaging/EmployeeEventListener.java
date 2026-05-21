package com.cts.employee.messaging;

import com.cts.employee.entity.AuditLog;
import com.cts.employee.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeEventListener {

    private final AuditLogRepository auditLogRepository;

    @JmsListener(destination = "employee.events")
    public void receive(EmployeeEvent event) {

        //  Log message receipt (this is what shows messaging in console)
        System.out.println("✅ EVENT RECEIVED FROM ACTIVE MQ: " + event);

        //  Persist audit entry triggered by JMS event
        AuditLog log = new AuditLog();
        log.setEmployeeId(event.getEmployeeId());
        log.setAction(event.getAction());
        log.setSource("JMS");

        auditLogRepository.save(log);
    }
}