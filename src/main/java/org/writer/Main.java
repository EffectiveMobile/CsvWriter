package org.writer;

import net.datafaker.Faker;
import org.writer.model.*;
import org.writer.service.CsvWriter;
import org.writer.service.Writable;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();

        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Person person = Person.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .dayOfBirth(faker.number().numberBetween(1, 31))
                    .monthOfBirth(faker.options().option(Months.class))
                    .yearOfBirth(faker.number().numberBetween(1950, 2023))
                    .build();
            people.add(person);
        }

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Student student = Student.builder()
                    .name(faker.name().fullName())
                    .score(List.of(
                            String.valueOf(faker.number().numberBetween(1, 10)),
                            String.valueOf(faker.number().numberBetween(1, 10)),
                            String.valueOf(faker.number().numberBetween(1, 10))
                    ))
                    .build();
            students.add(student);
        }

        Writable csvWriter = new CsvWriter();
        csvWriter.writeToFile(people, "out/people.csv");
        csvWriter.writeToFile(students, "out/students.csv");
}
}