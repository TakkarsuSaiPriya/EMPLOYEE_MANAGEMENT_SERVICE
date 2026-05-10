package com.cts.employee.controller;

import com.cts.employee.config.XmlValidator;
import com.cts.employee.dto.EmployeeRequest;
import com.cts.employee.entity.Employee;
import com.cts.employee.service.EmployeeService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;
    private final XmlValidator validator;

    // ✅ Create JAXBContext ONCE
    private static final JAXBContext JAXB_CONTEXT = createContext();

    private static JAXBContext createContext() {
        try {
            return JAXBContext.newInstance(EmployeeRequest.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize JAXBContext", e);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> create(@RequestBody String xml) {
        try {
            validator.validate(new StreamSource(new StringReader(xml)));
            EmployeeRequest request = unmarshal(xml);
            Employee saved = service.create(request);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> update(@PathVariable Long id,
                                           @RequestBody String xml) {
        try {
            validator.validate(new StreamSource(new StringReader(xml)));
            EmployeeRequest request = unmarshal(xml);
            return ResponseEntity.ok(service.update(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Safe unmarshalling
    private EmployeeRequest unmarshal(String xml) throws Exception {
        Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
        return (EmployeeRequest) unmarshaller.unmarshal(new StringReader(xml));
    }
}
