package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.FormatterLineService;
import org.writer.service.FormatterLineServiceImpl;
import org.writer.util.DataGeneration;
import org.writer.util.FileCreation;
import org.writer.service.Writable;
import org.writer.service.WritableServiceImpl;

import java.io.File;
import java.util.List;

public class Main {

    private static final String DIRECTORY = "out";
    private static final String PERSON_FILE = "person.csv";
    private static final String STUDENT_FILE = "student.csv";

    public static void main(String[] args) {

        FileCreation.createFile(DIRECTORY);

        FormatterLineService formatterLineService = new FormatterLineServiceImpl();
        Writable writable = new WritableServiceImpl(formatterLineService);

        writePersonsToFile(writable);
        writeStudentsToFile(writable);

    }

    private static void writePersonsToFile(Writable writable) {
        List<Person> persons = DataGeneration.generatePerson(10);
        String personsFilePath = DIRECTORY + File.separator + PERSON_FILE;
        writable.writeToFile(persons, personsFilePath);
    }

    private static void writeStudentsToFile(Writable writable) {
        List<Student> students = DataGeneration.generateStudent(10);
        String studentsFilePath = DIRECTORY + File.separator + STUDENT_FILE;
        writable.writeToFile(students, studentsFilePath);
    }
}