package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Класс сущности Person
 */

@Data
@Builder
@AllArgsConstructor
public class Person {
    /** Поле имя */
    private String firstName;

    /** Поле фамилия */
    private String lastName;

    /** Поле дата рождения */
    private int dayOfBirth;

    /** Поле месяц рождения */
    private Months monthOfBirth;

    /** Поле год рождения */
    private int yearOfBirth;
}
