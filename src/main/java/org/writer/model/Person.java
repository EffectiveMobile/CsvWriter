package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvField;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvField(name = "First Name")
    private String firstName;

    @CsvField(name = "Last Name")
    private String lastName;

    @CsvField(name = "Day Of Birth")
    private int dayOfBirth;

    @CsvField(name = "Month Of Birth")
    private Months monthOfBirth;

    @CsvField(name = "Year Of Birth")
    private int yearOfBirth;

}
