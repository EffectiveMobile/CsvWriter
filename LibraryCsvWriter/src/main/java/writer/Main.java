package writer;

import com.github.javafaker.Faker;
import writer.csv.CsvWriterService;
import writer.model.Months;
import writer.model.Person;
import writer.model.Student;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Faker faker = new Faker();

        List<Student> students = Arrays.asList(
                Student.builder().name(faker.name().fullName()).score(Arrays.asList("5", "4", "3")).build(),
                Student.builder().name(faker.name().fullName()).score(Arrays.asList("3", "4", "5")).build()
        );

        List<Person> people = Arrays.asList(
                Person.builder().firstName(faker.name().firstName()).lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 11)])
                        .yearOfBirth(faker.number().numberBetween(1950, 2020)).build(),
                Person.builder().firstName(faker.name().firstName()).lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 28))
                        .monthOfBirth(Months.values()[faker.number().numberBetween(0, 11)])
                        .yearOfBirth(faker.number().numberBetween(1950, 2020)).build()
        );

        Writable csvWriter = new CsvWriterService();
        csvWriter.writeToFile(students,"students_file");
        csvWriter.writeToFile(people,"person_file");
    }


}