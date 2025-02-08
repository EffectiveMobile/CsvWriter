package org.writer.service;

import lombok.RequiredArgsConstructor;
import org.writer.exception.DataIsEmptyException;
import org.writer.exception.WritingToFileException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Реализация интерфейса {@link Writable} для записи данных в CSV-файл.
 * Использует Reflection и аннотации для определения структуры CSV.
 */
@RequiredArgsConstructor
public class WritableServiceImpl implements Writable {

    private final FormatterLineService formatterLineService;

    /**
     * Запись объектов в файл в формате csv.
     *
     * @param data - лист с объектами.
     * @param fileName - имя файла, куда нужно записать данные.
     * @throws DataIsEmptyException - возникает при передаче пустого списка с данными.
     * @throws WritingToFileException - если возникли проблемы при попытке записи в файл.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new DataIsEmptyException("Data list is empty or null");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){

            writerHeader(writer, data);
            writerData(writer, data);

        }
        catch (IOException e) {
            throw new WritingToFileException("Failed to write to file: " + fileName ,e);
        }
    }

    /**
     * Вспомогательный метод для записи заголовка.
     *
     * @param writer - BufferedWriter для записи в файл.
     * @param data - список с объектами.
     */
    private void writerHeader(BufferedWriter writer, List<?> data){

        writeToLine(writer, formatterLineService.formatHeaders(data));
    }


    /**
     * Вспомогательный метод для записи данных.
     *
     * @param writer - BufferedWriter для записи в файл.
     * @param data - список с объектами.
     */
    private void writerData(BufferedWriter writer, List<?> data){

        writeToLine(writer, formatterLineService.formatLine(data));

    }

    /**
     * Метод для попытки записать строку в файл.
     *
     * @param writer - BufferedWriter для записи в файл.
     * @param line - строка для записи.
     */
    private void writeToLine(BufferedWriter writer, StringBuilder line) {
        try {
            writer.write(line.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new WritingToFileException("Failed to write header to file", e);
        }
    }

}
