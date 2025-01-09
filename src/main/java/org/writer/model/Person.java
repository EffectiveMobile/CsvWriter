package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;


/**
 * Класс, представляющий информацию о человеке с аннотациями для сериализации в CSV.
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvField(name = "First Name")
    private String firstName;

    @CsvField(name = "Last Name")
    private String lastName;

    @CsvField(name = "Day of Birth")
    private int dayOfBirth;

    @CsvField(name = "Month of Birth")
    private Months monthOfBirth;

    @CsvField(name = "Year of Birth")
    private int yearOfBirth;

}
