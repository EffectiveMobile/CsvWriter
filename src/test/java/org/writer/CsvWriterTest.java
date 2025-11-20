package org.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.exception.CsvWriteException;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvWriterTest {

    private final CsvWriter writer = new CsvWriter();

    @TempDir
    Path tempDir;

    @Test
    void testWritePersonsCsv() throws IOException {
        List<Person> people = new ArrayList<>();
        people.add(Person.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .dayOfBirth(15)
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(2002)
                .build());

        Path testFile = tempDir.resolve("persons.csv");
        writer.writeToFile(people, testFile.toString());

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertEquals("first_name,last_name,day_of_birth,month_of_birth,year_of_birth", lines.get(0));
        assertEquals("Ivan,Ivanov,15,JANUARY,2002", lines.get(1));
    }

    @Test
    void testEscapeSpecialCharacters() throws IOException {
        List<Person> people = new ArrayList<>();
        people.add(Person.builder()
                .firstName("Alice, \"The Great\"")
                .lastName("O'Connor")
                .dayOfBirth(1)
                .monthOfBirth(Months.MARCH)
                .yearOfBirth(1985)
                .build());

        Path testFile = tempDir.resolve("special_chars.csv");
        writer.writeToFile(people, testFile.toString());

        List<String> lines = Files.readAllLines(testFile);
        assertEquals("\"Alice, \"\"The Great\"\"\",O'Connor,1,MARCH,1985", lines.get(1));
    }

    @Test
    void testEmptyListThrowsException() {
        List<Person> emptyList = new ArrayList<>();
        Path testFile = tempDir.resolve("empty.csv");
        assertThrows(IllegalArgumentException.class, () -> writer.writeToFile(emptyList, testFile.toString()));
    }

    @Test
    void testListOfStudents() throws IOException {
        List<Student> students = List.of(
                Student.builder().name("Bob").score(List.of("5", "4", "3")).build(),
                Student.builder().name("Charlie").score(List.of("3", "2", "4")).build()
        );

        Path testFile = tempDir.resolve("students.csv");
        writer.writeToFile(students, testFile.toString());

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(3, lines.size());
        assertEquals("name,score", lines.get(0));
        assertTrue(lines.get(1).contains("Bob"));
        assertTrue(lines.get(2).contains("Charlie"));
    }

    @Test
    void testInvalidFilePathThrowsCsvWriteException() {
        List<Person> people = List.of(
                Person.builder()
                        .firstName("Test")
                        .lastName("User")
                        .dayOfBirth(1)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(2000).build()
        );

        String invalidPath = "/invalid_path/test.csv";
        assertThrows(CsvWriteException.class, () -> writer.writeToFile(people, invalidPath));
    }
}
