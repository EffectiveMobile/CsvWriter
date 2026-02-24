package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.CsvWriter;
import org.writer.service.Writable;
import org.writer.util.DataGenerator;

import java.util.List;

public class Main {

    private static final String PATH = "reports/";

    public static void main(String[] args) {

        Writable writer = new CsvWriter();

        List<Student> students = DataGenerator.generateStudents(5);
        writer.writeToFile(students, PATH + "students.csv");

        List<Person> persons = DataGenerator.generatePersons(5);
        writer.writeToFile(persons, PATH + "persons.csv");
    }
}