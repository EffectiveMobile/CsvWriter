package org.writer.util.data;

import lombok.experimental.UtilityClass;
import org.writer.model.CsvModel;
import org.writer.model.Student;

import java.util.List;

/**
 * Utility class for generating sample student data.
 * Provides a static method to retrieve a list of {@link Student} objects for testing or demonstration purposes.
 */
@UtilityClass
public class StudentDataUtil {

    /**
     * Returns a list of sample student data.
     *
     * @return a list of {@link Student} objects.
     */
    public static List<CsvModel> getStudents() {
        return List.of(
                Student.builder()
                        .id("S1")
                        .name("Anna")
                        .score(List.of("95", "87", "92"))
                        .build(),
                Student.builder()
                        .id("S2")
                        .name("Dmitry")
                        .score(List.of("88", "91", "89"))
                        .build(),
                Student.builder()
                        .id("S3")
                        .name("Ekaterina")
                        .score(List.of("78", "82", "85"))
                        .build()
        );
    }
}
