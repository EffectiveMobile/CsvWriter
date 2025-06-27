package org.writer;

import org.writer.csv.SimpleCsvConverter;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Student student = Student.builder()
                .name("Ivan")
                .score(List.of("5", "3", "4", "4"))
                .build();

        Student student1 = Student.builder()
                .name("Asya")
                .score(List.of("111", "222", "333"))
                .build();

        Student student2 = Student.builder()
                .name("Senya")
                .build();
        List<?> students = List.of(student, student1, student2);

        SimpleCsvConverter csvConverter = new SimpleCsvConverter();
        csvConverter.setConvertCollectionAsMultipleCell(true);
        Writable csvWriter = new CsvWriter(csvConverter);
        csvWriter.writeToFile(students, "students.csv");

        Person person1 = Person.builder()
                .firstName("Сергей")
                .lastName("Петров")
                .dayOfBirth(12)
                .monthOfBirth(Months.APRIL)
                .yearOfBirth(1995)
                .build();

        Person person2 = Person.builder()
                .firstName("Тихон")
                .lastName("Иванов")
                .dayOfBirth(11)
                .monthOfBirth(Months.APRIL)
                .yearOfBirth(1995)
                .build();

        Person person3 = Person.builder()
                .firstName("Николай")
                .dayOfBirth(11)
                .monthOfBirth(Months.APRIL)
                .yearOfBirth(1995)
                .build();

        List<?> persons = List.of(person1, person2, person3);
        Writable personWriter = new CsvWriter(new SimpleCsvConverter());
        personWriter.writeToFile(persons, "persons.csv");

        List<?> strings = List.of("String1", "String2", "Text1");
        new CsvWriter(new SimpleCsvConverter()).writeToFile(strings, "Strings.csv");

        List<?> integers = List.of(55, 47,1230, 33,44,66,77, 100);
        new CsvWriter(new SimpleCsvConverter()).writeToFile(integers, "integers.csv");


    }
}