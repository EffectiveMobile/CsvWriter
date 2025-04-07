package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.csv.CsvField;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvField(name = "First Name")
    private String firstName;

    @CsvField(name = "Last Name")
    private String lastName;

    @CsvField(name = "Day of birth")
    private int dayOfBirth;

    @CsvField(name = "Month of birth")
    private Months monthOfBirth;

    @CsvField(name = "Year of birth")
    private int yearOfBirth;

}
