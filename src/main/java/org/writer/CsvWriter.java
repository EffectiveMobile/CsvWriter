package org.writer;

import org.writer.annotation.CsvColumn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Класс CsvWriter предназначен для записи данных в формате CSV.
 * <p> Класс имплементирует интерфейс Writable и реализует метод writeToFile.
 * В классе через рефлексию извлекаются поля объектов и их анннотации
 * для последующего формирования заголовок и строк данных</p>
 */
public class CsvWriter implements Writable {

  /**
   * Записывает данные в CSV файл.
   */
  @Override
  public void writeToFile(List<?> data, String fileName) {
    if (data == null || data.isEmpty()) return;

    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
      Field[] fields = data.get(0).getClass().getDeclaredFields();
      StringBuilder header = new StringBuilder();
      for (Field field : fields) {
        if (field.isAnnotationPresent(CsvColumn.class)) {
          CsvColumn annotation = field.getAnnotation(CsvColumn.class);
          header.append(annotation.name()).append(",");
        }
      }
      bufferedWriter.write(header.substring(0, header.length() - 1));
      bufferedWriter.newLine();

      for (Object item : data) {
        StringBuilder line = new StringBuilder();
        for (Field field : fields) {
          if (field.isAnnotationPresent(CsvColumn.class)) {
            field.setAccessible(true);
            try {
              line.append(field.get(item)).append(",");
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
        bufferedWriter.write(line.substring(0, line.length() - 1));
        bufferedWriter.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to write CSV", e);
    }
  }
}
