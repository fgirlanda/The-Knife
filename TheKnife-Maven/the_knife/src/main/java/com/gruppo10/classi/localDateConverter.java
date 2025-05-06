package com.gruppo10.classi;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class localDateConverter extends AbstractBeanField<LocalDate, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    protected Object convert(String value) {
        return LocalDate.parse(value, FORMATTER); // Usa il formato dd-MM-yyyy
    }
}
