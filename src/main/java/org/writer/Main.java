package org.writer;

import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.Impl.CsvFormatter;
import org.writer.service.Impl.CsvReportGeneratorImpl;
import org.writer.service.Impl.WritableImpl;
import org.writer.service.ReportGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Log4j2
public class Main {

    private static Faker faker = new Faker(new Locale("en"));
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static void main(String[] args) {
        ReportGenerator reportGenerator = new CsvReportGeneratorImpl(new CsvFormatter(), new WritableImpl());

        try {
            recordingPerson(reportGenerator);
            recordingStudent(reportGenerator);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private static void recordingPerson(ReportGenerator generator) throws IOException {
        String fileName = "person.csv";
        List<Person> data = IntStream.range(0, 1000)
                .mapToObj(obj -> {
                    LocalDate birthDate = LocalDate.parse(
                            faker.timeAndDate().birthday(1, 99, "YYYY/MM/dd"),
                            dateFormat);
                    return Person.builder()
                            .firstName(faker.name().firstName())
                            .lastName(faker.name().lastName())
                            .dayOfBirth(birthDate.getDayOfMonth())
                            .monthOfBirth(Months.values()[birthDate.getMonthValue() - 1])
                            .yearOfBirth(birthDate.getYear())
                            .build();
                })
                .toList();
        generator.generate(data, fileName);
    }

    private static void recordingStudent(ReportGenerator generator) throws IOException {
        String fileName = "test/student.csv";
        List<Student> data = IntStream.range(0, 1000)
                .mapToObj(obj -> Student.builder()
                        .name(faker.name().fullName())
                        .score(
                                IntStream.range(0, faker.number().numberBetween(0, 10))
                                        .mapToObj(Integer::toString)
                                        .toList()
                        )
                        .build())
                .toList();
        generator.generate(data, fileName);
    }
}