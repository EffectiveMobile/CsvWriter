package org.writer.model.generators;

import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Additional class for quickly creating Student class objects.
 *
 * @ClassName StudentsGenerator
 * @Author Alexei Shvariov
 * @Date 27.06.2025
 * @Version 1.0
 */

@AllArgsConstructor
public class StudentsGenerator {
    private final Faker faker;

    /**Create object of {@link Student} class  with use DataFaker library
     * @return object of {@link Student} class*/
    public Student getStudent() {
        return Student.builder()
                .name(faker.name().fullName())
                .score(faker.collection(() -> String.valueOf(faker.number().numberBetween(1, 100)))
                        .maxLen(5)
                        .build().get())
                .build();
    }

    /**Create Student class object's collection with use DataFaker library
     * @return object of {@link Student} class
     * @param size - collection size */
    public List<Student> getStudentsList(int size) {
        final List<Student> students = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            students.add(getStudent());
        }
        return students;
    }


}
