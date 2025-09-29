package org.writer.util;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Вспомогательный класс для генерации тестовых данных
 */
public final class TestDataGenerator {
    private static final Faker FAKER = new Faker();

    private TestDataGenerator() {}

    /**
     * Генерирует случайное int в пределах границ (включительно).
     *
     * @param min нижняя граница.
     * @param max верхняя граница.
     *
     * @return случайное int.
     */
    public static int generateRandomInt(final int min, final int max) {
        return FAKER.number().numberBetween(min, max + 1);
    }

    /**
     * Генерирует список случайных объектов {@link Person}.
     *
     * @param count желаемое количество объектов.
     *
     * @return список сгенерированных объектов {@code Person}.
     */
    public static List<Person> generatePersons(final int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Person.builder()
                        .firstName(FAKER.name().firstName())
                        .lastName(FAKER.name().lastName())
                        .dayOfBirth(FAKER.number().numberBetween(1, 28))
                        .monthOfBirth(getRandomMonth())
                        .yearOfBirth(FAKER.number().numberBetween(1925, 2025))
                        .build())
                .toList();
    }

    /**
     * Генерирует список случайных объектов {@link Student}.
     *
     * @param count желаемое количество объектов.
     *
     * @return список сгенерированных объектов {@code Student}.
     */
    public static List<Student> generateStudents(final int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Student.builder()
                        .name(FAKER.name().fullName())
                        .score(generateRandomScores(FAKER.number().numberBetween(2, 9)))
                        .build())
                .toList();
    }

    /**
     * Вспомогательный метод для получения случайного месяца из enum {@link Months}.
     *
     * @return случайный месяц.
     */
    private static Months getRandomMonth() {
        Months[] months = Months.values();
        return months[FAKER.number().numberBetween(0, months.length - 1)];
    }

    /**
     * Вспомогательный метод для генерации списка случайных оценок.
     *
     * @param count количество оценок.
     *
     * @return список оценок в виде строк.
     */
    private static List<String> generateRandomScores(final int count) {
        return Stream.generate(() -> String.valueOf(FAKER.number().numberBetween(1, 6)))
                .limit(count)
                .toList();
    }
}
