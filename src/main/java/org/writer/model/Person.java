package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.ColumnCsv;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @ColumnCsv(name = "имя")
    private String firstName;

    @ColumnCsv(name = "фамилия")
    private String lastName;

    @ColumnCsv(name = "день_рождения")
    private int dayOfBirth;

    @ColumnCsv(name = "месяц_рождения")
    private Months monthOfBirth;

    @ColumnCsv(name = "год_рождения")
    private int yearOfBirth;

}
