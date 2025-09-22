package org.writer;

import com.github.javafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

//        System.out.println("Hello world!");
        Writable writable = new CsvWriter();
        Faker faker = new Faker();

        List<Person> people = IntStream.range(0, 5)
                .mapToObj(i -> Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1970, 2005))
                        .build())
                .toList();

        writable.writeToFile(people, "people.csv");

        List<Student> students = IntStream.range(0, 5)
                .mapToObj(i -> Student.builder()
                        .name(faker.name().fullName())
                        .score(List.of("A", "B", "C"))
                        .build())
                .toList();

        writable.writeToFile(students, "students.csv");

        System.out.println("Файлы people.csv и students.csv успешно созданы!");
    }
}