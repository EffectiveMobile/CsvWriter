package UnitTests;


import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.WritableImpl;
import org.writer.exception.InvalidDataException;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CSVWriterTest {
    private final Faker faker = new Faker();
    private final  WritableImpl writable = new WritableImpl();
    private final  List<Person> persons = new ArrayList<>();
    private final  List<Student> students = new ArrayList<>();
    private final String fileName = "output.csv";


    @BeforeEach
    void setUp() {
        for (int i = 0; i < 5; i++) {
            persons.add(new Person(faker.name().firstName(), faker.name().lastName(), 22, Months.APRIL, 1094));
        }
        persons.add(new Person("Egor", "Fillipov", 22, Months.APRIL, 1094));


        for (int i = 0; i < 5; i++) {
            students.add(new Student(faker.name().firstName(), List.of(faker.size().adjective(), faker.size().adjective())));
        }
        students.add(new Student("Egor", List.of("129", "200")));
    }

    @AfterEach
    void tearDown() {
        persons.clear();
        students.clear();
    }


    @Test
    void validPersonsWriteTest() throws IOException {
        writable.writeToFile(persons, fileName);
        Assertions.assertDoesNotThrow(() -> {});
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String s = bufferedReader.readLine();
        Assertions.assertNotNull(s);
        Assertions.assertTrue(s.contains("Egor"));
    }

    @Test
    void validStudentsWriteTest() throws IOException {
        writable.writeToFile(students, fileName);
        Assertions.assertDoesNotThrow(() -> {});
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String s = bufferedReader.readLine();
        Assertions.assertNotNull(s);
        Assertions.assertTrue(s.contains("Egor"));
    }

    @Test
    void invalidPersonsWriteTest() {
        Assertions.assertThrows(InvalidDataException.class, () -> writable.writeToFile(null, fileName));
    }

    @Test
    void invalidStudentsWriteTest() {
        Assertions.assertThrows(InvalidDataException.class, () -> writable.writeToFile(null, fileName));
    }

    @Test
    void invalidFileNameTest() {
        Assertions.assertThrows(InvalidDataException.class, () -> writable.writeToFile(persons, null));
    }
}
