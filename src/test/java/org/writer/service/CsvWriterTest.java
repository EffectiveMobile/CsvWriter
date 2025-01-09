package org.writer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.exception.EmptyDataException;
import org.writer.model.Person;
import org.writer.util.DataGenerator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Тесты для класса {@link CsvWriter}.
 * <p>
 * Проверяет корректность записи данных в CSV-файл, обработку ошибок и работу с различными данными.
 */
class CsvWriterTest {

    private CsvWriter csvWriter;
    private List<Person> personList;
    private static final String TEST_OUTPUT_DIRECTORY = "test_output";

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriter(new CsvFormatter());
        personList = DataGenerator.generatePersons(5);
        createDirectoryIfNotExists();
    }

    @Test
    void shouldThrowExceptionWhenDataIsNull() {
        assertThrows(EmptyDataException.class, () -> csvWriter.writeToFile(null, TEST_OUTPUT_DIRECTORY + File.separator + "test.csv"));
    }

    @Test
    void shouldThrowExceptionWhenDataIsEmpty() {
        assertThrows(EmptyDataException.class, () -> csvWriter.writeToFile(List.of(), TEST_OUTPUT_DIRECTORY + File.separator + "test.csv"));
    }

    @Test
    void shouldWriteDataToFile() {
        String fileName = TEST_OUTPUT_DIRECTORY + File.separator + "test.csv";

        assertDoesNotThrow(() -> csvWriter.writeToFile(personList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(Paths.get(fileName)));
        assertFalse(lines.isEmpty());
        assertEquals(personList.size() + 1, lines.size());

        String expectedHeaders = String.join(",", "First Name", "Last Name", "Day of Birth", "Month of Birth", "Year of Birth");
        assertEquals(expectedHeaders, lines.get(0));
    }

    @Test
    void shouldWriteMultipleLinesToFile() {
        String fileName = TEST_OUTPUT_DIRECTORY + File.separator + "test_multiple.csv";

        assertDoesNotThrow(() -> csvWriter.writeToFile(personList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(Paths.get(fileName)));
        assertFalse(lines.isEmpty());
        assertEquals(personList.size() + 1, lines.size());

        for (int i = 0; i < personList.size(); i++) {
            Person person = personList.get(i);
            String expectedLine = String.format("%s,%s,%d,%s,%d",
                    person.getFirstName(),
                    person.getLastName(),
                    person.getDayOfBirth(),
                    person.getMonthOfBirth(),
                    person.getYearOfBirth());
            assertEquals(expectedLine, lines.get(i + 1));
        }
    }

    @Test
    void shouldCreateFileWithCorrectName() {
        String fileName = TEST_OUTPUT_DIRECTORY + File.separator + "output.csv";

        assertDoesNotThrow(() -> csvWriter.writeToFile(personList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    void shouldHandleLargeData() {
        String fileName = TEST_OUTPUT_DIRECTORY + File.separator + "large_data.csv";
        List<Person> largePersonList = DataGenerator.generatePersons(1000);

        assertDoesNotThrow(() -> csvWriter.writeToFile(largePersonList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(Paths.get(fileName)));
        assertEquals(largePersonList.size() + 1, lines.size());
    }

    private void createDirectoryIfNotExists() {
        File directory = new File(CsvWriterTest.TEST_OUTPUT_DIRECTORY);
        if (!directory.exists()) {
            assertTrue(directory.mkdir(), "Failed to create test output directory.");
        }
    }
}
