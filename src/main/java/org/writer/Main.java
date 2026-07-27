package org.writer;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.CsvWriter;

public class Main {

  public static void main(String[] args) {
    CsvWriter writer = new CsvWriter();
    Faker faker = new Faker();
    int lower = 65;
    int upper = 90;
    Random random = new Random();
    Months[] months = Months.values();

    List<Student> students = Stream.generate(() -> new Student(
            faker.name().firstName(),
            Stream.generate(() -> Character.toString((char) random.nextInt(lower, upper)))
                .limit(random.nextInt(0, 5)).toList()))
        .limit(5).toList();

    writer.writeToFile(students, "students");

    List<Person> persons = Stream.generate(() -> new Person(
            faker.name().firstName(),
            faker.name().lastName(),
            random.nextInt(1, 31),
            months[random.nextInt(months.length)],
            faker.timeAndDate().birthday(18, 35).getYear()))
        .limit(5).toList();

    writer.writeToFile(persons, "persons.csv");


  }
}