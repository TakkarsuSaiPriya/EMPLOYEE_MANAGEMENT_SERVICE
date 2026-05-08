package com.cts.employee.controller;

import com.cts.employee.config.XmlValidator;
import com.cts.employee.dto.EmployeeRequest;
import com.cts.employee.entity.Employee;
import com.cts.employee.service.EmployeeService;
import jakarta.xml.bind.JAXBContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public Employee create(@RequestBody String xml) throws Exception {
        validator.validate(new StreamSource(new StringReader(xml)));
        return service.create(unmarshal(xml));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_XML_VALUE)
    public Employee update(@PathVariable Long id,
                           @RequestBody String xml) throws Exception {
        validator.validate(new StreamSource(new StringReader(xml)));
        return service.update(id, unmarshal(xml));
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<Employee> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    private EmployeeRequest unmarshal(String xml) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(EmployeeRequest.class);
        return (EmployeeRequest) ctx.createUnmarshaller()
                .unmarshal(new StringReader(xml));
    }
}
