package org.writer;

import org.writer.annotations.CsvColumn;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.model.CsvWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        /*
         * Here we are checking the existence of the annotations
         * inside the class person if they exist then the
         * object of this class will be saved and created in csv file
         * */
        Person annotatedPerson = new Person();

        CsvWriter csvWriter = new CsvWriter();

        Class<?> clientClass = annotatedPerson.getClass();

        Field[] fields = clientClass.getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(CsvColumn.class)) {

                break;

            }
        }

        csvWriter.writeToCsv(List.of(annotatedPerson), "PersonsInfo.csv");


        //Similar approach
        Student annotatedStudent = new Student();

        Class<?> clientClass2 = annotatedStudent.getClass();

        Field[] fieldOfAnnotatedStudent = clientClass2.getDeclaredFields();

        for (Field field : fieldOfAnnotatedStudent) {

            if (field.isAnnotationPresent(CsvColumn.class)) {
                break;
            }
        }
        csvWriter.writeToCsv(List.of(annotatedStudent), "StudentInfo.csv");


    /*
    Saving the object in csv file without Checking for annotation
    * */

        Person person = new Person("Dan", "Travis", 15, Months.OCTOBER, 1999);

        csvWriter.writeToCsv(List.of(person), "Person.csv");

        Student student = new Student("Dan", List.of("3", "5", "5", "5", "5"));

        csvWriter.writeToCsv(List.of(student), "Student.csv");


    }

}