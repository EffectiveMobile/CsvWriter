/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
package org.writer.processor;

import com.opencsv.CSVWriter;
import org.writer.Writable;
import org.writer.annotation.CsvField;
import org.writer.annotation.WritableToCsv;
import org.writer.exception.WriterException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса {@link Writable} для записи данных в CSV файл.
 * В данный момент реализована запись в enum с отметкой {@link CsvField} для необходимых полей.
 * На классы не распространяется реализация с отметкой {@link CsvField}, но может быть реализована в будущем.
 * Используется библиотека OpenCSV.
 *
 * @author Vladimir Chavkin
 */
public class CSVWriterProcessor implements Writable {

    /**
     * Записывает данные в CSV файл.
     *
     * @param data              Данные для записи
     * @param fileName          Название файла
     * @throws WriterException  Ошибка записи
     */
    @Override
    public void writeToFile(List<?> data, String fileName) throws WriterException {

        // Проверки
        if (data == null) {
            throw new WriterException("Data cannot be null");
        }

        if (data.isEmpty()) {
            throw new WriterException("Data cannot be empty");
        }

        if (fileName == null) {
            fileName = data.get(0).getClass().getAnnotation(WritableToCsv.class).fileName();
        }

        // Создание списка данных
        List<String[]> csvData = new ArrayList<>();

        // Проверка на enum
        if (data.get(0).getClass().isEnum()) {
            List<Field> csvFields = new ArrayList<>();
            for (Field field : data.get(0).getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(CsvField.class) && !field.isSynthetic()) {
                    csvFields.add(field);
                }
            }
            if (csvFields.isEmpty()) {
                throw new WriterException("No fields annotated with @CsvField found in enum");
            }

            // Формирование заголовков
            List<String> headers = new ArrayList<>();
            for (Field field : csvFields) {
                CsvField csvField = field.getAnnotation(CsvField.class);
                String header = csvField.name().isEmpty() ? field.getName() : csvField.name();
                headers.add(header);
            }
            csvData.add(headers.toArray(new String[0]));

            // Обработка данных из переданного списка
            for (Object o : data) {
                if (o == null) {
                    csvData.add(new String[csvFields.size()]);
                    continue;
                }
                String[] row = new String[csvFields.size()];
                for (int i = 0; i < csvFields.size(); i++) {
                    try {
                        Field field = csvFields.get(i);
                        field.setAccessible(true);
                        Object value = field.get(o);
                        row[i] = value != null ? value.toString() : "";
                    } catch (IllegalAccessException e) {
                        throw new WriterException("Error accessing field");
                    }
                }
                csvData.add(row);
            }
            // Для остальных классов
        } else {
            Field[] fields = data.get(0).getClass().getDeclaredFields();

            // Формирование заголовков
            String[] headers = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                headers[i] = fields[i].getName();
            }
            csvData.add(headers);

            // Обработка данных из переданного списка
            for (Object o : data) {
                String[] row = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    try {
                        fields[i].setAccessible(true);
                        Object value = fields[i].get(o);
                        row[i] = value != null ? value.toString() : "";
                    } catch (IllegalAccessException e) {
                        throw new WriterException("Error accessing field");
                    }
                }
                csvData.add(row);
            }
        }
        // Запись данных в CSV
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))) {
            csvWriter.writeAll(csvData);
        } catch (IOException e) {
            throw new WriterException("Error writing to file");
        }
    }

}
