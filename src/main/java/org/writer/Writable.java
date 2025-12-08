package org.writer;

import java.util.List;

/**
 * Интерфейс для записи коллекции в CSV отчет-
 */
public interface Writable {

    /**
     * Запись коллекции в отчет CSV
     * @param data коллекция данных для записи
     * @param fileName имя файла для записи
     */
    void writeToFile(List<?> data, String fileName);

}
