package org.writer.writeble.util;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.writer.model.Months.*;

/**
 * Класс для генерации тестовых данных с использованием библиотеки DataFaker.
 * Генерирует списки students и persons с случайными значениями.
 */
public class DataFakerCreator {

    private final Faker faker = new Faker();

    /**
     * Класс для генерации тестовых данных с использованием библиотеки DataFaker.
     * Генерирует списки студентов и персон с случайными значениями.
     */
    public List<Student> datafakerStudents() {

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            students.add(new Student(
                    faker.name().firstName(),
                    List.of(
                            faker.educator().course(),
                            faker.educator().course()
                    )
            ));
        }
        return students;

    }

    public static final List<Months> monthsList = List.of(
            JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE,
            JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    );

    /**
     * Генерирует список из 5 персон с случайными данными.
     *
     * @return список объектов Person
     */
    public List<Person> datafakerPersons() {

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            persons.add(new Person(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.number().numberBetween(1, 12),
                    monthsList.get(faker.number().numberBetween(0, 12)),
                    faker.number().numberBetween(1, 30)
            ));
        }
        return persons;


    }

}
