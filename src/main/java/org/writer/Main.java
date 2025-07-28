package org.writer;


import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.Writable;
import org.writer.service.impl.CsvWritableImpl;

import java.util.ArrayList;
import java.util.List;


import static org.writer.model.Months.*;

public class Main {
    public static void main(String[] args) {

        Writable writable = new CsvWritableImpl();

        Person person1 = new Person("oleg1", "fokin1", 25, JANUARY, 1896);
        Person person2 = new Person("oleg2", "fokin2", 45, MARCH, 1900);

        List<Person> people = List.of(person1, person2);
        writable.writeToFile(people,"1.txt");


        List<Integer> scores1 = List.of(1,2,3,4);
        List<Integer> scores2 = List.of(5,6,7,8);

        Student student1 = new Student("Oleg1",scores1);
        Student student2 = new Student("Oleg2",scores2);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        writable.writeToFile(students,"2.txt");

    }
}