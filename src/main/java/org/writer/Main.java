package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CsvSerializer serializer = new CsvSerializer();
        Writable csvWriter = new MyCsvWriter(serializer);

        Faker faker = new Faker();

        testPeople(csvWriter, faker);

        testStudentsWithScores(csvWriter, faker);

        testPersonWithSpecialSymbol(csvWriter, faker);

    }

    private static void testPeople(Writable csvWriter, Faker faker) {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            people.add(Person.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .dayOfBirth(faker.number().numberBetween(1, 29))
                    .monthOfBirth(Months.values()[faker.number().numberBetween(0,11)])
                    .yearOfBirth(faker.number().numberBetween(1920,2015))
                    .build());
        }

        csvWriter.writeToFile(people, "csv/people.csv");
    }

    private static void testStudentsWithScores(Writable csvWriter, Faker faker) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            students.add(Student.builder()
                    .name(faker.name().firstName())
                    .score(getStudentScores(faker))
                    .build());
        }
        csvWriter.writeToFile(students, "csv/students.csv");
    }

    private static List<String> getStudentScores(Faker faker) {
        List<String> studentScores = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            studentScores.add(String.valueOf(faker.number().numberBetween(1, 5)));
        }
        return studentScores;
    }

    private static void testPersonWithSpecialSymbol(Writable csvWriter, Faker faker) {
        Person PersonWithSpecialSymbol = Person.builder()
                .firstName("Test, User")
                .lastName("Special; Name,")
                .dayOfBirth(1)
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(2000)
                .build();
        csvWriter.writeToFile(List.of(PersonWithSpecialSymbol), "csv/people_special_symbol.csv");
    }
}