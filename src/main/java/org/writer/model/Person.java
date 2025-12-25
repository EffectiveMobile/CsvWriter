package org.writer.model;

import org.writer.annotation.CSVField;

public class Person {

    @CSVField(name = "Имя", order = 1)
    private String firstName;

    @CSVField(name = "Фамилия", order = 2)
    private String lastName;

    @CSVField(name = "День рождения", order = 3)
    private int dayOfBirth;

    @CSVField(name = "Месяц рождения", order = 4)
    private Months monthOfBirth;

    @CSVField(name = "Год рождения", order = 5)
    private int yearOfBirth;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(int dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public Months getMonthOfBirth() {
        return monthOfBirth;
    }

    public void setMonthOfBirth(Months monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Person() {
    }

    public Person(String firstName, String lastName, int dayOfBirth, Months monthOfBirth, int yearOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dayOfBirth = dayOfBirth;
        this.monthOfBirth = monthOfBirth;
        this.yearOfBirth = yearOfBirth;
    }
}
