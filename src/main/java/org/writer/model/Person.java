package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

/**
 * Класс, представляющий человека.
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvColumn(order = 0, name = "First Name")
    private String firstName;

    @CsvColumn(order = 1, name = "Last Name")
    private String lastName;

    @CsvColumn(order = 2, name = "Day")
    private int dayOfBirth;

    @CsvColumn(order = 3, name = "Month")
    private Months monthOfBirth;

    @CsvColumn(order = 4, name = "Year")
    private int yearOfBirth;

}
