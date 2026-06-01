package com.cts.employee.controller;

import com.cts.employee.config.XmlValidator;
import com.cts.employee.dto.EmployeeRequest;
import com.cts.employee.dto.EmployeeResponse;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;
    private final XmlValidator validator;

    private static final JAXBContext JAXB_CONTEXT = createContext();

    private static JAXBContext createContext() {
        try {
            return JAXBContext.newInstance(EmployeeRequest.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize JAXBContext", e);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> create(@RequestBody String xml) throws Exception {

        validator.validate(new StreamSource(new StringReader(xml)));
        EmployeeRequest request = unmarshal(xml);

        Employee saved = service.create(request);

        return ResponseEntity.ok(mapToResponse(saved));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id,
                                                   @RequestBody String xml) throws Exception {

        validator.validate(new StreamSource(new StringReader(xml)));
        EmployeeRequest request = unmarshal(xml);

        Employee updated = service.update(id, request);

        return ResponseEntity.ok(mapToResponse(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(mapToResponse(service.get(id)));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        List<EmployeeResponse> responseList = service.getAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EmployeeRequest unmarshal(String xml) throws Exception {
        Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
        return (EmployeeRequest) unmarshaller.unmarshal(new StringReader(xml));
    }

    private EmployeeResponse mapToResponse(Employee emp) {
        return new EmployeeResponse(
                emp.getId(),
                emp.getName(),
                emp.getEmail(),
                emp.getDepartment(),
                emp.getDateOfJoining()
        );
    }
}
