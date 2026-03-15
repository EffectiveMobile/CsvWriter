package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Writable writable = new WritableImpl();

        List<Student> students = List.of(
            new Student("Nick", List.of("5,4,3,3,4")),
            new Student("Paul", List.of("4,4,4,4,4")),
            new Student("Ann", List.of("5,5,5,5,5"))
        );

        List<Person> persons = List.of(
            new Person("John", "Doe", 1, Months.JANUARY, 1991),
            new Person("Alexandr", "Pushkin", 6, Months.JUNE, 1799)
        );

        writable.writeToFile(students, "students.csv");
        writable.writeToFile(persons, "persons.csv");
    }
}