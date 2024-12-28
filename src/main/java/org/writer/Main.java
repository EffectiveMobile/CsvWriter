package org.writer;

import lombok.SneakyThrows;
import org.writer.model.Person;
import org.writer.model.Scores;
import org.writer.model.Student;
import org.writer.service.CSVManipulator;
import org.writer.service.ClassManipulator;
import org.writer.service.Manipulator;
import org.writer.util.ClassLabel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(3);

        Person person1 = Person
                         .builder()
                .age(18)
                .firstName("name1")
                .lastName("last1")
                .dateOfBirth(LocalDate.now().minusYears(15))
                .build();

        Person person2 = Person
                .builder()
                .age(12)
                .firstName("name2")
                .lastName("last2")
                .dateOfBirth(LocalDate.now().minusYears(17))
                .build();

        Student student1 = Student
                .builder()
                .place(1)
                .firstName("name3")
                .lastName("surename3")
                .score(25.9d)
                .build();

        Scores scores1 = Scores
                .builder()
                .scores(list)
                .build();

        ClassManipulator classManipulator = new ClassManipulator();

        Manipulator<Object> manipulator = new Manipulator<>();

        CSVManipulator csvManipulator = new CSVManipulator();

        List<Object> objects = new ArrayList<>(List.of(person1, person2));

      csvManipulator.createCSV(
              manipulator.manipulate(classManipulator.classScanner("org.writer.model", ClassLabel.class), objects),
               "Person");

    //    csvManipulator.createCSV1(manipulator.manipulator(classManipulator.classScanner("org.writer.model", ClassLabel.class), objects));

//                ClassLabel.class));
//
//
//        Adapter adapter = new Adapter();
//        Writable writable = new WritableImpl();
//        CSVParser csvParser = new CSVParser<>(writable, adapter);
//        csvParser.createCSV(res, List.of(person1, student1, scores1, person2));
    }
}