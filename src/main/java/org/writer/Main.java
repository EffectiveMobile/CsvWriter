package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Person person = Person.builder()
                .firstName("Mike")
                .lastName("Vasovski")
                .dayOfBirth(1)
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(2000)
                .build();

        Person person2 = Person.builder()
                .firstName("James")
                .lastName("Sally")
                .dayOfBirth(22)
                .monthOfBirth(Months.NOVEMBER)
                .yearOfBirth(1997)
                .build();

        Student student = Student.builder()
                .name("Vasya")
                .score(List.of("5", "4", "3", "2", "1"))
                .build();


        Student student2 = Student.builder()
                .name("Petya")
                .score(List.of("5", "4", "3", "2", "1"))
                .build();

        Writable csvWriter = new CsvWriterImpl();
        csvWriter.writeToFile(List.of(person, person2, student, student2), "test.csv");
        csvWriter.writeToFile(List.of(person, person2), "test2.csv");
    }
}