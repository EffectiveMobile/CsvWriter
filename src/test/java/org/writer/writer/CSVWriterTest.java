package org.writer.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки функциональности CSVWriter.
 *
 * @author Sidorin Aleksei
 * @version 1.0
 * @since 16.10.2025
 */
class CSVWriterTest {

    /**
     * Очищает все тестовые файлы после каждого теста.
     * Удаляет CSV файлы из директории outputs, которые могли быть созданы в ходе тестирования.
     *
     * @throws IOException если возникают ошибки ввода-вывода при удалении файлов
     */
    @AfterEach
    void cleanUpTestFiles() throws IOException {
        String outputDir = "outputs";
        Path outputPath = Paths.get(outputDir);

        if (!Files.exists(outputPath)) {
            return;
        }

        try (var files = Files.list(outputPath)) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".csv")
                                    && path.toString().contains("test"))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка при удалении файла " + path.getFileName() + ": " + e.getMessage());
                        }
                    });
        }
    }

    /**
     * Тестирует запись корректных данных в CSV файл.
     * Проверяет создание файла, его содержимое, количество строк,
     * соответствие заголовка и данных ожидаемым значениям.
     *
     * @throws IOException если возникают ошибки ввода-вывода при чтении файла
     */
    @Test
    void testWriteToFile_WithValidData_CreatesFileWithCorrectContent() throws IOException {
        CSVWriter csvWriter = new CSVWriter();
        List<Person> testData = Arrays.asList(
                new Person("John", "Doe", 1, Months.APRIL, 2003),
                new Person("Jane", "Smith", 12, Months.AUGUST, 1998),
                new Person("Rex", "Miller", 10, Months.SEPTEMBER, 1990)
        );

        String fileName = "test_output.csv";
        File outputFile = new File("outputs", fileName);

        csvWriter.writeToFile(testData, fileName);

        assertTrue(outputFile.exists(), "File should be created");
        assertTrue(outputFile.length() > 0, "File should not be empty");

        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertEquals(4, lines.size(), "Should have header + 3 data rows");

        String expectedHeader = "Имя,Фамилия,День рождения,Месяц рождения,Год рождения";
        assertEquals(expectedHeader, lines.get(0), "Header should match annotation names");

        assertEquals("John,Doe,1,APRIL,2003", lines.get(1), "First data row should match");
        assertEquals("Jane,Smith,12,AUGUST,1998", lines.get(2), "Second data row should match");
        assertEquals("Rex,Miller,10,SEPTEMBER,1990", lines.get(3), "Third data row should match");
    }

    /**
     * Тестирует обработку кавычек в данных.
     * Проверяет корректное экранирование запятых, кавычек,
     * символов новой строки и возврата каретки.
     *
     * @throws IOException если возникают ошибки ввода-вывода при чтении файла
     */
    @Test
    void testWriteToFile_WithQuoteCorrect() throws IOException {
        CSVWriter csvWriter = new CSVWriter();
        List<Person> testData = Arrays.asList(
                new Person("John, Jr.", "Doe\"Smith", 1, Months.APRIL, 2003)
        );

        String fileName = "special_chars_test.csv";
        File outputFile = new File("outputs", fileName);

        csvWriter.writeToFile(testData, fileName);

        assertTrue(outputFile.exists(), "File should be created");

        String fileContent = new String(Files.readAllBytes(outputFile.toPath()));
        String[] lines = fileContent.split("\n");

        String firstDataLine = lines[1];
        assertTrue(firstDataLine.contains("\"John, Jr.\""));
        assertTrue(firstDataLine.contains("\"Doe\"\"Smith\""));

        List<String> fileLines = Files.readAllLines(outputFile.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, fileLines.size());

        assertTrue(fileLines.get(1).startsWith("\"John, Jr.\",\"Doe\"\"Smith\",1,APRIL,2003"));
    }

    /**
     * Тестирует поведение метода при передаче пустого списка данных.
     * Ожидается выброс исключения IllegalArgumentException с соответствующим сообщением.
     */
    @Test
    void testWriteToFile_WithEmptyList_ThrowsException() {
        CSVWriter csvWriter = new CSVWriter();
        List<Person> emptyData = Arrays.asList();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvWriter.writeToFile(emptyData, "empty_test.csv"));

        assertEquals("List of data cant be null or empty", exception.getMessage());
    }

    /**
     * Тестирует поведение метода при передаче null в качестве списка данных.
     * Ожидается выброс исключения IllegalArgumentException с соответствующим сообщением.
     */
    @Test
    void testWriteToFile_WithNullList_ThrowsException() {
        CSVWriter csvWriter = new CSVWriter();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvWriter.writeToFile(null, "null_test.csv"));

        assertEquals("List of data cant be null or empty", exception.getMessage());
    }

    /**
     * Тестирует создание выходной директории, если она не существует.
     * Проверяет, что директория создается и файл сохраняется в правильном расположении.
     *
     * @throws IOException если возникают ошибки ввода-вывода при проверке существования файла
     */
    @Test
    void testWriteToFile_CreatesOutputDirectory() throws IOException {
        CSVWriter csvWriter = new CSVWriter();
        List<Person> testData = Arrays.asList(
                new Person("John", "Doe", 1, Months.APRIL, 2003)
        );

        String fileName = "test_output.csv";
        File outputFile = new File("outputs", fileName);

        csvWriter.writeToFile(testData, fileName);

        assertTrue(outputFile.exists(), "Output directory should be created");
        assertTrue(outputFile.exists(), "File should be created in custom directory");
    }

    /**
     * Тестирует соблюдение порядка полей в выходном CSV файле.
     * Проверяет, что заголовки колонок соответствуют ожидаемому порядку.
     *
     * @throws IOException если возникают ошибки ввода-вывода при чтении файла
     */
    @Test
    void testWriteToFile_FieldOrderRespected() throws IOException {
        CSVWriter csvWriter = new CSVWriter();
        List<Person> testData = Arrays.asList(
                new Person("John", "Doe", 1, Months.APRIL, 2003),
                new Person("Jane", "Smith", 12, Months.AUGUST, 1998),
                new Person("Rex", "Miller", 10, Months.SEPTEMBER, 1990)
        );

        String fileName = "test_output.csv";
        File outputFile = new File("outputs", fileName);

        csvWriter.writeToFile(testData, fileName);
        List<String> lines = Files.readAllLines(outputFile.toPath());
        String header = lines.get(0);

        String[] columns = header.split(",");
        assertEquals("Имя", columns[0]);
        assertEquals("Фамилия", columns[1]);
        assertEquals("День рождения", columns[2]);
        assertEquals("Месяц рождения", columns[3]);
        assertEquals("Год рождения", columns[4]);
    }

}