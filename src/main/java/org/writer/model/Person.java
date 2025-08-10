package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvField;

/**
 * Class describing data about a person.
 * Can be serialized to CSV thanks to the @CsvField annotation.
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    /**
     * Name
     */
    @CsvField(name = "First Name")
    private String firstName;

    /**
     * Last name
     */
    @CsvField(name = "Last Name")
    private String lastName;

    /**
     * Birthday
     */
    @CsvField(name = "Day Of Birth")
    private int dayOfBirth;

    /**
     * Birth month (uses Months enumeration)
     */
    @CsvField(name = "Month Of Birth")
    private Months monthOfBirth;

    /**
     * Year of birth
     */
    @CsvField(name = "Year Of Birth")
    private int yearOfBirth;
}
