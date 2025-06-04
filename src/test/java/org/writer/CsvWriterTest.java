package org.writer;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvWriterTest {
    private Writable writer;
    private Faker faker;

    @BeforeEach
    void setUp() {
        writer = new WriterImpl();
        faker = new Faker();
    }

    /**
     * Тест 1:
     * При наличии списка из нескольких Person метод writeToFile создаёт CSV-файл
     * с 1 заголовком + N строк, и строки содержат правильные значения.
     */
    @Test
    void testWriteToFile_CreatesCorrectCsv(@TempDir Path tempDir) throws IOException {
        Person p1 = Person.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .dayOfBirth(15)
                .monthOfBirth(Months.JUNE)
                .yearOfBirth(1990)
                .build();

        Person p2 = Person.builder()
                .firstName("Maria")
                .lastName("Petrova")
                .dayOfBirth(3)
                .monthOfBirth(Months.DECEMBER)
                .yearOfBirth(1992)
                .build();

        List<Person> people = List.of(p1, p2);

        Path csvFile = tempDir.resolve("people.csv");

        writer.writeToFile(people, csvFile.toString());

        List<String> lines = Files.readAllLines(csvFile);

        assertFalse(lines.isEmpty(), "File mustn't be empty");

        String header = lines.get(0);
        String expectedHeader = "First name,Last name,Day of birth,Month of birth,Year of birth";
        assertEquals(expectedHeader, header, "CSV header does not match");

        assertEquals(3, lines.size(), "Wrong number of string");

        String dataLine1 = lines.get(1);
        String expectedLine1 = "Ivan,Ivanov,15,JUNE,1990";
        assertEquals(expectedLine1, dataLine1, "The first line is incorrect");

        String dataLine2 = lines.get(2);
        String expectedLine2 = "Maria,Petrova,3,DECEMBER,1992";
        assertEquals(expectedLine2, dataLine2, "The second line is incorrect");
    }

    /**
     * Тест 2:
     * Если передать пустой список (или null), ожидается IllegalArgumentException.
     */
    @Test
    void testWriteToFile_EmptyListThrows(@TempDir Path tempDir) {
        List<Person> empty = List.of();
        Path csvFile = tempDir.resolve("empty.csv");

        assertThrows(IllegalArgumentException.class, () -> writer.writeToFile(empty, csvFile.toString()));
    }

    /**
     * Тест 3:
     * Если в объекте есть поле со значением null, в CSV получается пустая ячейка (между запятыми).
     */
    @Test
    void testWriteToFile_NullValuesBecomeEmpty(@TempDir Path tempDir) throws IOException {
        Person pNull = Person.builder()
                .firstName("Anna")
                .lastName(null)
                .dayOfBirth(1)
                .monthOfBirth(Months.MAY)
                .yearOfBirth(2000)
                .build();

        Path csvFile = tempDir.resolve("nulls.csv");
        writer.writeToFile(List.of(pNull), csvFile.toString());

        List<String> lines = Files.readAllLines(csvFile);
        assertEquals(2, lines.size(), "Must be a header and one data line");

        String dataLine = lines.get(1);
        String expected = "Anna,,1,MAY,2000";
        assertEquals(expected, dataLine, "Field with null must be null cell");
    }

    /**
     * Тест 4:
     * Если строковое поле содержит запятую и кавычки, оно должно
     * правильно экранироваться: оборачиваться в кавычки и дублировать внутренние кавычки.
     */
    @Test
    void testWriteToFile_EscapingCommasAndQuotes(@TempDir Path tempDir) throws IOException {
        Person pEscape = Person.builder()
                .firstName("Anna, \"The\"")
                .lastName("O'Neil")
                .dayOfBirth(5)
                .monthOfBirth(Months.MARCH)
                .yearOfBirth(1985)
                .build();

        Path csvFile = tempDir.resolve("escape.csv");
        writer.writeToFile(List.of(pEscape), csvFile.toString());

        List<String> lines = Files.readAllLines(csvFile);
        assertEquals(2, lines.size(), "Must be a header and one data line");

        String dataLine = lines.get(1);
        String escapedFirst = "\"Anna, \"\"The\"\"\"";
        String expectedLine = escapedFirst + ",O'Neil,5,MARCH,1985";
        assertEquals(expectedLine, dataLine, "Wrong view of a line with coma and quotation marks");
    }

    /**
     * Тест 5:
     * Тестируем, что данные создаются при помощи Datafaker
     * и правильно конвертируются в Csv
     */
    @Test
    void testWithDatafaker(@TempDir Path tempDir) throws IOException {
        Person randomPerson = Person.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .dayOfBirth(faker.number().numberBetween(1, 28))
                .monthOfBirth(Months.values()[faker.number().numberBetween(0, 11)])
                .yearOfBirth(faker.number().numberBetween(1950, 2010))
                .build();

        Path csvFile = tempDir.resolve("random.csv");
        writer.writeToFile(List.of(randomPerson), csvFile.toString());

        List<String> lines = Files.readAllLines(csvFile);
        assertTrue(lines.size() >= 2, "File must have at least one data line and a header");
    }
}
