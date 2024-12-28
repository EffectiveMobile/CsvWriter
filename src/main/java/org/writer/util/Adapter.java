package org.writer.util;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class Adapter {

    public String dateOfBirthConverter(LocalDate dateOfBirth) {

        DateTimeFormatter dateTimeFormatter  = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return dateTimeFormatter.format(dateOfBirth);
    }
}
