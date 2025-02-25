package org.writer;

import java.util.List;

/**
 * Интерфейс для записи данных в файл.
 */
public interface Writable {

    /**
     * Записывает список объектов в файл.
     * @param data список объектов для записи
     * @param fileName имя файла
     */
    void writeToFile(List<?> data, String fileName);

}
