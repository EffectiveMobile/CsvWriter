package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String PERSON_FILE_NAME = "persons.csv";
    private static final String STUDENT_FILE_NAME = "students.csv";

    public static void main(String[] args) {
        Writable csvWriter = new CsvWriter();
        Faker faker = new Faker();
        Random random = new Random();

        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            people.add(
                    Person.builder()
                            .firstName(faker.name().firstName())
                            .lastName(faker.name().lastName())
                            .dayOfBirth(faker.number().numberBetween(1, 28))
                            .monthOfBirth(Months.values()[random.nextInt(Months.values().length)])
                            .yearOfBirth(faker.number().numberBetween(1941, 2025))
                            .build()
            );
        }

        csvWriter.writeToFile(people, PERSON_FILE_NAME);

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> scores = List.of(
                    String.valueOf(faker.number().numberBetween(1, 5)),
                    String.valueOf(faker.number().numberBetween(1, 5)),
                    String.valueOf(faker.number().numberBetween(1, 5))
            );

            students.add(
                    Student.builder()
                            .name(faker.name().fullName())
                            .score(scores)
                            .build()
            );
        }

        csvWriter.writeToFile(students, STUDENT_FILE_NAME);
    }
}