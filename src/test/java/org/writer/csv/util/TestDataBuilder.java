package org.writer.csv.util;

import org.writer.model.Months;
import org.writer.model.Person;

import java.util.List;

public class TestDataBuilder {
    public static Person person() {
        return Person.builder()
                .firstName("Ivan")
                .lastName("Petrov")
                .dayOfBirth(10)
                .monthOfBirth(Months.AUGUST)
                .yearOfBirth(1988)
                .password("secret")
                .build();
    }

    public static List<Person> people() {
        return List.of(
                Person.builder().firstName("Nina").lastName("Ivanova").dayOfBirth(10).monthOfBirth(Months.AUGUST).yearOfBirth(1994).password("secret").build(),
                Person.builder().firstName("Bob").lastName("Smith").dayOfBirth(17).monthOfBirth(Months.JULY).yearOfBirth(1987).password("hidden").build()
        );
    }
}
