package org.writer;

import java.util.Arrays;
import java.util.List;
import org.writer.model.Student;
import org.writer.service.CsvWriter;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        var writer = new CsvWriter();

        var listOfObjects = List.of(
            new Student("Sergey", Arrays.asList("A", "B")),
            new Student("Sergey", null),
            new Student("Alexander", List.of("A", "C")));
        writer.writeToFile(listOfObjects, "Test");

        writer.writeToFile(List.of(1, 2), "test");
    }
}