package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CsvSerializer serializer = new CsvSerializer();
        Writable csvWriter = new MyCsvWriter(serializer);

        testPeople(csvWriter);

        testStudentsWithScores(csvWriter);

        testPersonWithSpecialSymbol(csvWriter);

    }

    private static void testPeople(Writable csvWriter) {
        List<Person> people = Arrays.asList(
                Person.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .dayOfBirth(15)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1990)
                        .build(),
                Person.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .dayOfBirth(22)
                        .monthOfBirth(Months.JUNE)
                        .yearOfBirth(1985)
                        .build()
        );
        csvWriter.writeToFile(people, "csv/people.csv");
    }

    private static void testStudentsWithScores(Writable csvWriter) {
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Alice")
                        .score(Arrays.asList("3", "5", "5"))
                        .build(),
                Student.builder()
                        .name("Bob")
                        .score(Arrays.asList("5", "4", "4", "2"))
                        .build(),
                Student.builder()
                        .name("Charlie")
                        .score(Arrays.asList("Math: 5", "Physics: 4", "Chemistry: 5"))
                        .build()
        );

        csvWriter.writeToFile(students, "csv/students.csv");
    }

    private static void testPersonWithSpecialSymbol(Writable csvWriter) {
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