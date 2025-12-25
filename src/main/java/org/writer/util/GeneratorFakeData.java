package org.writer.util;

import com.github.javafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Генератор случайных данных
 * для сущностей Person и Student
 *
 * @author Sidorin Aleksei
 * @version 1.0
 * @since 16.10.2025
 */
public class GeneratorFakeData {

    private static final Faker faker = new Faker(new Locale("ru"));

    private GeneratorFakeData() {

    }

    /**
     * Генерирует список объектов Person со случайными данными
     *
     * @param count количество генерируемых объектов
     * @return список объектов Person со случайными данными
     * @throws IllegalArgumentException если count меньше или равен 0
     */
    public static List<Person> generatePersons(int count) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(generatePerson());
        }
        return persons;
    }

    private static Person generatePerson() {
        Person person = new Person();
        person.setLastName(faker.name().lastName());
        person.setDayOfBirth(faker.number().numberBetween(1, 28));
        person.setMonthOfBirth(Months.values()[faker.number().numberBetween(0, Months.values().length - 1)]);
        person.setYearOfBirth(faker.number().numberBetween(1950, 2005));
        return person;
    }

    /**
     * Генерирует список объектов Student со случайными данными
     *
     * @param count количество генерируемых объектов
     * @return список объектов Student со случайными данными
     * @throws IllegalArgumentException если count меньше или равен 0
     */
    public static List<Student> generateStudents(int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            students.add(generateStudent());
        }
        return students;
    }

    private static Student generateStudent() {
        Student student = new Student();
        student.setName(faker.name().firstName() + " " + faker.name().lastName());
        student.setScore(generateScores());
        return student;

    }

    /**
     * Генерирует список случайных баллов в диапазоне от 0 до 100
     *
     * @return список строк, содержащих числовые баллы от 0 до 100
     */
    private static List<String> generateScores() {
        List<String> scores = new ArrayList<>();

        int count = 1 + faker.random().nextInt(10);

        for (int i = 0; i < count; i++) {
            int score = faker.random().nextInt(101);
            scores.add(String.valueOf(score));
        }

        return scores;
    }

}
