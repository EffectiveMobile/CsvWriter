package org.writer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataGeneratorTest {

    @Test
    @DisplayName("Генерация студентов: должен вернуть список указанного размера")
    void generateStudents_shouldReturnListOfSpecifiedSize() {
        int count = 5;
        List<Student> students = DataGenerator.generateStudents(count);
        assertNotNull(students, "Список не должен быть null");
        assertEquals(count, students.size(), "Размер списка должен соответствовать запрошенному");
    }

    @Test
    @DisplayName("Генерация персон: должен вернуть список с валидными данными")
    void generatePersons_shouldReturnListOfValidData() {
       int count = 3;
        List<Person> persons = DataGenerator.generatePersons(count);
        assertNotNull(persons, "Список не должен быть null");
        assertEquals(count, persons.size(), "Размер списка должен соответствовать запрошенному");
        for (Person person : persons) {
            assertNotNull(person.getFirstName(), "Имя не должно быть null");
            assertNotNull(person.getLastName(), "Фамилия не должна быть null");
            assertNotNull(person.getMonthOfBirth(), "Месяц рождения не должен быть null");
            assertTrue(person.getDayOfBirth() >= 1 && person.getDayOfBirth() <= 28,
                    "День рождения должен быть в диапазоне 1-28");
            assertTrue(person.getYearOfBirth() >= 1900 && person.getYearOfBirth() <= 2025,
                    "Год рождения должен быть в диапазоне 1900-2025");
        }
    }
}
