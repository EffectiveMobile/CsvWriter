package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.Column;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @Column(columnName = "First name", position = 1)
    private String firstName;

    @Column(columnName = "Last name", position = 2)
    private String lastName;

    @Column(columnName = "Day of birth name", position = 3)
    private int dayOfBirth;

    @Column(columnName = "Month of birth", position = 4)
    private Months monthOfBirth;

    @Column(columnName = "Year of birth", position = 5)
    private int yearOfBirth;

}
