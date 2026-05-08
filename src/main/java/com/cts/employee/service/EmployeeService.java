package com.cts.employee.service;

import com.cts.employee.dto.EmployeeRequest;
import com.cts.employee.entity.*;
import com.cts.employee.messaging.*;
import com.cts.employee.repository.*;
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

        producer.send(EmployeeEvent.builder()
                .employeeId(saved.getId())
                .action("CREATE")
                .timestamp(Instant.now())
                .build());

        audit(saved.getId(), "CREATE", "REST");
        return saved;
    }

    public Employee update(Long id, EmployeeRequest req) {
        Employee emp = empRepo.findById(id).orElseThrow();
        BeanUtils.copyProperties(req, emp);
        audit(id, "UPDATE", "REST");
        return empRepo.save(emp);
    }

    public Employee get(Long id) {
        return empRepo.findById(id).orElseThrow();
    }

    public List<Employee> getAll() {
        return empRepo.findAll();
    }

    public void delete(Long id) {
        empRepo.deleteById(id);
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