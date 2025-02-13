package org.writer.util.data;

import lombok.experimental.UtilityClass;
import org.writer.model.CsvModel;
import org.writer.model.Employee;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utility class for generating sample employee data.
 * Provides a static method to retrieve a list of {@link Employee} objects for testing or demonstration purposes.
 */
@UtilityClass
public class EmployeeDataUtil {

    /**
     * Returns a list of sample employee data.
     *
     * @return a list of {@link Employee} objects.
     */
    public static List<CsvModel> getEmployees() {
        return List.of(
                Employee.builder()
                        .id("E1")
                        .department("Engineering")
                        .salary(new BigDecimal("75000.00"))
                        .build(),
                Employee.builder()
                        .id("E2")
                        .department("Marketing")
                        .salary(new BigDecimal("65000.00"))
                        .build(),
                Employee.builder()
                        .id("E3")
                        .department("Sales")
                        .salary(new BigDecimal("70000.00"))
                        .build()
        );
    }
}
