package org.writer.util.data;

import lombok.experimental.UtilityClass;
import org.writer.model.CsvModel;
import org.writer.model.Person;
import org.writer.model.enums.Months;

import java.util.List;

/**
 * Utility class for generating sample person data.
 * Provides a static method to retrieve a list of {@link Person} objects for testing or demonstration purposes.
 */
@UtilityClass
public class PersonDataUtil {

    /**
     * Returns a list of sample person data.
     *
     * @return a list of {@link Person} objects.
     */
    public static List<CsvModel> getPeople() {
        return List.of(
                Person.builder()
                        .id("P1")
                        .firstName("John")
                        .lastName("Doe")
                        .dayOfBirth(15)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1990)
                        .build(),
                Person.builder()
                        .id("P2")
                        .firstName("Jane")
                        .lastName("Smith")
                        .dayOfBirth(20)
                        .monthOfBirth(Months.MARCH)
                        .yearOfBirth(1985)
                        .build(),
                Person.builder()
                        .id("P3")
                        .firstName("Alice")
                        .lastName("Johnson")
                        .dayOfBirth(10)
                        .monthOfBirth(Months.SEPTEMBER)
                        .yearOfBirth(1995)
                        .build()
        );
    }
}
