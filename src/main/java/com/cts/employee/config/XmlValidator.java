package com.cts.employee.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import javax.xml.XMLConstants;
import javax.xml.validation.*;
import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;

@Component
public class XmlValidator {

    private final Schema schema;

    public XmlValidator() throws Exception {
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = factory.newSchema(
                new ClassPathResource("xsd/employee.xsd").getFile());
    }

    public void validate(Source source) throws Exception {
        schema.newValidator().validate(source);
    }
}