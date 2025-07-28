package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

/**
 *  Класс Person представляет модель чкловека
 *  <p> Благодаря аннотациям @CsvColumn, которые определяют названия столбцов,
 *  пользователь может быть записан в CSV файл.</p>
 */
@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvColumn(name = "Name")
    private String firstName;

    @CsvColumn(name = "LastName")
    private String lastName;

    @CsvColumn(name = "Day of Birth")
    private int dayOfBirth;

    @CsvColumn(name = "Month Of Birth")
    private Months monthOfBirth;

    @CsvColumn(name = "Year Of Birth")
    private int yearOfBirth;
}
