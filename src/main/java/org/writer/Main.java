package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        Faker faker = new Faker(new Locale("en"));
        List<Person> people = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        CsvWriter csvWriter = CsvWriter.getInstance();

        for (int i = 0; i < 10; i++) {
            people.add(Person.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .dayOfBirth(faker.number().numberBetween(1, 28))
                    .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                    .yearOfBirth(faker.number().numberBetween(1960, 2005))
                    .build());
        }

        for (int i = 0; i < 10; i++) {
            students.add(Student.builder()
                    .name(faker.name().firstName())
                    .score(faker.vehicle().standardSpecs())
                    .build());
        }

        csvWriter.writeToFile(people, "people.csv");
        csvWriter.writeToFile(students, "students.csv");
    }
}
