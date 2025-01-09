package org.writer.service;

import lombok.RequiredArgsConstructor;
import org.writer.exception.EmptyDataException;
import org.writer.exception.FileWritingException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Сервис для записи данных в CSV-файл.
 * <p>
 * Записывает данные в файл с использованием форматирования, предоставляемого {@link Formatter}.
 * Форматирование включает в себя преобразование объектов в строки CSV и запись их в файл.
 */
@RequiredArgsConstructor
public class CsvWriter implements Writable {

    private final Formatter formatter;


    /**
     * Записывает данные в указанный файл в формате CSV.
     * <p>
     * Если данные пусты или равны {@code null}, выбрасывается исключение {@link EmptyDataException}.
     *
     * @param data     список данных для записи.
     * @param fileName имя файла, в который будут записаны данные.
     * @throws EmptyDataException  если данные пусты или равны {@code null}.
     * @throws FileWritingException если произошла ошибка при записи в файл.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new EmptyDataException("Data is empty or null.");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {

            writeLine(bufferedWriter, formatter.formatHeaders(data));

            formatter.formatData(data).forEach(line -> writeLine(bufferedWriter, line));

        } catch (IOException e) {
            throw new FileWritingException("Error writing to file: " + fileName, e);
        }
    }

    /**
     * Записывает одну строку в файл.
     *
     * @param writer объект {@link BufferedWriter} для записи.
     * @param line   строка, которую необходимо записать.
     * @throws FileWritingException если произошла ошибка при записи строки.
     */
    private void writeLine(BufferedWriter writer, String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new FileWritingException("Error writing line to file", e);
        }
    }
}
