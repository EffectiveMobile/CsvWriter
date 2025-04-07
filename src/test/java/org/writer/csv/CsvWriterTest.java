package org.writer.csv;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvWriterTest {

    private final Faker faker = new Faker();

    @Test
    public void testWritePersonCsv() throws IOException {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            people.add(new Person(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.number().numberBetween(1, 28),
                    Months.values()[faker.number().numberBetween(0, 12)],
                    faker.number().numberBetween(1970, 2005)
            ));
        }

        String filePath = "test_person.csv";
        new CsvWriter().writeToFile(people, filePath);

        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        file.delete();
    }

    @Test
    public void testWriteStudentCsv() throws IOException {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            List<String> scores = List.of(
                    String.valueOf(faker.number().numberBetween(60, 100)),
                    String.valueOf(faker.number().numberBetween(60, 100)),
                    String.valueOf(faker.number().numberBetween(60, 100))
            );

            students.add(Student.builder()
                    .name(faker.name().fullName())
                    .score(scores)
                    .build());
        }

        String filePath = "test_student.csv";
        new CsvWriter().writeToFile(students, filePath);

        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        file.delete();
    }

    @Test
    public void testEmptyDataDoesNotCreateFile() throws IOException {
        List<Person> emptyList = new ArrayList<>();
        String filePath = "empty_test.csv";

        new CsvWriter().writeToFile(emptyList, filePath);
        File file = new File(filePath);

        Assertions.assertFalse(file.exists());
    }

    @Test
    public void testInvalidFileNameThrowsException() {
        List<Person> people = List.of(
                new Person("Ivan", "Ivanov", 1, Months.JANUARY, 1990)
        );

        String invalidPath = "invalid:/test.csv";

        Assertions.assertThrows(IOException.class, () -> {
            new CsvWriter().writeToFile(people, invalidPath);
        });
    }
}
