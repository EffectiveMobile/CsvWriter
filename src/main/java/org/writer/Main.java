package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Writable writable = new WritableImpl();

        List<Student> students = List.of(
                new Student("Student 1", List.of("5", "4", "3", "2")),
                new Student("Student 2", List.of("2", "1", "1", "2")),
                new Student("Student 3", List.of("2", "3", "4", "5"))
        );
        writable.writeToFile(students, "Students.csv");

        List<Person> persons = List.of(
                new Person("Имя 1", "Фамилия 1", 17, Months.SEPTEMBER, 1996),
                new Person("Имя 2", "Фамилия 2", 18, Months.OCTOBER, 1995),
                new Person("Имя 3", "Фамилия 3", 19, Months.NOVEMBER, 1994),
                new Person("Имя 4", "Фамилия 4", 20, Months.DECEMBER, 1993),
                new Person("Имя 5", "Фамилия 5", 21, Months.JANUARY, 1992)
        );
        writable.writeToFile(persons, "Persons.csv");
    }
}