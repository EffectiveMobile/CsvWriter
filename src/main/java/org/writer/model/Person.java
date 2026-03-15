package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.Csv;

@Data
@Builder
@AllArgsConstructor
public class Person {

    private String firstName;
    @Csv(name = "lastName")
    private String lastName;

    @Csv(name = "dayOfBirth")
    private int dayOfBirth;

    @Csv(name = "monthOfBirth")
    private Months monthOfBirth;

    @Csv(name = "yearOfBirth")
    private int yearOfBirth;

}
