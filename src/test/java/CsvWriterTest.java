import org.junit.jupiter.api.Test;
import org.writer.CsvWriter;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс CsvWriterTest содержит тесты для проверки работы класса CsvWriter.
 * <p>Тест проверяет корректность записи данных пользователей в CSV файл.</p>
 */
public class CsvWriterTest {

  /**
   * Тестирует метод toWriteCSV класса CsvWriter.
   * <p>Метод создает список людей, записывает их данные в файл и
   * затем считывает его для проверки корректной записи данных .</p>
   */

  @Test
  public void testWriteToCsv() throws IOException {
    List<Person> persons = new ArrayList<>();
    persons.add(new Person("Polina", "Khval", 25, Months.APRIL, 2001));
    persons.add(new Person("Alina", "Abramova", 20, Months.AUGUST, 2000));

    CsvWriter csvWriter = new CsvWriter();
    String fileName = "test_file_persons.csv";
    csvWriter.writeToFile(persons,fileName);

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line = reader.readLine();
      assertEquals("Name,LastName,Day of Birth,Month Of Birth,Year Of Birth", line);

      line = reader.readLine();
      assertEquals("Polina,Khval,25,APRIL,2001", line);

      line = reader.readLine();
      assertEquals("Alina,Abramova,20,AUGUST,2000", line);
    }
  }
}
