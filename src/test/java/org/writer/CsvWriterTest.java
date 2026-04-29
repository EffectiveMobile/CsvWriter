package org.writer;

import net.datafaker.Faker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class CsvWriterTest {

    private CsvWriter csvWriter;
    private Faker faker;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriter();
        faker = new Faker();
    }

    @Test
    void testWritePersonToFile() throws IOException {
        List<Person> persons = IntStream.range(0, 9)
                .mapToObj(i -> Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 11)])
                        .yearOfBirth(faker.number().numberBetween(1985, 2005))
                        .build())
                .toList();

        Path filePath = tempDir.resolve("persons.csv");
        csvWriter.writeToFile(persons, filePath.toString());

        assertTrue(Files.exists(filePath));

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(10, lines.size());
        assertEquals("Имя,Фамилия,День рождения,Месяц рождения,Год рождения", lines.get(0));

        String[] firstPersonData = lines.get(1).split(",");
        assertEquals(persons.get(0).getFirstName(), firstPersonData[0]);
        assertEquals(persons.get(0).getLastName(), firstPersonData[1]);
    }

    @Test
    void testWriteStudentsToFile() throws IOException {
        List<Student> students = IntStream.range(0, 4)
                .mapToObj(i -> Student.builder()
                        .name(faker.name().fullName())
                        .score(List.of(
                                String.valueOf(faker.number().numberBetween(1, 5)),
                                String.valueOf(faker.number().numberBetween(1, 5))
                        )).build())
                .toList();

        Path filePath = tempDir.resolve("students.csv");
        csvWriter.writeToFile(students, filePath.toString());

        assertTrue(Files.exists(filePath));

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(5, lines.size());
        assertEquals("Имя,Оценки", lines.get(0));

        String[] firstStudentData = lines.get(1).split(",");
        assertEquals(students.get(0).getName(), firstStudentData[0]);
        assertEquals(String.join(";", students.get(0).getScore()), firstStudentData[1]);
    }

    @Test
    void testWriteEmptyListDoesNothing() {
        String fileName = "empty.csv";
        assertThrows(IllegalArgumentException.class, () -> csvWriter.writeToFile(List.of(), fileName));
        assertTrue(Files.notExists(Path.of(fileName)));
    }

    @Test
    void testWriteToFileThatNotCsvSerializable() {

        class UnannotatedClass {
            private final String data;

            public UnannotatedClass(String data) {
                this.data = data;
            }
        }

        List<UnannotatedClass> unannotatedList = List.of(new UnannotatedClass("some data"));
        assertThrows(IllegalArgumentException.class, () -> csvWriter.writeToFile(unannotatedList, "should_fail.csv"));
    }
}
