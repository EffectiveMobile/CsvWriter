package org.writer;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

class WritableTest {

    private final Faker faker = new Faker();
    private final Writable csvWriter = new WritableImpl();

    @Test
    void writeToFilePersonTest() throws IOException {
        List<Person> persons = IntStream.range(0, 5)
                .mapToObj(o -> Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().firstName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(faker.options().option(Months.class))
                        .yearOfBirth(faker.number().numberBetween(1990, 2010))
                        .build())
                .toList();

        csvWriter.writeToFile(persons, "persons.csv");

        Assertions.assertTrue(Files.exists(Paths.get("persons.csv")));
    }

    @Test
    void writeToFileStudentTest() throws IOException {
        List<Student> students = IntStream.range(0, 5)
                .mapToObj(o -> Student.builder()
                        .name(faker.name().firstName())
                        .score(List.of(faker.number().digits(3)))
                        .build())
                .toList();

        csvWriter.writeToFile(students, "students.csv");

        Assertions.assertTrue(Files.exists(Paths.get("students.csv")));
    }
}