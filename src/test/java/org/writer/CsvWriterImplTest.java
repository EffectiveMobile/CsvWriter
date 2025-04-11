package org.writer;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvWriterImplTest {
    private CsvWriterImpl csvWriter;
    private Faker faker;

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriterImpl();
        faker = new Faker();
    }

    @Test
    void testWritePersons() {
        List<Person> people = List.of(
                new Person("John", "Doe", 15, Months.JANUARY, 1990),
                new Person(faker.name().firstName(), faker.name().lastName(), faker.number().numberBetween(1, 31), Months.MARCH, 2000)
        );
        String fileName = "persons.csv";
        csvWriter.writeToFile(people, fileName);

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            Assertions.assertEquals(3, lines.size());
            Assertions.assertEquals("First Name,Last Name,Day,Month,Year", lines.get(0));
            Assertions.assertEquals("John,Doe,15,JANUARY,1990", lines.get(1));
        } catch (Exception e) {
            Assertions.fail("Failed to read file: " + e.getMessage());
        }
    }

    @Test
    void testWriteStudents() {
        List<Student> students = List.of(
                new Student("Alice", List.of("A", "B")),
                new Student("Bob", List.of("C", faker.letterify("?")))
        );
        String fileName = "students.csv";
        csvWriter.writeToFile(students, fileName);

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            Assertions.assertEquals(3, lines.size());
            Assertions.assertEquals("Name,Scores", lines.get(0));
            Assertions.assertTrue(lines.get(1).startsWith("Alice,A;B"));
        } catch (Exception e) {
            Assertions.fail("Failed to read file: " + e.getMessage());
        }
    }

    @Test
    void testWriteEmptyList() {
        String fileName = "empty.csv";
        csvWriter.writeToFile(List.of(), fileName);
        Assertions.assertFalse(Files.exists(Paths.get(fileName))); // Файл не должен создаться
    }
}
