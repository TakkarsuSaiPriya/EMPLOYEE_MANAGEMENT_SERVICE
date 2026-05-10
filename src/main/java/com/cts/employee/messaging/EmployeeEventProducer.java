package com.cts.employee.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final JmsTemplate jmsTemplate;

    public void send(EmployeeEvent event) {

        // ✅ Send message to ActiveMQ queue
        jmsTemplate.convertAndSend("employee.events", event);

        // ✅ THIS LINE MAKES THE MESSAGE VISIBLE IN INTELLIJ CONSOLE
        System.out.println("✅ EVENT SENT TO ACTIVE MQ: " + event);
    }
}