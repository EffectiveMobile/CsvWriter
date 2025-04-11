package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CsvWriterImpl csvWriter = new CsvWriterImpl();
        Faker faker = new Faker();

        // Пример 1: Список Person
        List<Person> people = List.of(
                new Person("Alice", "Smith", 20, Months.FEBRUARY, 1995),
                new Person(faker.name().firstName(), faker.name().lastName(), faker.number().numberBetween(1, 31), Months.JUNE, 2000)
        );
        csvWriter.writeToFile(people, "people.csv");

        // Пример 2: Список Student
        List<Student> students = List.of(
                new Student("Bob", List.of("A", "B", "C")),
                new Student(faker.name().firstName(), List.of(faker.letterify("?"), faker.letterify("?")))
        );
        csvWriter.writeToFile(students, "students.csv");

        System.out.println("CSV files generated (check logs if errors occurred)!");
    }
}