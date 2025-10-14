package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        Faker faker = new Faker();
        Writable csvWriter = new CsvWriter();


        List<Person> people = IntStream.range(0, 10)
                .mapToObj(i -> Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 11)])
                        .yearOfBirth(faker.number().numberBetween(1990, 2010))
                        .build())
                .collect(Collectors.toList());

        csvWriter.writeToFile(people, "people_report.csv");
        System.out.println("Отчет по людям сохранен в people_report.csv");


        List<Student> students = IntStream.range(0, 5)
                .mapToObj(i -> Student.builder()
                        .name(faker.name().fullName())
                        .score(List.of(
                                String.valueOf(faker.number().numberBetween(1, 5)),
                                String.valueOf(faker.number().numberBetween(1, 5)),
                                String.valueOf(faker.number().numberBetween(1, 5))
                        ))
                        .build())
                .collect(Collectors.toList());

        csvWriter.writeToFile(students, "students_report.csv");
        System.out.println("Отчет по студентам сохранен в students_report.csv");
    }
}