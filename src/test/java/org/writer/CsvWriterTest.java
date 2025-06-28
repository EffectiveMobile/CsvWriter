package org.writer;


import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.csv.SimpleCsvConverter;
import org.writer.model.Person;
import org.writer.model.generators.PersonGenerator;
import org.writer.model.generators.StudentsGenerator;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.List;

public class CsvWriterTest {
    private static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    private final PersonGenerator personGenerator = new PersonGenerator(new Faker());
    private final StudentsGenerator studentsGenerator = new StudentsGenerator(new Faker());

    @TempDir
    private File tempDir;

    @Test
    @DisplayName("Test writeToFile _when write valid object list then file success written")
    public void testWriteToFile_whenWriteValidObjectList_thenFileSuccessWritten() {
        final String fileName = "filename.csv";
        final String filePath = tempDir.getPath() + SEPARATOR + fileName;
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        List<Person> persons = personGenerator.getPersonsList(100);
        writer.writeToFile(persons, filePath);

        final File csvFile = new File(filePath);
        Assertions.assertTrue(csvFile.exists());
        Assertions.assertTrue(csvFile.length() > 0);
    }

    @Test
    @DisplayName("Test writeToFile _when not set CsvConverter for CsvWriter then file success written")
    public void testWriteToFile_whenSetCsvConverterIsNull_thenFileSuccessWritten() {
        final String fileName = "filename.csv";
        final String filePath = tempDir.getPath() + SEPARATOR + fileName;
        final Writable writer = new CsvWriter(null);

        writer.writeToFile(List.of(studentsGenerator.getStudentsList(10)), filePath);

        final File csvFile = new File(filePath);
        Assertions.assertTrue(csvFile.exists());
        Assertions.assertTrue(csvFile.length() > 0);
    }

    @Test
    @DisplayName("Test writeToFile when fileName is null then throws IllegalArgumentException")
    public void testWriteToFile_whenFileNameIsNull_thenThrowsIllegalArgumentException() {
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(studentsGenerator.getStudentsList(3), null));
    }

    @Test
    @DisplayName("Test writeToFile when fileName is empty then throws IllegalArgumentException")
    public void testWriteToFile_whenFileNameIsEmpty_thenThrowsIllegalArgumentException() {
        final String fileName = "";
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(studentsGenerator.getStudentsList(3), fileName));
    }

    @Test
    @DisplayName("Test writeToFile when fileName not contains extension then throws IllegalArgumentException")
    public void testWriteToFile_whenFileNameWithoutExtension_thenThrowsIllegalArgumentException() {
        final String fileName = "filename";
        final String filePath = tempDir.getPath() + SEPARATOR + fileName;
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(studentsGenerator.getStudentsList(3), filePath));
    }

    @Test
    @DisplayName("Test writeToFile when fileName contains only extension then throws IllegalArgumentException")
    public void testWriteToFile_whenFileNameContainsOnlyExtension_thenThrowsIllegalArgumentException() {
        final String fileName = ".csv";
        final String filePath = tempDir.getPath() + SEPARATOR + fileName;
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(studentsGenerator.getStudentsList(3), filePath));
    }

    @Test
    @DisplayName("Test writeToFile when fileName contains only directory then throws IllegalArgumentException")
    public void testWriteToFile_whenFileNameContainsOnlyDirectory_thenThrowsIllegalArgumentException() {
        final String filePath = tempDir.getPath() + SEPARATOR;
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(studentsGenerator.getStudentsList(3), filePath));
    }

    @Test
    @DisplayName("Test writeToFile when data is empty then throws IllegalArgumentException")
    public void testWriteToFile_whenDataIsEmpty_thenThrowsIllegalArgumentException() {
        final String fileName = "filename.csv";
        final String filePath = tempDir.getPath() + SEPARATOR + fileName;
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(Collections.emptyList(), filePath));
    }

    @Test
    @DisplayName("Test writeToFile when data is null then throws IllegalArgumentException")
    public void testWriteToFile_whenDataIsNull_thenThrowsIllegalArgumentException() {
        final String fileName = "filename.csv";
        final String filePath = tempDir.getPath() + SEPARATOR + fileName;
        final Writable writer = new CsvWriter(new SimpleCsvConverter());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> writer.writeToFile(null, filePath));
    }



}
