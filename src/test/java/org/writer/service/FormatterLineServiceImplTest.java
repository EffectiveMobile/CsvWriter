package org.writer.service;

import org.junit.jupiter.api.Test;
import org.writer.model.Student;
import org.writer.util.DataGeneration;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormatterLineServiceImplTest {

    private final FormatterLineServiceImpl formatterLineService = new FormatterLineServiceImpl();

    @Test
    void testFormatHeadersWithGeneratedData() {

        List<Student> data = DataGeneration.generateStudent(5);


        String headers = formatterLineService.formatHeaders(data).toString();


        assertTrue(headers.contains("Name"));
        assertTrue(headers.contains("Score"));
    }

    @Test
    void testFormatDataWithGeneratedData() {

        List<Student> data = DataGeneration.generateStudent(5);


        StringBuilder formattedData = formatterLineService.formatLine(data);
        List<String> list = Arrays.asList(formattedData.toString().split("\n"));


        Student firstStudent = data.get(0);
        String expectedFormattedData = firstStudent.getName() + "," + String.join(";", firstStudent.getScore());
        assertEquals(expectedFormattedData, list.get(0));
    }

    @Test
    void testFormatEmptyListWithGeneratedData() {

        List<Student> data = DataGeneration.generateStudent(0);


        StringBuilder formattedData = formatterLineService.formatLine(data);


        assertTrue(formattedData.isEmpty());
    }

}
