package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.Csv;

@Data
@Builder
@AllArgsConstructor
@Csv
public class Person {

    private String firstName;

    private String lastName;

    private int dayOfBirth;

    private Months monthOfBirth;

    private int yearOfBirth;

}
