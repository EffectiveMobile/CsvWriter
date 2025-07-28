package org.writer.service;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для создания списка объектов типа {@link Student}.
 */
@AllArgsConstructor
public class StudentService {

  private final Faker faker;

  /**
   * Создает список из 10 случайных объектов {@link Student}.
   * @return список созданных студентов
   */
  public List<Student> creteListStudent(){
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      students.add(Student.builder()
          .name(faker.name().name())
          .score(List.of("A", "B", "C"))
          .build());
    }
    return students;
  }
}
