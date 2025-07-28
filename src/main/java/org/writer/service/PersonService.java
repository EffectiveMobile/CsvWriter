package org.writer.service;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.writer.model.Months;
import org.writer.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Сервис для создания списка объектов типа {@link Person}.
 */
@AllArgsConstructor
public class PersonService {
  private final Faker faker;
  private final Random random;

  /**
   * Создает список из 10 случайных объектов {@link Person}.
   * @return список созданных людей
   */
  public List<Person> creteListPerson(){
    List<Person> persons = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      int randomMonths = random.nextInt(1,12);
      persons.add(Person.builder()
          .firstName(faker.name().firstName())
          .lastName(faker.name().lastName())
          .dayOfBirth(random.nextInt(countDaysInMonths(randomMonths)))
          .monthOfBirth(Months.values()[randomMonths - 1])
          .yearOfBirth(random.nextInt(1980, 2007)).build());
    }
    return persons;
  }

  /**
   * Подсчитывает количество дней в заданном месяце.
   * @param randomMonths номер месяца (от 1 до 12)
   * @return количество дней в месяце
   */
  private int countDaysInMonths(int randomMonths){
        return switch (randomMonths) {
          case 1, 3, 5, 7, 8, 10, 12  -> 31;
          case 4, 6, 9, 11 -> 30;
          case 2 -> 28;
          default -> 0;
        };
    }
}
