package org.writer.util.data;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.writer.model.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class for generating sample employee data.
 */
@UtilityClass
public class EmployeeDataUtil {
    private static final Faker FAKER = new Faker();
    private static final List<String> DEPARTMENTS = List.of("Engineering", "Marketing", "Sales", "HR", "Finance", "IT");
    private static final BigDecimal MIN_SALARY = new BigDecimal("50000.00");
    private static final BigDecimal MAX_SALARY = new BigDecimal("120000.00");
    private static final int SALARY_SCALE = 2;

    /**
     * Generates a list of employees with random data.
     *
     * @param count the number of employees to generate
     * @return a list of {@link Employee} objects
     */
    public static List<Employee> getEmployees(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> Employee.builder()
                        .department(generateDepartment())
                        .salary(generateSalary())
                        .build())
                .toList();
    }

    /**
     * Generates a random salary within the predefined range.
     *
     * @return a random salary as {@link BigDecimal}
     */
    private static BigDecimal generateSalary() {
        return BigDecimal.valueOf(FAKER.number()
                .randomDouble(SALARY_SCALE, MIN_SALARY.intValue(), MAX_SALARY.intValue()));
    }

    /**
     * Selects a random department from the predefined list.
     *
     * @return a department name as a string
     */
    private static String generateDepartment() {
        return FAKER.options()
                .nextElement(DEPARTMENTS);
    }
}