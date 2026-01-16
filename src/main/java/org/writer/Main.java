package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Max")
                        .score(Arrays.asList("1", "2"))
                        .build(),
                Student.builder()
                        .name("Lewis")
                        .score(Arrays.asList("22", "33"))
                        .build()
        );

        List<Person> persons = Arrays.asList(
                Person.builder()
                        .firstName("Max")
                        .lastName("Verstappen")
                        .dayOfBirth(7)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1985)
                        .build(),
                Person.builder()
                        .firstName("Lewis")
                        .lastName("Hamilton")
                        .dayOfBirth(30)
                        .monthOfBirth(Months.SEPTEMBER)
                        .yearOfBirth(1997)
                        .build()
        );

        Writable csvWriter = new WritableImpl();
        csvWriter.writeToFile(students, "students.csv");
        csvWriter.writeToFile(persons, "persons.csv");
    }
}