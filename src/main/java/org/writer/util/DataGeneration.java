package org.writer.util;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс для генерации тестовых данных с помощью {@link Faker}.
 */
public class DataGeneration {

    private static final Faker faker = new Faker();

    /**
     * Генерация данных для объекта {@link Person}.
     *
     * @param count - количество генераций.
     * @return - лист со сгенерированными данными.
     */
    public static List<Person> generatePerson(int count) {
        return IntStream.range(0, count)
                .mapToObj(i ->  Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 31))
                        .monthOfBirth(faker.options().option(Months.class))
                        .yearOfBirth(faker.number().numberBetween(1950, 2025))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Генерация данных для объекта {@link Student}.
     *
     * @param count - количество генераций.
     * @return - лист со сгенерированными данными.
     */
    public static List<Student> generateStudent(int count) {
        return IntStream.range(0, count)
                .mapToObj(i ->  Student.builder()
                        .name(faker.name().fullName())
                        .score(IntStream.range(0, 5)
                                .mapToObj(j -> String.valueOf(faker.number().numberBetween(1, 10)))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
