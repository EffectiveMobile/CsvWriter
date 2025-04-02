package test.java;


import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.writer.WritableImpl;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;



public class WritableTest {
    private WritableImpl csvWriter = new WritableImpl();
    private final String testFilePath = "peopleTest.csv";

    @BeforeEach
    public void setUp() {
        csvWriter = new WritableImpl();
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Удаляем файл после каждого теста, если он существует
        Files.deleteIfExists(Paths.get(testFilePath));
    }

    @Test
    public void testWriteToCsv_EmptyList() throws IOException {
        // Проверяем, что выбрасывается исключение при попытке создать файл, когда список пуст
        assertThrows(IllegalArgumentException.class, () -> {
            csvWriter.writeToFile(Arrays.asList(), testFilePath);
        });
    }

    @Test
    public void testWriteToCsv_SinglePerson() throws IOException {
        Person person = new Person("John Doe", "John Doe",1, Months.APRIL, 2);

        csvWriter.writeToFile(Arrays.asList(person), testFilePath);

        String content = new String(Files.readAllBytes(Paths.get(testFilePath)));

        assertTrue("Данные человека должны быть записаны", content.contains("John Doe;John Doe;1;APRIL;2;"));

    }

    @Test
    public void testWriteToCsv_MultiplePersons() throws IOException {
        Person person1 = new Person("John Doe", "John Doe",1, Months.APRIL, 2);
        Person person2 = new Person("John DD", "John Doe",2, Months.APRIL, 2);
        csvWriter.writeToFile(Arrays.asList(person1, person2), testFilePath);

        String content = new String(Files.readAllBytes(Paths.get(testFilePath)));
        System.out.println(content);
        assertTrue("Данные первого человека должны быть записаны", content.contains("John Doe;John Doe;1;APRIL;2;"));
        assertTrue("Данные второго человека должны быть записаны", content.contains("John DD;John Doe;2;APRIL;2;"));
    }

    @Test
    public void testWriteToCsv_InvalidFilePath() {
        // Проверяем, что выбрасывается исключение при попытке записи в недопустимый путь
        assertThrows(IOException.class, () -> {
            csvWriter.writeToFile(Arrays.asList(new Person("John Doe", "John Doe",1, Months.APRIL, 2)), "/invalid/path/people.csv");
        });
    }
}