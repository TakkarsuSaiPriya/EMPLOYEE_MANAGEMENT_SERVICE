package com.cts.employee.service;

import com.cts.employee.dto.EmployeeRequest;
import com.cts.employee.entity.AuditLog;
import com.cts.employee.entity.Employee;
import com.cts.employee.exception.ResourceNotFoundException;
import com.cts.employee.messaging.EmployeeEvent;
import com.cts.employee.messaging.EmployeeEventProducer;
import com.cts.employee.repository.AuditLogRepository;
import com.cts.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository empRepo;
    private final AuditLogRepository auditRepo;
    private final EmployeeEventProducer producer;

    // Create employee
    public Employee create(EmployeeRequest req) {

        log.info("Creating employee: {}", req.getName());

        Employee emp = new Employee();
        BeanUtils.copyProperties(req, emp);

        Employee saved = empRepo.save(emp);

        sendEvent(saved.getId(), "CREATE");
        audit(saved.getId(), "CREATE");

        log.info("Employee created successfully with id: {}", saved.getId());

        return saved;
    }

    // Update employee
    public Employee update(Long id, EmployeeRequest req) {

        log.info("Updating employee with id: {}", id);

        Employee emp = empRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id));

        emp.setName(req.getName());
        emp.setEmail(req.getEmail());
        emp.setDepartment(req.getDepartment());
        emp.setDateOfJoining(req.getDateOfJoining());

        Employee updated = empRepo.save(emp);

        sendEvent(updated.getId(), "UPDATE");
        audit(id, "UPDATE");

        log.info("Employee updated successfully with id: {}", updated.getId());

        return updated;
    }

    // Get employee by id
    public Employee get(Long id) {

        log.info("Fetching employee with id: {}", id);

        return empRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id));
    }

    // Get all employees
    public List<Employee> getAll() {

        log.info("Fetching all employees");

        return empRepo.findAll();
    }

    // Delete employee
    public void delete(Long id) {

        log.warn("Deleting employee with id: {}", id);

        if (!empRepo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }

        empRepo.deleteById(id);

        sendEvent(id, "DELETE");
        audit(id, "DELETE");

        log.info("Employee deleted successfully with id: {}", id);
    }

    // Send JMS event
    private void sendEvent(Long id, String action) {

        try {
            log.info("Sending event {} for employee {}", action, id);

            producer.send(EmployeeEvent.builder()
                    .employeeId(id)
                    .action(action)
                    .timestamp(Instant.now())
                    .build());

            log.info("Event {} sent successfully", action);

        } catch (Exception ex) {
            log.error("JMS error while sending event: {}", ex.getMessage());
        }
    }

    // Audit log
    private void audit(Long id, String action) {

        AuditLog logEntity = new AuditLog();
        logEntity.setEmployeeId(id);
        logEntity.setAction(action);
        logEntity.setSource("REST");

        auditRepo.save(logEntity);

        log.info("Audit log created for employee {} with action {}", id, action);
    }
}