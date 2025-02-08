package org.writer.service;

import java.util.List;

/**
 * Интерфейс для форматирования данных для записи в файл.
 */
public interface FormatterLineService {


    /**
     * Форматирования данных для записи заголовка в файл.
     *
     * @param data - список с объектами.
     * @return - строку с названиями колонок.
     */
    StringBuilder formatHeaders(List<?> data);

    /**
     * Форматирование данных для записи в файл.
     *
     * @param data - список с объектами.
     * @return - строку с данными.
     */
    StringBuilder formatLine(List<?> data);
}
