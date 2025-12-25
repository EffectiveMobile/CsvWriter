package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.util.GeneratorFakeData;
import org.writer.writer.CSVWriter;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> persons = GeneratorFakeData.generatePersons(12);
        List<Student> students = GeneratorFakeData.generateStudents(3);
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.writeToFile(persons, "persons.csv");
        csvWriter.writeToFile(students, "students.csv");
    }
}