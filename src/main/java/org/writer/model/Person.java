package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvEntity;

@Data
@Builder
@AllArgsConstructor
@CsvEntity
public class Person {
    @CsvColumn(name = "firstName", order = 1)
    private String firstName;

    @CsvColumn(name = "lastName", order = 2)
    private String lastName;

    @CsvColumn(name = "birthDay", order = 5)
    private int dayOfBirth;

    @CsvColumn(name = "birthMonth", order = 4)
    private Months monthOfBirth;

    @CsvColumn(name = "birthYear", order = 3)
    private int yearOfBirth;
}
