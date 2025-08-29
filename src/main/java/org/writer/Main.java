package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.CsvFileWriter;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Faker f = new Faker();

        //Csv for persons
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            persons.add(new Person(f.name().firstName(),
                    f.name().lastName(),
                    f.number().numberBetween(1, 30),
                    Months.values()[f.number().numberBetween(1, 12)],
                    f.number().numberBetween(1980, 2020)
            ));
        }
        new CsvFileWriter().writeToFile(persons, "persons.csv");

        //Csv for students
        List<Student> students = new ArrayList<>();
        students.add(new Student(f.name().name(), List.of("4", "3", "5")));
        students.add(new Student(f.name().name(), List.of("5", "4", "5")));
        students.add(new Student(f.name().name(), List.of("4", "3", "4")));
        students.add(new Student(f.name().name(), List.of("3", "2", "5")));
        students.add(new Student(f.name().name(), List.of("4", "3", "5")));
        new CsvFileWriter().writeToFile(students, "students.csv");

        System.out.println("Cvs generate is Done!");
    }
}