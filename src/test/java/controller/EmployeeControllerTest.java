package com.cts.employee.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateEmployee() throws Exception {

        String uniqueEmail = "junit_" + System.currentTimeMillis() + "@test.com";

        String xml = """
            <Employee>
              <name>JUnit User</name>
              <email>%s</email>
              <department>QA</department>
              <dateOfJoining>2025-01-01</dateOfJoining>
            </Employee>
        """.formatted(uniqueEmail);

        mockMvc.perform(
                post("/employees")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(xml)
        ).andExpect(status().isOk());
    }
}