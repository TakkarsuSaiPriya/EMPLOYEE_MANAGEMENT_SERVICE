package com.cts.employee.config;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String value) {
        return LocalDate.parse(value); // expects yyyy-MM-dd
    }

    @Override
    public String marshal(LocalDate value) {
        return value.toString();
    }
}