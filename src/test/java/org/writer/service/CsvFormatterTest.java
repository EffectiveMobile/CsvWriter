package org.writer.service;

import org.junit.jupiter.api.Test;
import org.writer.model.Student;
import org.writer.util.DataGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для форматирования данных с помощью CsvFormatter.
 * Проверяет корректность форматирования заголовков и данных для сгенерированных списков.
 */
class CsvFormatterTest {

    private final CsvFormatter csvFormatter = new CsvFormatter();

    @Test
    void testFormatHeadersWithGeneratedData() {

        List<Student> data = DataGenerator.generateStudents(5);


        String headers = csvFormatter.formatHeaders(data);


        assertTrue(headers.contains("Name"));
        assertTrue(headers.contains("Scores1"));
        assertTrue(headers.contains("Scores2"));
        assertTrue(headers.contains("Scores3"));
        assertTrue(headers.contains("Scores4"));
        assertTrue(headers.contains("Scores5"));
    }

    @Test
    void testFormatDataWithGeneratedData() {

        List<Student> data = DataGenerator.generateStudents(5);


        List<String> formattedData = csvFormatter.formatData(data);


        Student firstStudent = data.get(0);
        String expectedFormattedData = firstStudent.getName() + "," + String.join(",", firstStudent.getScore());
        assertEquals(expectedFormattedData, formattedData.get(0));
    }

    @Test
    void testFormatEmptyListWithGeneratedData() {

        List<Student> data = DataGenerator.generateStudents(0);


        List<String> formattedData = csvFormatter.formatData(data);


        assertTrue(formattedData.isEmpty());
    }

}
