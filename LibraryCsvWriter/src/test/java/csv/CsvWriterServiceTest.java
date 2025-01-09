package csv;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import writer.Writable;
import writer.csv.CsvWriterService;
import writer.model.Months;
import writer.model.Person;
import writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvWriterServiceTest {
    private Writable csvWriter;
    private static final String STUDENTS_FILE = "students_file_test";
    private static final String PERSON_FILE = "person_file_test";

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriterService();
    }

    @Test
    void writeToCsvStudents_success(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve(STUDENTS_FILE);
        List<Student> students = generateStudent();
        csvWriter.writeToFile(students, filePath.toString());

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(1, lines.size());
        assertEquals("Dodie Schinner,5;4;3", lines.get(0));
    }
    @Test
    void writeToCsvPersons_success(@TempDir Path tempDir) throws IOException {
        Path filePath = tempDir.resolve(PERSON_FILE);
        List<Person> students = generatePerson();

        csvWriter.writeToFile(students, filePath.toString());

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(1, lines.size());
        assertEquals("Dodie,Schinner,4,APRIL,2011", lines.get(0));
    }
    @Test
    void writeToCsv_dataEmpty(){
        List<Student> students = List.of();

        assertThrows(IllegalArgumentException.class, () -> csvWriter.writeToFile(students,"csv_file"));

    }

    private List<Student> generateStudent(){
        return  List.of(
                Student.builder().name("Dodie Schinner").score(Arrays.asList("5", "4", "3")).build()
        );
    }

    private List<Person> generatePerson(){
        return  List.of(
                Person.builder()
                        .firstName("Dodie")
                        .lastName("Schinner")
                        .dayOfBirth(4)
                        .monthOfBirth(Months.APRIL)
                        .yearOfBirth(2011).build());

    }


}