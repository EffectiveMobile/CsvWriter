package org.writer.util;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Утилитный класс для генерации тестовых данных с использованием библиотеки {@link Faker}.
 * <p>
 * Предоставляет методы для создания коллекций объектов {@link Student} и {@link Person}
 * с заполненными случайными значениями полями. Используется для тестирования
 * и демонстрации функциональности.
 * </p>
 *
 * @author ZhelnovachevRoman
 */
public class DataGenerator {

    private static final Faker faker = new Faker();

    /**
     * Генерирует список студентов с случайными именами и оценками.
     * <p>
     * Каждый студент получает случайное имя и список из трёх оценок
     * в диапазоне от 1 до 5.
     * </p>
     *
     * @param count количество студентов для генерации.
     * @return список объектов {@link Student} с заполненными полями.
     */
    public static List<Student> generateStudents(int count){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            students.add(Student.builder()
                    .name(faker.name().firstName())
                    .score(List.of(
                            String.valueOf(faker.number().numberBetween(1, 5)),
                            String.valueOf(faker.number().numberBetween(1, 5)),
                            String.valueOf(faker.number().numberBetween(1, 5))
                    ))
                    .build());
        }
        return students;
    }

    /**
     * Генерирует список персон с случайными ФИО и датами рождения.
     * <p>
     * Каждая персона получает случайное имя, фамилию, день рождения (1-28),
     * месяц рождения (из перечисления {@link Months}) и год рождения (1900-2025).
     * </p>
     *
     * @param count количество персон для генерации.
     * @return список объектов {@link Person} с заполненными полями.
     */
    public static List<Person> generatePersons(int count){
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(Person.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                            .dayOfBirth(faker.number().numberBetween(1, 28))
                            .monthOfBirth(faker.options().option(Months.class))
                            .yearOfBirth(faker.number().numberBetween(1900, 2025))
                    .build());
        }
        return persons;
    }
}
