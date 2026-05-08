package com.cts.employee.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final JmsTemplate jmsTemplate;

    public void send(EmployeeEvent event) {
        jmsTemplate.convertAndSend("employee.events", event);
    }
}