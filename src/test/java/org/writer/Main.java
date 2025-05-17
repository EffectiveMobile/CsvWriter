package org.writer;

import lombok.AllArgsConstructor;
import org.writer.annotation.CsvMasked;
import org.writer.annotation.CsvRecord;
import org.writer.annotation.constans.MaskingStrategy;
import org.writer.model.Client;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.util.TestDataGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        try (Writable writer = CsvWriterFactory.create(new FileWriter("user_list.csv"))) {
            List<User> users = new ArrayList<>();
            users.add(new User("Bob", "Ivanovitch", "1123-1313-1231-1414"));
            users.add(new User("Nicol", "Ivanovitch", "1841-1313-2355-1414"));
            users.add(new User("Vladimir", "Ivanovitch", "2809-9253-1231-1414"));

            writer.write(users);
        }

        try (Writable writer = CsvWriterFactory.create(new FileWriter("person_list.csv"))) {
            List<Person> persons = TestDataGenerator.generatePersons(3);
            writer.write(persons);
        }

        try (Writable writer = CsvWriterFactory.create(new FileWriter("clients_list.csv"))) {
            List<Client> clients = TestDataGenerator.generateClients(3);
            writer.write(clients);
        }

        try (Writable writer = CsvWriterFactory.create(new FileWriter("students_list.csv"))) {
            List<Student> students = TestDataGenerator.generateStudents(3);
            writer.write(students);
        }
    }

    @CsvRecord
    @AllArgsConstructor
    private static class User {

        private String firstName;
        private String lastName;
        @CsvMasked(strategy = MaskingStrategy.ASTERISKS_PARTIAL_SUFFIX)
        private String accountNumber;
    }
}
