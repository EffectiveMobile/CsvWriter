package org.writer;

import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.CsvWriter;
import org.writer.service.Writable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Тестовый класс для проверки работы {@link CsvWriter}.
 * Содержит тесты для записи объектов {@link Person} и {@link Student} в CSV-файл.
 *
 * @author Мельников Никита
 */
public class CsvWriterTest {

    /**
     * Тест для проверки записи списка объектов {@link Person} в CSV-файл.
     * Проверяет, что заголовок и данные записаны корректно.
     */
    @Test
    public void testCsvWriterPeopleWrite(){
        List<Person> people = Arrays.asList(
                Person.builder()
                        .firstName("Anton")
                        .lastName("Antonov")
                        .dayOfBirth(13)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1993)
                        .build(),
                Person.builder()
                        .firstName("Boris")
                        .lastName("Borisov")
                        .dayOfBirth(13)
                        .monthOfBirth(Months.SEPTEMBER)
                        .yearOfBirth(2002)
                        .build()
        );

        Writable csvWriter = new CsvWriter();
        csvWriter.writeToFile(people, "src/test/resources/test-people");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-people.csv"))) {
            String header = reader.readLine();
            assertEquals("firstName,lastName,dayOfBirth,monthOfBirth,yearOfBirth", header);

            String row1 = reader.readLine();
            assertEquals("Anton,Antonov,13,JANUARY,1993", row1);

            String row2 = reader.readLine();
            assertEquals("Boris,Borisov,13,SEPTEMBER,2002", row2);
        }

        catch (Exception e) {
            fail("Failed to read file: " + e.getMessage());
        }
    }

    /**
     * Тест для проверки записи списка объектов {@link Student} в CSV-файл.
     * Проверяет, что заголовок и данные записаны корректно, включая обработку списков.
     */
    @Test
    public void testCsvWriterStudentsWrite(){
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Jora Ivanov")
                        .score(List.of("3", "4", "5"))
                        .build(),
                Student.builder()
                        .name("Magomed Magomedov")
                        .score(List.of("5", "5", "5"))
                        .build()
        );

        Writable csvWriter = new CsvWriter();
        csvWriter.writeToFile(students, "src/test/resources/test-students");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/test-students.csv"))) {
            String customHeader = reader.readLine();
            assertEquals("Full name,List of scores", customHeader);

            String row1 = reader.readLine();
            assertEquals("Jora Ivanov,3;4;5", row1);

            String row2 = reader.readLine();
            assertEquals("Magomed Magomedov,5;5;5", row2);
        }

        catch (Exception e) {
            fail("Failed to read file: " + e.getMessage());
        }
    }
}
