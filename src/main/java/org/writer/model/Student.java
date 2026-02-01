package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.datafaker.Faker;
import org.writer.annotations.CsvColumn;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    private static Faker faker = new Faker();

    @CsvColumn(name = "firstName")
    private String name;

    @CsvColumn(name = "score")
    private List<String> scores;

    public Student() {
        this.name = faker.name().name();
        this.scores = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int randomNumber = 3 + (int)(Math.random() * 3);
            scores.add(String.valueOf(randomNumber));
        }
    }

}