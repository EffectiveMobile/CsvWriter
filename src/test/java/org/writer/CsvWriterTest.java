package org.writer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.writer.model.Car;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    private static CsvWriter csvWriter;

    @BeforeAll
    static void setUp() {
        csvWriter = new CsvWriter();
    }

    @Test
    void writeToFileTest() throws IOException {
        Path file = Files.createTempFile("test", ".csv");
        Person person1 = Person.builder()
                .firstName("Daniel")
                .lastName("Smith")
                .dayOfBirth(1)
                .monthOfBirth(Months.APRIL)
                .yearOfBirth(1990)
                .build();
        Person person2 = Person.builder()
                .firstName("Margo")
                .lastName("Da Silva")
                .dayOfBirth(5)
                .yearOfBirth(2005)
                .build();
        Person person3 = Person.builder()
                .lastName("Barkley")
                .dayOfBirth(16)
                .monthOfBirth(Months.FEBRUARY)
                .yearOfBirth(1993)
                .build();

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);

        csvWriter.writeToFile(persons, file.toString());

        List<String> lines = Files.readAllLines(file);
        assertEquals(4, lines.size()); //строка заголовков + 3 объекта
        assertEquals("firstName,lastName,dayOfBirth,monthOfBirth,yearOfBirth", lines.get(0));
        assertEquals("Daniel,Smith,1,APRIL,1990", lines.get(1));
        assertEquals("Margo,Da Silva,5,-,2005", lines.get(2));
        assertEquals("-,Barkley,16,FEBRUARY,1993", lines.get(3));
    }

    @Test
    void writeToFileNullTest() throws IOException {
        Path file = Files.createTempFile("null", ".csv");

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                csvWriter.writeToFile(null, file.toString()));

        assertEquals("Cannot invoke \"java.util.List.iterator()\" because \"data\" is null",
                exception.getMessage());
    }

    @Test
    void writeToFileEmptyListTest() throws IOException {
        Path file = Files.createTempFile("empty", ".csv");

        csvWriter.writeToFile(new ArrayList<>(), file.toString());

        List<String> lines = Files.readAllLines(file);
        assertTrue(lines.isEmpty());
    }

    @Test
    void writeToFileNotAnnotatedTest() throws IOException {
        Path file = Files.createTempFile("notCsv", ".csv");
        List<Car> cars = List.of(new Car("Audi", "V12", 2015),
                new Car("BMW", "V8", 2005));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                csvWriter.writeToFile(cars, file.toString()));

        assertTrue(exception.getMessage().contains("Passed class is not annotated by @CSV:"));
    }

    @Test
    void writeToNonExistentFileTest() {
        List<Student> students = new ArrayList<>();
        students.add(Student.builder().name("Taylor").build());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                csvWriter.writeToFile(students, "csv"));

        assertTrue(exception.getMessage().contains("Failed to write CSV file:"));
    }
}