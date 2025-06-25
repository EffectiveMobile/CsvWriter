package org.writer;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        List<String> scores1 = List.of("score1", "score2");
        Student student1 = Student.builder()
                .name("Ivan")
                .score(scores1)
                .build();

        List<String> score2 = List.of("111", "222", "333");
        Student student = Student.builder()
                .name("Asya")
                .score(score2)
                .build();


        Student student2 = Student.builder()
                .name("Senya")
                .build();


        Person person1 = Person.builder()
                .firstName("Сергей")
                .lastName("Петров")
                .dayOfBirth(12)
                .monthOfBirth(Months.APRIL)
                .yearOfBirth(1995)
                .build();

        StringCsvWriter csvWriter = new StringCsvWriter(new SimpleCsvConverter());
        csvWriter.writeToFile(List.of(person1), "/home/alexei/IdeaProjects/CsvWriter");
    }
}