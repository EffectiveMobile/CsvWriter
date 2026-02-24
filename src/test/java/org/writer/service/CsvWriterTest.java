package org.writer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.exception.EmptyListException;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvWriterTest {
    private CsvWriter csvWriter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriter();
    }

    @Test
    @DisplayName("Корректная запись в CSV-файл")
    void writeToFile_shouldWriteCorrectCsvFormat() throws IOException {
            List<Student> students = List.of(
                Student.builder()
                        .name("Иван")
                        .score(List.of("5", "4", "5"))
                        .build(),
                Student.builder()
                        .name("Мария")
                        .score(List.of("3", "4", "4"))
                        .build()
        );
        Path filePath = tempDir.resolve("test.csv");
        csvWriter.writeToFile(students, filePath.toString());
        String content = Files.readString(filePath);
        assertTrue(content.contains("имя,успеваемость"), "Должен содержать заголовок");
        assertTrue(content.contains("Иван,5;4;5"), "Должен содержать первую строку");
        assertTrue(content.contains("Мария,3;4;4"), "Должен содержать вторую строку");
    }

    @Test
    @DisplayName("Пустой список студентов - выбрасывает кастомное исключение")
    void writeToFile_withEmptyList_shouldThrowException() {
       List<Student> emptyList = List.of();
        EmptyListException exception = assertThrows(
                EmptyListException.class,
                () -> csvWriter.writeToFile(emptyList, "test.csv")
        );
        assertEquals("Список пуст", exception.getMessage());
    }
}
