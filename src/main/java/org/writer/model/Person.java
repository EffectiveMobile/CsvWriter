package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;
import org.writer.annotation.CsvWritable;

@Data
@Builder
@AllArgsConstructor
@CsvWritable
public class Person {

    @CsvField
    private String firstName;

    @CsvField
    private String lastName;

    @CsvField
    private int dayOfBirth;

    @CsvField
    private Months monthOfBirth;

    @CsvField
    private int yearOfBirth;

}
