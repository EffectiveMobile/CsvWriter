package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File directory = new File("csv");
        if (!directory.exists()) {
            directory.mkdir();
        }

        List<Person> persons = new ArrayList<>();
        Person person1 = Person.builder()
                .firstName("Alex")
                .lastName("Smith")
                .dayOfBirth(12)
                .monthOfBirth(Months.APRIL)
                .yearOfBirth(2000)
                .build();
        Person person2 = Person.builder()
                .firstName("Mariya")
                .lastName("Simonova")
                .monthOfBirth(Months.JULY)
                .yearOfBirth(1996)
                .build();
        Person person3 = Person.builder()
                .firstName("Daniel")
                .dayOfBirth(30)
                .yearOfBirth(2002)
                .build();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);

        CsvWriter writer = new CsvWriter();
        writer.writeToFile(persons, "csv/persons.csv");

        List<String> scores1 = new ArrayList<>();
        scores1.add("5");
        scores1.add("4");
        scores1.add("4");

        List<String> scores2 = new ArrayList<>();
        scores2.add("3");
        scores2.add("5");
        scores2.add("4");

        List<String> scores3 = new ArrayList<>();
        scores3.add("2");
        scores3.add("3");
        scores3.add("2");

        Student student1 = Student.builder()
                .name("Paul")
                .score(scores1)
                .build();

        Student student2 = Student.builder()
                .name("Leonardo")
                .score(scores2)
                .build();

        Student student3 = Student.builder()
                .name("James")
                .score(scores3)
                .build();

        Student student4 = Student.builder()
                .name("Thomas")
                .build();

        Student student5 = Student.builder()
                .name("James")
                .score(new ArrayList<>())
                .build();

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        writer.writeToFile(students, "csv/students.csv");
    }
}