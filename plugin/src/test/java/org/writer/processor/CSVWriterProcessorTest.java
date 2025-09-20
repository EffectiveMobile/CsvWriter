package org.writer.processor;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.exception.WriterException;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVWriterProcessorTest {

    private CSVWriterProcessor writer;
    private Path temp;

    @BeforeEach
    void setUp() throws IOException {
        writer = new CSVWriterProcessor();
        temp = Files.createTempFile("test", ".csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(temp);
    }

    @Test
    void testWriteEnum() throws IOException, CsvException {
        // Arrange
        List<Months> months = List.of(
                Months.JANUARY,
                Months.FEBRUARY,
                Months.MARCH
        );
        String fileName = temp.toString();

        // Act
        writer.writeToFile(months, fileName);

        // Assert
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> csvContent = reader.readAll();
            assertEquals(4, csvContent.size()); // Заголовок + 3 строки
            assertArrayEquals(new String[]{"value"}, csvContent.get(0)); // Заголовок
            assertArrayEquals(new String[]{"January"}, csvContent.get(1));
            assertArrayEquals(new String[]{"February"}, csvContent.get(2));
            assertArrayEquals(new String[]{"March"}, csvContent.get(3));
        }
    }

    @Test
    void testWritePerson() throws IOException, CsvException {
        // Arrange
        List<Person> persons = List.of(
                new Person("John", "Doe", 1, Months.JANUARY, 1990),
                new Person("Jane", "Doe", 2, Months.FEBRUARY, 1991),
                new Person("Jack", "Doe", 3, Months.MARCH, 1992)
        );
        String fileName = temp.toString();

        // Act
        writer.writeToFile(persons, fileName);

        // Assert
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> csvContent = reader.readAll();
            assertEquals(4, csvContent.size()); // Заголовок + 3 строки
            assertArrayEquals(new String[]{"firstName", "lastName", "dayOfBirth", "monthOfBirth", "yearOfBirth"},
                    csvContent.get(0)); // Заголовок
            assertArrayEquals(new String[]{"John", "Doe", "1", "JANUARY", "1990"}, csvContent.get(1));
            assertArrayEquals(new String[]{"Jane", "Doe", "2", "FEBRUARY", "1991"}, csvContent.get(2));
            assertArrayEquals(new String[]{"Jack", "Doe", "3", "MARCH", "1992"}, csvContent.get(3));
        }
    }

    @Test
    void testNullDataThrowsException() {
        // Arrange
        WriterException writerException = assertThrows(WriterException.class, () ->
                writer.writeToFile(null, temp.toString()));

        // Assert
        assertEquals("Data cannot be null", writerException.getMessage());
    }

    @Test
    void testEmptyDataThrowsException() {
        // Arrange
        WriterException writerException = assertThrows(WriterException.class, () ->
                writer.writeToFile(Collections.emptyList(), temp.toString()));

        // Assert
        assertEquals("Data cannot be empty", writerException.getMessage());
    }

    @Test
    void testEnumWithoutCsvFieldAnnotationThrowsException() {
        // Arrange
        enum TestEnum {
            VALUE;
            private String field;
        }
        List<TestEnum> data = List.of(TestEnum.VALUE);

        // Assert
        WriterException writerException = assertThrows(WriterException.class, () ->
                writer.writeToFile(data, temp.toString()));
        assertEquals("No fields annotated with @CsvField found in enum", writerException.getMessage());
    }

    @Test
    void testFilenameFromAnnotation() throws IOException, CsvException {
        // Arrange
        List<Months> months = List.of(
                Months.JANUARY,
                Months.FEBRUARY
        );

        // Act
        writer.writeToFile(months, null);

        // Assert
        try (CSVReader reader = new CSVReader(new FileReader("output.csv"))) {
            List<String[]> csvContent = reader.readAll();
            assertEquals(3, csvContent.size()); // Заголовок + 3 строки
            assertArrayEquals(new String[]{"value"}, csvContent.get(0)); // Заголовок
            assertArrayEquals(new String[]{"January"}, csvContent.get(1));
            assertArrayEquals(new String[]{"February"}, csvContent.get(2));
        } finally {
            Files.deleteIfExists(Paths.get("output.csv"));
        }
    }

}
