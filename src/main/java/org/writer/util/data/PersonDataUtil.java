package org.writer.util.data;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.writer.model.Person;
import org.writer.model.enums.Months;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class for generating sample person data.
 */
@UtilityClass
public class PersonDataUtil {
    private static final Faker FAKER = new Faker();
    private static final List<Months> MONTHS = List.of(Months.values());
    private static final int MIN_BIRTH_YEAR = 1970;
    private static final int MAX_BIRTH_YEAR = 2005;
    private static final int MIN_BIRTH_DAY = 1;
    private static final int MAX_BIRTH_DAY = 28;

    /**
     * Generates a list of people with random data.
     *
     * @param count the number of people to generate
     * @return a list of {@link Person} objects
     */
    public static List<Person> getPeople(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Person.builder()
                        .firstName(FAKER.name().firstName())
                        .lastName(FAKER.name().lastName())
                        .dayOfBirth(FAKER.number().numberBetween(MIN_BIRTH_DAY, MAX_BIRTH_DAY + 1))
                        .monthOfBirth(generateMonth())
                        .yearOfBirth(FAKER.number().numberBetween(MIN_BIRTH_YEAR, MAX_BIRTH_YEAR + 1))
                        .build())
                .toList();
    }

    /**
     * Selects a random birth month.
     *
     * @return a randomly selected {@link Months} enum value
     */
    private static Months generateMonth() {
        return FAKER.options()
                .nextElement(MONTHS);
    }
}