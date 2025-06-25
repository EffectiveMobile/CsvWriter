package org.writer;

import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        List<String> scores1 = List.of("score1", "score2");
        Student student1 = Student.builder()
                .name("ivan")
                .score(scores1)
                .build();

        List<String> score2 = List.of("111", "222", "333");
        Student student = Student.builder()
                .name("Asya")
                .score(score2)
                .build();
        CsvPrinter csvPrinter = new CsvPrinter(List.of(student, student1));
        System.out.println(csvPrinter.printToString());
    }
}