package org.writer;


import com.github.javafaker.Faker;
import org.writer.model.*;
import org.writer.utils.writers.Writable;
import org.writer.utils.writers.csvwriter.CSVWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        Faker faker = new Faker(Locale.ENGLISH);

        UUID id = UUID.randomUUID();
        String fullname = faker.name().fullName();
        LocalDate date = faker.date().birthday(0, 10).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        Employee employee = new Employee(id, fullname, date);
        List<Employee> employees = List.of(employee);

        Writable<Employee> writerE = new CSVWriter<>();

        writerE.writeToFile(employees, "src\\main\\resources" +
                "\\reportEmployee.csv");

        Product product = new Product(UUID.randomUUID(), faker.company().name(),
                Double.parseDouble(faker.commerce().price().replace(',', '.')));
        List<Product> products = List.of(product);

        Writable<Product> writerP = new CSVWriter<>();
        writerP.writeToFile(products, "src\\main\\resources" +
                "\\reportProduct");

        List<Person> persons = List.of(Person.builder()
                .firstName("Pasha")
                .lastName("Barabaska")
                .dayOfBirth(2)
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(1992)
                .build());

        Writable<Person> writerPerson = new CSVWriter<>();
        writerPerson.writeToFile(persons, "src\\main\\resources" +
                "\\reportPersons");

        List<Student> students = List.of(Student.builder()
                .name("bacugan")
                .score(List.of("qwerty", "wasd", "12345")).build());
        Writable<Student> writerStudent = new CSVWriter<>();
        writerStudent.writeToFile(students,  "src\\main\\resources" +
                "\\reportStudents");
    }
}