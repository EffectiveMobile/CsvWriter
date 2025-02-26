package org.writer.util.data;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.writer.model.Student;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class for generating sample student data.
 */
@UtilityClass
public class StudentDataUtil {
    private static final Faker FAKER = new Faker();
    private static final int MIN_SCORE = 60;
    private static final int MAX_SCORE = 100;
    private static final int SCORES_COUNT = 3;

    /**
     * Generates a list of students with random data.
     *
     * @param count the number of students to generate
     * @return a list of {@link Student} objects
     */
    public static List<Student> getStudents(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Student.builder()
                        .name(FAKER.name().firstName())
                        .score(generateScores())
                        .build())
                .toList();
    }

    /**
     * Generates a list of random scores for a student.
     *
     * @return a list of scores as strings
     */
    private static List<String> generateScores() {
        return IntStream.range(0, SCORES_COUNT)
                .mapToObj(i -> String.valueOf(FAKER.number()
                        .numberBetween(MIN_SCORE, MAX_SCORE + 1)))
                .toList();
    }
}