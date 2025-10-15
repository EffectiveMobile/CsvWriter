package org.writer.writeble;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.writeble.util.DataFakerCreator;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WritableSCVImplTest {

    private final Writable writable = new WritableSCVImpl();
    private final DataFakerCreator creator = new DataFakerCreator();
    private final String testFile = "test.csv";

    @AfterEach
    void cleanup() {
        File file = new File(testFile);
        if (file.exists()) file.delete();
    }

    @Test
    void testWriteStudentsToFile() {
        List<Student> students = creator.datafakerStudents();
        writable.writeToFile(students, testFile);
        File file = new File(testFile);
        assertTrue(file.exists(), "Файл должен быть создан");
        assertTrue(file.length() > 0, "Файл не должен быть пустым");
    }

    @Test
    void testWritePersonsToFile() {
        List<Person> persons = creator.datafakerPersons();
        writable.writeToFile(persons, testFile);
        File file = new File(testFile);
        assertTrue(file.exists(), "Файл должен быть создан");
        assertTrue(file.length() > 0, "Файл не должен быть пустым");
    }
}
