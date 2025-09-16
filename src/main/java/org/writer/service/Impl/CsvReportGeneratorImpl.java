package org.writer.service.Impl;

import lombok.AllArgsConstructor;
import org.writer.service.Formatter;
import org.writer.service.ReportGenerator;
import org.writer.service.Writable;

import java.io.IOException;
import java.util.List;

/**
 * Класс {@code CsvReportGeneratorImpl} реализует интерфейс {@link ReportGenerator} для генерации
 * CSV-отчетов. Использует {@link Formatter} для форматирования данных и {@link Writable} для записи
 * отформатированных данных в файл.
 *
 * @since 1.0
 */
@AllArgsConstructor
public class CsvReportGeneratorImpl implements ReportGenerator {

    /**
     * Форматтер для преобразования данных в CSV-формат.
     */
    private Formatter formatter;

    /**
     * Компонент для записи данных в файл.
     */
    private Writable writable;

    @Override
    public void generate(List<?> data, String fileName) throws IOException {
        List<String> formattedData = formatter.formatting(data);
        writable.writeToFile(formattedData, fileName);
    }
}
