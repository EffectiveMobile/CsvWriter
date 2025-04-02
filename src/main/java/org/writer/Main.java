package org.writer;


import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();
        List<Person> personList = new ArrayList<>();
        List<Student> studentsList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            personList.add(new Person(faker.name().firstName(),
                    null,
                    i + 1,
                    Months.APRIL,
                    i + 1));
        }

        for (int i = 0; i < 10; i++) {
            List<String> stringList = new ArrayList<>();
            for (int j = 0; j < 5; j++){
                stringList.add(faker.name().name());
            }
            studentsList.add(new Student(faker.name().name(), stringList));


        }

        try {
            WritableImpl writable = new WritableImpl();
            writable.writeToFile(personList, "generated.csv");
            writable.writeToFile(studentsList,"generatedstudent.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
