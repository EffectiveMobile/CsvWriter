package org.writer;

import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.writer.csv.CsvWriter;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Faker faker = new Faker();
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            people.add(new Person(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.number().numberBetween(1, 28),
                    Months.values()[faker.number().numberBetween(0, 12)],
                    faker.number().numberBetween(1970, 2005)
            ));
        }

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> scores = List.of(
                    String.valueOf(faker.number().numberBetween(60, 100)),
                    String.valueOf(faker.number().numberBetween(60, 100)),
                    String.valueOf(faker.number().numberBetween(60, 100))
            );

            students.add(Student.builder()
                    .name(faker.name().fullName())
                    .score(scores)
                    .build());
        }

        CsvWriter writer = new CsvWriter();
        writer.writeToFile(students, "output/student.csv");
        writer.writeToFile(people, "output/people.csv");
    }
}