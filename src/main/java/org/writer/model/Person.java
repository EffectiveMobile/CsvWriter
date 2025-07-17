package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.Column;

/**
 * Модель человека с персональными данными.
 * Содержит аннотации {@link Column} для экспорта в Csv.
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    /**
     * Имя человека.
     */
    @Column(columnName = "First name", position = 1)
    private String firstName;

    /**
     * Фамилия человека.
     */
    @Column(columnName = "Last name", position = 2)
    private String lastName;

    /**
     * День рождения. Не конкретная дата, а только число.
     */
    @Column(columnName = "Day of birth", position = 3)
    private int dayOfBirth;

    /**
     * Месяц рождения.
     * Используются месяцы из {@link Months}.
     */
    @Column(columnName = "Month of birth", position = 4)
    private Months monthOfBirth;

    /**
     * Год рождения.
     */
    @Column(columnName = "Year of birth", position = 5)
    private int yearOfBirth;

}
