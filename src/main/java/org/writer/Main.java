package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.writeble.Writable;
import org.writer.writeble.WritableSCVImpl;
import org.writer.writeble.util.DataFakerCreator;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        DataFakerCreator creator = new DataFakerCreator();

        List<Student> students = creator.datafakerStudents();
        List<Person> persons = creator.datafakerPersons();

        Writable writable = new WritableSCVImpl();

        writable.writeToFile(students, "students.csv");
        writable.writeToFile(persons, "persons.csv");

    }

}