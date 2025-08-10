package org.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class CsvWriterTest {
    private static final String TEST_PERSON_FILE = "test_person.csv";
    private static final String TEST_EMPTY_FILE = "test_empty.csv";
    private static final String TEST_NULL_FILE = "test_null.csv";
    private static final String TEST_STUDENT_FILE = "test_student.csv";
    private static final String TEST_EMPTY_SCORES_FILE = "test_empty_scores.csv";
    private static final String TEST_NULL_SCORES_FILE = "test_null_scores.csv";

    private Writable csvWriter;

    @BeforeEach
    public void setUp() {
        csvWriter = new CsvWriter();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_PERSON_FILE));
        Files.deleteIfExists(Paths.get(TEST_EMPTY_FILE));
        Files.deleteIfExists(Paths.get(TEST_NULL_FILE));
        Files.deleteIfExists(Paths.get(TEST_STUDENT_FILE));
        Files.deleteIfExists(Paths.get(TEST_EMPTY_SCORES_FILE));
        Files.deleteIfExists(Paths.get(TEST_NULL_SCORES_FILE));
    }

    @Test
    public void testWritePersonCsv() throws IOException {
        List<Person> people = List.of(
                new Person("John", "Doe", 1, Months.JANUARY, 1980),
                new Person("Jane", "Smith", 15, Months.APRIL, 1990)
        );

        csvWriter.writeToFile(people, TEST_PERSON_FILE);

        String content = Files.readString(Paths.get(TEST_PERSON_FILE));
        String[] lines = content.split(System.lineSeparator());

        // Check headers
        assertTrue(lines[0].contains("First Name"));
        assertTrue(lines[0].contains("Last Name"));
        assertTrue(lines[0].contains("Day"));
        assertTrue(lines[0].contains("Month"));
        assertTrue(lines[0].contains("Year"));

        // Check data
        assertTrue(lines[1].contains("John"));
        assertTrue(lines[1].contains("Doe"));
        assertTrue(lines[1].contains("1"));
        assertTrue(lines[1].contains("JANUARY"));
        assertTrue(lines[1].contains("1980"));

        assertTrue(lines[2].contains("Jane"));
        assertTrue(lines[2].contains("Smith"));
        assertTrue(lines[2].contains("15"));
        assertTrue(lines[2].contains("APRIL"));
        assertTrue(lines[2].contains("1990"));
    }

    @Test
    public void testWriteEmptyList() throws IOException {
        List<Person> emptyList = List.of();
        csvWriter.writeToFile(emptyList, TEST_EMPTY_FILE);

        String content = Files.readString(Paths.get(TEST_EMPTY_FILE));
        assertTrue(content.contains("First Name")); // Only headers should be present
        assertEquals(1, content.split(System.lineSeparator()).length);
    }

    @Test
    public void testWriteNullList() {
        assertThrows(IllegalArgumentException.class,
                () -> csvWriter.writeToFile(null, TEST_NULL_FILE));
    }

    @Test
    public void testWriteLargeDataset() throws IOException {
        List<Person> largeDataset = IntStream.range(0, 1000)
                .mapToObj(i -> new Person("Name" + i, "Surname" + i, i % 30, Months.values()[i % 12], 1970 + i % 30))
                .collect(Collectors.toList());

        csvWriter.writeToFile(largeDataset, TEST_PERSON_FILE);

        List<String> lines = Files.readAllLines(Paths.get(TEST_PERSON_FILE));
        assertEquals(1001, lines.size()); // 1000 records + header
        assertTrue(lines.get(1).contains("Name0"));
        assertTrue(lines.get(1000).contains("Name999"));
    }

    @Test
    public void testFileCreation() throws IOException {
        List<Person> people = List.of(new Person("Test", "User", 1, Months.JANUARY, 2000));
        csvWriter.writeToFile(people, TEST_PERSON_FILE);

        assertTrue(Files.exists(Paths.get(TEST_PERSON_FILE)));
        assertTrue(Files.size(Paths.get(TEST_PERSON_FILE)) > 0);
    }

    @Test
    public void testSpecialCharactersInData() throws IOException {
        List<Person> people = List.of(
                new Person("John,", "Doe\"", 1, Months.JANUARY, 1980),
                new Person("Jane\n", "Smith\r", 2, Months.FEBRUARY, 1990)
        );

        csvWriter.writeToFile(people, TEST_PERSON_FILE);

        String content = Files.readString(Paths.get(TEST_PERSON_FILE));
        assertTrue(content.contains("\"John,\""));
        assertTrue(content.contains("\"Doe\"\"\""));
        assertTrue(content.contains("\"Jane\n\""));
        assertTrue(content.contains("\"Smith\r\""));
    }

    @Test
    public void testWriteStudentWithScores() throws IOException {
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Alice")
                        .score(Arrays.asList("A", "B+", "A-"))
                        .build(),
                Student.builder()
                        .name("Bob")
                        .score(Arrays.asList("B", "C", "A+"))
                        .build()
        );

        csvWriter.writeToFile(students, TEST_STUDENT_FILE);

        String content = Files.readString(Paths.get(TEST_STUDENT_FILE));
        String[] lines = content.split(System.lineSeparator());

        assertEquals("\"Name\",\"Score\"", lines[0]);

        assertTrue(lines[1].contains("\"Alice\""));
        assertTrue(lines[1].contains("\"[A, B+, A-]\""));

        assertTrue(lines[2].contains("\"Bob\""));
        assertTrue(lines[2].contains("\"[B, C, A+]\""));
    }

    @Test
    public void testWriteStudentWithEmptyScores() throws IOException {
        List<Student> students = Collections.singletonList(
                Student.builder()
                        .name("Charlie")
                        .score(Collections.emptyList())
                        .build()
        );

        csvWriter.writeToFile(students, TEST_EMPTY_SCORES_FILE);

        String content = Files.readString(Paths.get(TEST_EMPTY_SCORES_FILE));
        String[] lines = content.split(System.lineSeparator());

        assertEquals("\"Name\",\"Score\"", lines[0]);
        assertTrue(lines[1].contains("\"Charlie\""));
        assertTrue(lines[1].contains("\"[]\""));
    }

    @Test
    public void testWriteStudentWithNullScores() throws IOException {
        List<Student> students = Collections.singletonList(
                Student.builder()
                        .name("David")
                        .score(null)
                        .build()
        );

        csvWriter.writeToFile(students, TEST_NULL_SCORES_FILE);

        String content = Files.readString(Paths.get(TEST_NULL_SCORES_FILE));
        String[] lines = content.split(System.lineSeparator());

        assertEquals("\"Name\",\"Score\"", lines[0]);
        assertTrue(lines[1].contains("\"David\""));
        assertTrue(lines[1].contains("null"));
    }

    @Test
    public void testWriteMultipleStudentsWithVariousScores() throws IOException {
        List<Student> students = Arrays.asList(
                Student.builder().name("Eve").score(Arrays.asList("A+", "A+")).build(),
                Student.builder().name("Frank").score(null).build(),
                Student.builder().name("Grace").score(Collections.singletonList("B-")).build(),
                Student.builder().name("Henry").score(Collections.emptyList()).build()
        );

        csvWriter.writeToFile(students, TEST_STUDENT_FILE);

        List<String> lines = Files.readAllLines(Paths.get(TEST_STUDENT_FILE));
        assertEquals(5, lines.size()); // header + 4 students

        assertTrue(lines.get(1).contains("\"Eve\""));
        assertTrue(lines.get(1).contains("\"[A+, A+]\""));

        assertTrue(lines.get(2).contains("\"Frank\""));
        assertTrue(lines.get(2).contains("null"));

        assertTrue(lines.get(3).contains("\"Grace\""));
        assertTrue(lines.get(3).contains("\"[B-]\""));

        assertTrue(lines.get(4).contains("\"Henry\""));
        assertTrue(lines.get(4).contains("\"[]\""));
    }

    @Test
    public void testWriteStudentWithSpecialCharactersInName() throws IOException {
        List<Student> students = Collections.singletonList(
                Student.builder()
                        .name("John \"The Best\" Doe")
                        .score(Arrays.asList("A", "B"))
                        .build()
        );

        csvWriter.writeToFile(students, TEST_STUDENT_FILE);

        String content = Files.readString(Paths.get(TEST_STUDENT_FILE));
        assertTrue(content.contains("\"John \"\"The Best\"\" Doe\""));
        assertTrue(content.contains("\"[A, B]\""));
    }
}
