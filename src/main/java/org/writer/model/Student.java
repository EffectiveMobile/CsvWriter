package org.writer.model;

import org.writer.annotation.CSVField;

import java.util.List;

public class Student {

    @CSVField(name = "Имя", order = 1)
    private String name;

    @CSVField(name = "Баллы", order = 2)
    private List<String> score;

    public Student() {
    }

    public Student(String name, List<String> score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getScore() {
        return score;
    }

    public void setScore(List<String> score) {
        this.score = score;
    }

}