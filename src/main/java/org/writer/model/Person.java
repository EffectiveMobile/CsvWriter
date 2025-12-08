package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

/**
 * Класс {@code Person} представляет модель человека,
 * данные которого могут быть сериализованы в CSV‑файл.
 * <p>
 * Для каждого поля используется аннотация {@link CsvColumn},
 * которая определяет название соответствующей колонки в CSV‑отчёте.
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    /**
     * Имя человека.
     */
    @CsvColumn(name = "First name")
    private String firstName;

    /**
     * Фамилия человека.
     */
    @CsvColumn(name = "Last name")
    private String lastName;

    /**
     * День рождения (число месяца).
     */
    @CsvColumn(name = "Day of birth")
    private int dayOfBirth;

    /**
     * Месяц рождения.
     */
    @CsvColumn(name = "Month of birth")
    private Months monthOfBirth;

    /**
     * Год рождения.
     */
    @CsvColumn(name = "Year of birth")
    private int yearOfBirth;

}
