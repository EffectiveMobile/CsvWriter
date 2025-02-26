package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.model.enums.Months;
import org.writer.validation.annotation.ValidCsvField;

/**
 * Represents a person entity.
 * Includes personal details such as name and date of birth.
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    @ValidCsvField(headerName = "First Name")
    private String firstName;

    @ValidCsvField(headerName = "Last Name")
    private String lastName;

    @ValidCsvField(headerName = "Day of birth")
    private int dayOfBirth;

    @ValidCsvField(headerName = "Month of birth")
    private Months monthOfBirth;

    @ValidCsvField(headerName = "Year of birth")
    private int yearOfBirth;
}
