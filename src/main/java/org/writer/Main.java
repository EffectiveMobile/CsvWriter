package org.writer;

import com.github.javafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Writable writer = new CsvWriter();
        Faker faker = new Faker(new Locale("ru"));
        Random random = new Random();

        int currentYear = LocalDateTime.now().getYear();


        List<Person> people = List.of(
                Person.builder().firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(random.nextInt(30) + 1)
                        .monthOfBirth(Months.values()[random.nextInt(12)])
                        .yearOfBirth(random.nextInt(currentYear - 100, currentYear)).build(),
                Person.builder().firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(random.nextInt(30) + 1)
                        .monthOfBirth(Months.values()[random.nextInt(12)])
                        .yearOfBirth(random.nextInt(currentYear - 100, currentYear)).build(),
                Person.builder().firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(random.nextInt(30) + 1)
                        .monthOfBirth(Months.values()[random.nextInt(12)])
                        .yearOfBirth(random.nextInt(currentYear - 100, currentYear)).build(),
                Person.builder().firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(random.nextInt(30) + 1)
                        .monthOfBirth(Months.values()[random.nextInt(12)])
                        .yearOfBirth(random.nextInt(currentYear - 100, currentYear)).build()
        );

        writer.writeToFile(people, "persons.csv");

        List<Student> students = List.of(
                Student.builder().name(faker.name().firstName()).score(List.of("F", "F", "F")).build(),
                Student.builder().name(faker.name().firstName()).score(List.of("A", "A", "A")).build(),
                Student.builder().name(faker.name().firstName()).score(List.of("A", "B")).build(),
                Student.builder().name(faker.name().firstName()).score(List.of("E", "E", "E", "E")).build()
        );

        writer.writeToFile(students, "students.csv");
    }
}