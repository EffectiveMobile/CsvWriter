package org.writer;

import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WritableImplTest {

    @Test
    void testWritePersonsToFile(){
        List<Person> people = new ArrayList<>(Arrays.asList(
                Person.builder().firstName("John").lastName("Doe").dayOfBirth(1).monthOfBirth(Months.JANUARY).yearOfBirth(1990).build(),
                Person.builder().firstName("Jane").lastName("Smith").dayOfBirth(15).monthOfBirth(Months.MARCH).yearOfBirth(1985).build()
        ));
        WritableImpl writable = new WritableImpl();
        writable.writeToFile(people,"test_people.csv");

        //Проверка, что файл был создан
        assertTrue(new java.io.File("test_people.csv").exists());
    }

    @Test
    void testWriteStudentsToFile(){
        List<Student>students = new ArrayList<>(Arrays.asList(
                Student.builder().name("Alice").score(Arrays.asList("A","B","C")).build(),
                Student.builder().name("Bob").score(Arrays.asList("B","A","A")).build()
        ));
        WritableImpl writable = new WritableImpl();
        writable.writeToFile(students,"test_students.csv");

        //Проверка, что файл был создан
        assertTrue(new java.io.File("test_students.csv").exists());
    }

    @Test
    void testWriteEmptyList(){
        List<Object> emptyList = Arrays.asList();
        WritableImpl writable = new WritableImpl();
        assertThrows(IllegalArgumentException.class, ()-> writable.writeToFile(emptyList,"test_empty.csv"));
    }
}
