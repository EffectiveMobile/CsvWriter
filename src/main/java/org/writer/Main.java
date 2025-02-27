package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        // Создаем объект Faker для генерации случайных данных
        Faker faker = new Faker(new Locale("ru"));

        // Генерация списка людей (Person) с использованием DataFaker
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // Генерируем 5 случайных людей
            Person person = Person.builder()
                    .firstName(faker.name().firstName()) // Случайное имя
                    .lastName(faker.name().lastName())   // Случайная фамилия
                    .dayOfBirth(faker.number().numberBetween(1, 28)) // Случайный день рождения (1-28)
                    .monthOfBirth(faker.options().option(Months.values())) // Случайный месяц
                    .yearOfBirth(faker.number().numberBetween(1970, 2000)) // Случайный год рождения (1970-2000)
                    .build();
            people.add(person);
        }

        // Генерация списка студентов (Student) с использованием DataFaker
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // Генерируем 5 случайных студентов
            Student student = Student.builder()
                    .name(faker.name().fullName()) // Случайное полное имя
                    .score(Arrays.asList(
                            faker.options().option("A", "B", "C", "D", "F"), // Случайная оценка
                            faker.options().option("A", "B", "C", "D", "F"),
                            faker.options().option("A", "B", "C", "D", "F")
                    ))
                    .build();
            students.add(student);
        }

        // Создаем экземпляр WritableImpl для записи данных в CSV
        WritableImpl writable = new WritableImpl();
        writable.writeToFile(people, "people.csv");
        writable.writeToFile(students, "students.csv");

        System.out.println("CSV файлы успешно созданы");
    }

}