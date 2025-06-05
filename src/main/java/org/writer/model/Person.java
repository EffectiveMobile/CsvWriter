package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvField;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvField(name = "FirstName")
    private String firstName;

    @CsvField(name = "LastName")
    private String lastName;

    @CsvField(name = "DayOfBirth")
    private int dayOfBirth;

    @CsvField(name = "MonthOfBirth")
    private Months monthOfBirth;

    @CsvField
    private int yearOfBirth;

}
