package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvSerializable;

@Data
@Builder
@AllArgsConstructor
@CsvSerializable
public class Person {

    @CsvColumn(name = "Имя")
    private String firstName;

    @CsvColumn(name = "Фамилия")
    private String lastName;

    @CsvColumn(name = "День рождения")
    private int dayOfBirth;

    @CsvColumn(name = "Месяц рождения")
    private Months monthOfBirth;

    @CsvColumn(name = "Год рождения")
    private int yearOfBirth;
}
