package org.writer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class WritableImplTest {

    @TempDir
    Path tempDir;

    @Test
    void simpleWriteToFile() throws IOException {
        WritableImpl writable = new WritableImpl();
        String fileName = "CorrectFileName";
        Path csvFile = tempDir.resolve(fileName);
        List<Student> students = List.of(
                new Student("Student 1", List.of("5", "4", "3", "2")),
                new Student("Student 2", List.of("2", "1", "1", "2")),
                new Student("Student 3", List.of("2", "3", "4", "5"))
        );

        writable.writeToFile(students, csvFile.toString());

        assertAll(
                () -> assertTrue(Files.exists(csvFile), "Файл должен существовать"),
                () -> assertTrue(Files.isRegularFile(csvFile), "Должен быть файл, а не директория"),
                () -> assertTrue(Files.size(csvFile) > 0, "Файл не должен быть пустым")
        );

        List<String> actual = Files.readAllLines(csvFile);
        List<String> expected = List.of(
                "name,score",
                "Student 1,\"5, 4, 3, 2\"",
                "Student 2,\"2, 1, 1, 2\"",
                "Student 3,\"2, 3, 4, 5\""
        );
        assertEquals(expected, actual);
    }

    @Test
    void dataHasDoubleQuotes() throws IOException {
        WritableImpl writable = new WritableImpl();
        String fileName = "CorrectFileName";
        Path csvFile = tempDir.resolve(fileName);
        List<Student> students = List.of(
                new Student("St\"ude\"nt 1", List.of("5", "4", "3", "2")),
                new Student("S\"tud\"ent 2", List.of("2", "1", "1", "2")),
                new Student("\"Stu\"dent 3", List.of("2", "3", "4", "5"))
        );

        writable.writeToFile(students, csvFile.toString());

        assertAll(
                () -> assertTrue(Files.exists(csvFile), "Файл должен существовать"),
                () -> assertTrue(Files.isRegularFile(csvFile), "Должен быть файл, а не директория"),
                () -> assertTrue(Files.size(csvFile) > 0, "Файл не должен быть пустым")
        );

        List<String> actual = Files.readAllLines(csvFile);
        List<String> expected = List.of(
                "name,score",
                "\"St\"\"ude\"\"nt 1\",\"5, 4, 3, 2\"",
                "\"S\"\"tud\"\"ent 2\",\"2, 1, 1, 2\"",
                "\"\"\"Stu\"\"dent 3\",\"2, 3, 4, 5\""
        );
        assertEquals(expected, actual);
    }

    @Test
    void dataHasComma() throws IOException {
        WritableImpl writable = new WritableImpl();
        String fileName = "CorrectFileName";
        Path csvFile = tempDir.resolve(fileName);
        List<Student> students = List.of(
                new Student("St,udent 1", List.of("5", "4", "3", "2")),
                new Student("Stu,dent 2", List.of("2", "1", "1", "2")),
                new Student("Stud,ent 3", List.of("2", "3", "4", "5"))
        );

        writable.writeToFile(students, csvFile.toString());

        assertAll(
                () -> assertTrue(Files.exists(csvFile), "Файл должен существовать"),
                () -> assertTrue(Files.isRegularFile(csvFile), "Должен быть файл, а не директория"),
                () -> assertTrue(Files.size(csvFile) > 0, "Файл не должен быть пустым")
        );

        List<String> actual = Files.readAllLines(csvFile);
        List<String> expected = List.of(
                "name,score",
                "\"St,udent 1\",\"5, 4, 3, 2\"",
                "\"Stu,dent 2\",\"2, 1, 1, 2\"",
                "\"Stud,ent 3\",\"2, 3, 4, 5\""
        );
        assertEquals(expected, actual);
    }

    @Test
    void dataHasEmptyCollection() throws IOException {
        WritableImpl writable = new WritableImpl();
        String fileName = "CorrectFileName";
        Path csvFile = tempDir.resolve(fileName);
        List<Student> students = List.of(
                new Student("Student 1", Collections.emptyList()),
                new Student("Student 2", Collections.emptyList()),
                new Student("Student 3", Collections.emptyList())
        );

        writable.writeToFile(students, csvFile.toString());

        assertAll(
                () -> assertTrue(Files.exists(csvFile), "Файл должен существовать"),
                () -> assertTrue(Files.isRegularFile(csvFile), "Должен быть файл, а не директория"),
                () -> assertTrue(Files.size(csvFile) > 0, "Файл не должен быть пустым")
        );

        List<String> actual = Files.readAllLines(csvFile);
        List<String> expected = List.of(
                "name,score",
                "Student 1,",
                "Student 2,",
                "Student 3,"
        );
        assertEquals(expected, actual);
    }
}