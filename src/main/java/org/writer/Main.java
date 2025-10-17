package org.writer;

import org.writer.csv.CsvDocument;
import org.writer.model.Months;
import org.writer.model.Person;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var report = new Report(new CsvDocument());
        report.writeObjectAsDoc(List.of(Person.builder()
                .firstName("Mike")
                .lastName("Petrov")
                .dayOfBirth(10)
                .monthOfBirth(Months.AUGUST)
                .yearOfBirth(1988)
                .password("myPassword")
                .build()), "People");
    }
}