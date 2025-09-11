package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();
        WritableImpl writableImpl = new WritableImpl();

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            students.add(new Student(faker.name().firstName(), List.of(faker.size().adjective(), faker.size().adjective())));
        }
        writableImpl.writeToFile(students, "output.csv");


        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            persons.add(new Person(faker.name().firstName(), faker.name().lastName(), 22, Months.APRIL, 1094));
        }
        writableImpl.writeToFile(persons, "output.csv");

    }
}