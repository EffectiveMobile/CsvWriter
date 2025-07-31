package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CSVColumn;
import org.writer.annotations.CSVReport;

@Data
@Builder
@AllArgsConstructor
@CSVReport
public class Person {

    @CSVColumn(columnName = "first name")
    private String firstName;

    @CSVColumn(columnName = "last name")
    private String lastName;

    @CSVColumn(columnName = "day of birth")
    private int dayOfBirth;

    @CSVColumn(columnName = "month of birth")
    private Months monthOfBirth;

    @CSVColumn(columnName = "year of birth")
    private int yearOfBirth;

}
