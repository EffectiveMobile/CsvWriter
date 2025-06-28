package org.writer.model.generators;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Additional class for quickly creating {@link Person} class objects.
 *
 * @ClassName PersonGenerator
 * @Author Alexei Shvariov
 * @Date 27.06.2025
 * @Version 1.0
 */

@RequiredArgsConstructor
public class PersonGenerator {
    private final Faker faker;

    /**Create object of {@link Person} class  with use DataFaker library
     * @return object of Person class*/
    public Person getPerson() {
        return Person.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .dayOfBirth(faker.number().numberBetween(1, 28))
                .monthOfBirth(faker.options().option(Months.class))
                .yearOfBirth(faker.number().numberBetween(2000, 2025))
                .passport(faker.passport().valid())
                .build();
    }

    /**Create Person class object's collection with use DataFaker library
     * @return object of {@link Person} class
     * @param size - collection size */
    public List<Person> getPersonsList(int size) {
        final List<Person> persons = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            persons.add(getPerson());
        }
        return persons;
    }


}
