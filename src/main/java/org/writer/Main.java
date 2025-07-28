package org.writer;

import com.github.javafaker.Faker;
import org.writer.service.PersonService;
import org.writer.service.StudentService;

import java.util.Locale;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

      PersonService personService = new PersonService(new Faker(new Locale("ru")), new Random());
      StudentService studentService = new StudentService(new Faker(new Locale("ru")));

      Writable writer = new CsvWriter();
      writer.writeToFile(personService.creteListPerson(), "persons.csv");
      writer.writeToFile(studentService.creteListStudent(), "students.csv");
    }
}
