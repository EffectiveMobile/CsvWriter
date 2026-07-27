package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvWritable;

@Data
@Builder
@AllArgsConstructor
public class Person {
    @CsvWritable
    private String firstName;

    private String lastName;

    @CsvWritable
    private int dayOfBirth;

    @CsvWritable
    private Months monthOfBirth;

    @CsvWritable(value = false)
    private int yearOfBirth;

}
