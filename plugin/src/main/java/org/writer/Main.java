package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.processor.CSVWriterProcessor;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CSVWriterProcessor csvWriterProcessor = new CSVWriterProcessor();

        List<Person> persons = new ArrayList<>(List.of(
                new Person("John", "Doe", 1, Months.JANUARY, 1990),
                new Person("Jane", "Doe", 2, Months.FEBRUARY, 1991),
                new Person("Jack", "Doe", 3, Months.MARCH, 1992)
        ));

        csvWriterProcessor.writeToFile(persons, "persons.csv");

        List<Months> months = List.of(
                Months.JANUARY,
                Months.FEBRUARY
        );

        csvWriterProcessor.writeToFile(months, "months.csv");


        List<Student> students = new ArrayList<>(List.of(
                new Student("John", List.of("90", "80", "70")),
                new Student("Jane", List.of("95", "85", "75")),
                new Student("Jack", List.of("100", "90", "80"))
        ));

        csvWriterProcessor.writeToFile(students, "students.csv");
    }
}