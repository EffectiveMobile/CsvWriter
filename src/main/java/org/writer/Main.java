package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс приложения для генерации тестовых данных Person и Student
 * и записи их в файл с помощью интерфейса Writable.
 */
public class Main {

    private static final Faker faker = new Faker();
    private static final Writable writable = new Writable() {
        @Override
        public void writeToFile(List data) {
            Writable.super.writeToFile(data);
        }
    };

    /**
     * Точка входа в приложение.
     * Генерирует списки Person и Student, затем записывает их в файлы.
     */
    public static void main(String[] args) {
        List<Person> persons = createRecursivePerson(5);
        List<Student> students = createRecursiveStudents(5);
        writable.writeToFile(persons);
        writable.writeToFile(students);
    }

    /**
     * Рекурсивно создает список объектов Person.
     * @param n количество объектов для генерации
     * @return список объектов Person
     */
    public static List<Person> createRecursivePerson(int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        List<Person> objects = createRecursivePerson(n - 1);
        objects.add(createPerson());
        return objects;
    }

    /**
     * Рекурсивно создает список объектов Student.
     * @param n количество объектов для генерации
     * @return список объектов Student
     */
    public static List<Student> createRecursiveStudents(int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        List<Student> students = createRecursiveStudents(n - 1);
        students.add(createStudent());
        return students;
    }

    /**
     * Создает объект Student со случайными данными.
     * @return новый объект Student
     */
    public static Student createStudent() {
        return Student.builder()
                .name(faker.funnyName().name())
                .score(createScore(10))
                .build();
    }

    /**
     * Генерирует список случайных оценок.
     * @param count количество оценок для генерации
     * @return список строковых представлений оценок (0-100000)
     */
    public static List<String> createScore(int count) {
        List<String> score = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            score.add(String.valueOf(faker.random().nextInt(0, 100000)));
        }
        return score;
    }

    /**
     * Создает объект Person со случайными данными.
     * @return новый объект Person
     */
    public static Person createPerson() {
        Months[] arrayEnum = Months.values();
        int numberMonths = faker.random().nextInt(11);
        return Person.builder()
                .firstName(faker.funnyName().name())
                .lastName(faker.funnyName().name())
                .dayOfBirth(dayOfBirth(numberMonths))
                .monthOfBirth(arrayEnum[numberMonths])
                .yearOfBirth(faker.random().nextInt(1960, 2007))
                .build();
    }

    /**
     * Определяет случайный день рождения в зависимости от месяца.
     * @param numberMonths номер месяца (0-11)
     * @return день месяца (1-28/30/31 в зависимости от месяца)
     */
    public static int dayOfBirth(int numberMonths) {
        switch (numberMonths) {
            case 3, 5, 8, 10 -> {
                return faker.random().nextInt(1, 30);
            }
            case 1 -> {
                return faker.random().nextInt(1, 28);
            }
            default -> {
                return faker.random().nextInt(1, 31);
            }
        }
    }
}