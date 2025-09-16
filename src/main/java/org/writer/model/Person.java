package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;

/**
 * Класс {@code Person} представляет модель данных человека, используемую для сериализации и десериализации
 * в CSV-файл. Содержит информацию об имени, фамилии и дате рождения (день, месяц, год).
 *
 * <p>
 * Класс использует аннотацию {@link org.writer.annotation.CsvField} для указания соответствия полей
 * столбцам CSV-файла, включая их имена и порядок. Поддерживает автоматическую генерацию геттеров, сеттеров,
 * методов {@code toString}, {@code equals} и {@code hashCode} благодаря аннотации {@link lombok.Data}.
 * Также предоставляет конструктор и паттерн Builder благодаря аннотациям {@link lombok.AllArgsConstructor}
 * и {@link lombok.Builder}.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    /**
     * Имя человека. Соответствует столбцу "first_name" в CSV-файле с порядком 1.
     */
    @CsvField(columnName = "first_name", order = 1)
    private String firstName;

    /**
     * Фамилия человека. Соответствует столбцу "last_name" в CSV-файле с порядком 2.
     */
    @CsvField(columnName = "last_name", order = 2)
    private String lastName;

    /**
     * День рождения человека. Соответствует столбцу "date_of_birth" в CSV-файле с порядком 3.
     */
    @CsvField(columnName = "date_of_birth", order = 3)
    private int dayOfBirth;

    /**
     * Месяц рождения человека, представленный перечислением {@link Months}.
     * Соответствует столбцу "month_of_birth" в CSV-файле с порядком 4.
     */
    @CsvField(columnName = "month_of_birth", order = 4)
    private Months monthOfBirth;

    /**
     * Год рождения человека. Соответствует столбцу "year_of_birth" в CSV-файле с порядком 5.
     */
    @CsvField(columnName = "year_of_birth", order = 5)
    private int yearOfBirth;

}
