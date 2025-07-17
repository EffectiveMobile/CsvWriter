package org.writer;


import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CsvWriterTest {

    private static final String FILE_NAME = "test.csv";
    private final CsvWriter csvWriter = CsvWriter.getInstance();
    private final Faker faker = new Faker(new Locale("en"));

    @AfterEach
    void cleanup() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void writeToFile_ShouldThrowIllegalArgumentException_WhenNullData() {
        assertThrows(IllegalArgumentException.class,
                () -> csvWriter.writeToFile(null, FILE_NAME));
    }

    @Test
    void writeToFile_ShouldThrowIllegalArgumentException_WhenEmptyData() {
        List<?> emptyList = Collections.emptyList();

        assertThrows(IllegalArgumentException.class,
                () -> csvWriter.writeToFile(emptyList, FILE_NAME));
    }

    @Test
    void writeToFile_ShouldCreateFile_WhenValidData() throws IOException {
        String fileName = "test.csv";
        List<Person> data = List.of(
                Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        csvWriter.writeToFile(data, fileName);

        Path filePath = Paths.get(fileName);
        assertTrue(Files.exists(filePath));
    }

    @Test
    void writeToFile_ShouldWriteCorrectNumberOfLines_WhenValidData() throws IOException {
        List<Person> data = List.of(
                Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        csvWriter.writeToFile(data, FILE_NAME);
        Path path = Paths.get(FILE_NAME);
        List<String> lines = Files.readAllLines(path);

        assertEquals(2, lines.size()); // header + 2 data rows
    }

    @Test
    void writeToFile_WithValidData_ShouldWriteCorrectHeaders() throws IOException {
        List<Person> data = List.of(
                Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        csvWriter.writeToFile(data, FILE_NAME);
        Path filePath = Paths.get(FILE_NAME);
        String content = Files.readString(filePath);

        assertTrue(content.startsWith("FirstName,LastName,DayOfBirth,MonthOfBirth,YearOfBirth"));
    }

    @Test
    void writeToFile_ShouldEscapeWithQuotes_WhenDataWithComma() throws IOException {
        List<Person> data = List.of(
                Person.builder()
                        .firstName("John, J")
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        csvWriter.writeToFile(data, FILE_NAME);
        Path filePath = Paths.get(FILE_NAME);
        String content = Files.readString(filePath);

        assertTrue(content.contains("\"John, J\""));
    }

    @Test
    void writeToFile_ShouldEscapeWithQuotes_WhenDataWithQuotes() throws IOException {
        List<Person> data = List.of(
                Person.builder()
                        .firstName("John \"Johnny\"")
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        csvWriter.writeToFile(data, FILE_NAME);
        Path filePath = Paths.get(FILE_NAME);
        String content = Files.readString(filePath);

        assertTrue(content.contains("\"John \"\"Johnny\"\"\""));
    }

    @Test
    void writeToFile_ShouldWriteEmptyString_WhenNullData() throws IOException {
        List<Person> data = List.of(
                Person.builder()
                        .firstName(null)
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        csvWriter.writeToFile(data, FILE_NAME);
        Path filePath = Paths.get(FILE_NAME);
        List<String> lines = Files.readAllLines(filePath);
        assertTrue(lines.get(1).startsWith(","));
    }

    @Test
    void writeToFile_WithIOError_ShouldThrowRuntimeException() {
        String invalidPath = "/invalid/path/test.csv";
        List<Person> data = List.of(
                Person.builder()
                        .firstName(null)
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 12)])
                        .yearOfBirth(faker.number().numberBetween(1960, 2005))
                        .build()
        );

        assertThrows(RuntimeException.class,
                () -> csvWriter.writeToFile(data, invalidPath));
    }
}
