package org.writer;

import net.datafaker.Faker;
import org.writer.csv.SimpleCsvConverter;
import org.writer.model.generators.PersonGenerator;
import org.writer.model.generators.StudentsGenerator;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String sep = FileSystems.getDefault().getSeparator();
        final String fileName = "csvFiles";
        final File directory = new File(fileName);
        if (!directory.exists()) {
            directory.mkdir();
        }

        List<?> students = new StudentsGenerator(new Faker()).getStudentsList(100);
        SimpleCsvConverter csvConverter = new SimpleCsvConverter();
        csvConverter.setConvertCollectionAsMultipleCell(true);
        Writable csvWriter = new CsvWriter(csvConverter);
        csvWriter.writeToFile(students, fileName + sep + "students.csv");

        List<?> persons = new PersonGenerator(new Faker()).getPersonsList(100);
        Writable personWriter = new CsvWriter(new SimpleCsvConverter());
        personWriter.writeToFile(persons, fileName + sep + "persons.csv");

        List<?> strings = List.of("String1", "String2", "Text1");
        new CsvWriter(new SimpleCsvConverter()).writeToFile(strings, fileName + sep + "Strings.csv");

        List<?> integers = List.of(55, 47, 1230, 33, 44, 66, 77, 100);
        new CsvWriter(new SimpleCsvConverter()).writeToFile(integers, fileName + sep + "integers.csv");


    }
}