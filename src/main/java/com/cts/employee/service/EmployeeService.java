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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository empRepo;
    private final AuditLogRepository auditRepo;
    private final EmployeeEventProducer producer;

    public Employee create(EmployeeRequest req) {

        Employee emp = new Employee();
        BeanUtils.copyProperties(req, emp);

        Employee saved = empRepo.save(emp);

        // JMS is best‑effort
        try {
            producer.send(EmployeeEvent.builder()
                    .employeeId(saved.getId())
                    .action("CREATE")
                    .timestamp(Instant.now())
                    .build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        audit(saved.getId(), "CREATE", "REST");
        return saved;
    }

    public Employee update(Long id, EmployeeRequest req) {

        Employee emp = empRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id));

        BeanUtils.copyProperties(req, emp);

        Employee updated = empRepo.save(emp);

        try {
            producer.send(EmployeeEvent.builder()
                    .employeeId(updated.getId())
                    .action("UPDATE")
                    .timestamp(Instant.now())
                    .build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        audit(id, "UPDATE", "REST");
        return updated;
    }

    public Employee get(Long id) {
        return empRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id " + id));
    }

    public List<Employee> getAll() {
        return empRepo.findAll();
    }

    public void delete(Long id) {

        if (!empRepo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }

        empRepo.deleteById(id);

        try {
            producer.send(EmployeeEvent.builder()
                    .employeeId(id)
                    .action("DELETE")
                    .timestamp(Instant.now())
                    .build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        audit(id, "DELETE", "REST");
    }

    private void audit(Long id, String action, String source) {
        AuditLog log = new AuditLog();
        log.setEmployeeId(id);
        log.setAction(action);
        log.setSource(source);
        auditRepo.save(log);
    }
}
