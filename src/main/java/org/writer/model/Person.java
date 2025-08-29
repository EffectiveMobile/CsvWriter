package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvColumn(name = "Name", order = 1)
    private String firstName;

    @CsvColumn(name = "LastName", order = 2)
    private String lastName;

    @CsvColumn(name = "DayOfBirth", order = 3)
    private int dayOfBirth;

    @CsvColumn(name = "MonthOfBirth", order = 4)
    private Months monthOfBirth;

    @CsvColumn(name = "YearOfBirth", order = 5)
    private int yearOfBirth;

}
