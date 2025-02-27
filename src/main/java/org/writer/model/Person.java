package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
/**
Модель человека
 */
@Data
@Builder
@AllArgsConstructor
public class Person {
    @CSVField(name = "firstName")
    private String firstName;
    @CSVField(name = "lastName")
    private String lastName;
    @CSVField(name = "dayOfBirth")
    private int dayOfBirth;
    @CSVField(name = "monthOfBirth")
    private Months monthOfBirth;
    @CSVField(name = "yearOfBirth")
    private int yearOfBirth;

}
