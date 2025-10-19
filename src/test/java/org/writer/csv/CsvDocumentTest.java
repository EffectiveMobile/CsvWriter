package org.writer.csv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.writer.csv.util.TestDataBuilder;
import org.writer.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvDocumentTest {

    private final CsvDocument csvDocument = new CsvDocument();
    private final Path testFile = Path.of("test.csv");

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(testFile);
    }

    @Test
    void testColumnsExcludesTransient() {
        Person p = TestDataBuilder.person();
        List<String> columns = csvDocument.columns(p);

        assertTrue(columns.contains("firstName"));
        assertTrue(columns.contains("lastName"));
        assertTrue(columns.contains("dayOfBirth"));

        // должно быть исключено
        assertFalse(columns.contains("password"));
    }

    @Test
    void testValuesExcludesTransient() {
        Person p = TestDataBuilder.person();
        List<String> values = csvDocument.values(p);

        assertTrue(values.contains(p.getFirstName()));
        assertTrue(values.contains(p.getLastName()));
        // пароль не должен попасть
        assertFalse(values.contains(p.getPassword()));
    }

    @Test
    void testWriteToFileCreatesCsv() throws IOException {
        List<Person> people = TestDataBuilder.people();

        csvDocument.writeToFile(people, "test");

        assertTrue(Files.exists(testFile));

        String content = Files.readString(testFile);
        // Проверяем заголовки
        assertTrue(content.startsWith("firstName, lastName, dayOfBirth"));
        // Проверяем значения
        Person p1 = people.get(0);
        assertTrue(content.contains(String.format("%s, %s", p1.getFirstName(), p1.getLastName())));
        Person p2 = people.get(1);
        assertTrue(content.contains(String.format("%s, %s", p2.getFirstName(), p2.getLastName())));
        // Убеждаемся, что паролей нет
        assertFalse(content.contains("secret"));
        assertFalse(content.contains("hidden"));
    }

    @Test
    void testWriteToFileThrowsOnEmptyList() {
        assertThrows(RuntimeException.class,
                () -> csvDocument.writeToFile(List.of(), "empty"));
    }

    @Test
    void testWriteToFileThrowsOnNull() {
        assertThrows(RuntimeException.class,
                () -> csvDocument.writeToFile(null, "null"));
    }
}