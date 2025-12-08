package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CsvWriter csvWriter = new CsvWriter();

        List<Student> students = List.of(
                new Student("Andrew", List.of("3.4", "4.5")),
                new Student("Andrew", List.of("3.5", "4.1", "2.3"))
        );

        List<Person> persons = List.of(
                new Person("Andrew", "Smith", 24, Months.MAY, 1956),
                new Person("Liza", "Black", 31, Months.DECEMBER, 1992),
                new Person("Kay", "Zoom", 1, Months.APRIL, 2001)
        );

        csvWriter.writeToFile(students, "students.csv");
        csvWriter.writeToFile(persons, "people.csv");
    }
}