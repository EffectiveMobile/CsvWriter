package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.exception.WriterException;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.CsvFileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFileWriterTest {

    private CsvFileWriter csvFileWriter;

    @BeforeEach
    public void init() {
        csvFileWriter = new CsvFileWriter();
    }

    @Test
    public void testWriteToFileForPerson() throws IOException {
        List<Person> students = List.of(
                new Person("Pavel", "Schelkin", 21, Months.JULY, 2004),
                new Person("Roman", "Ivanov", 22, Months.NOVEMBER, 2003)
        );

        Path file = Files.createTempFile("PersonsTest", ".csv");
        csvFileWriter.writeToFile(students, file.toString());

        String result = Files.readString(file);
        assertTrue(result.contains("Name,LastName,DayOfBirth,MonthOfBirth,YearOfBirth"));
        assertTrue(result.contains("Pavel,Schelkin,21,JULY,2004"));
        assertTrue(result.contains("Roman,Ivanov,22,NOVEMBER,2003"));
    }

    @Test
    public void testWriteToFileForStudents() throws IOException {
        List<Student> students = List.of(
                new Student("Pavel", List.of("1")),
                new Student("Roman", List.of("1"))
        );

        Path file = Files.createTempFile("StudentsTest", ".csv");
        csvFileWriter.writeToFile(students, file.toString());

        String result = Files.readString(file);
        assertTrue(result.contains("Name,Score"));
        assertTrue(result.contains("Pavel,[1]"));
        assertTrue(result.contains("Roman,[1]"));
    }

    @Test
    public void testWriteToFileWithNullData() {
        WriterException resultException = assertThrows(WriterException.class,
                () -> csvFileWriter.writeToFile(null, ""));
        assertEquals("Data is null or empty", resultException.getMessage());
    }
}
