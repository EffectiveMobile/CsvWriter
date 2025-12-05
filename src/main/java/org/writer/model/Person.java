package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CsvField;

@Data
@Builder
@AllArgsConstructor
public class Person {
    @CsvField("First name")
    private String firstName;

    @CsvField("Last name")
    private String lastName;

    @CsvField(value = "Birth day", ignore = true)
    private int dayOfBirth;

    private Months monthOfBirth;

    @CsvField("Birth year")
    private int yearOfBirth;

}
