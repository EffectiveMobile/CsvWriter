package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> people = List.of(
                Person.builder()
                        .firstName("Ivan")
                        .lastName("Ivanov")
                        .dayOfBirth(15)
                        .monthOfBirth(Months.JUNE)
                        .yearOfBirth(1990)
                        .build(),
                Person.builder()
                        .firstName("Maria")
                        .lastName("Petrova")
                        .dayOfBirth(3)
                        .monthOfBirth(Months.DECEMBER)
                        .yearOfBirth(1992)
                        .build()
        );

        List<Student> students = List.of(
                Student.builder()
                        .name("Alexey Petrov")
                        .score(List.of("A", "B+", "C"))
                        .build(),

                Student.builder()
                        .name("Ekaterina Smirnova")
                        .score(List.of("C", "S+", "A"))
                        .build()
        );

        Writable writer = new WriterImpl();
        writer.writeToFile(people, "people.csv");
        writer.writeToFile(students, "student.csv");
    }
}