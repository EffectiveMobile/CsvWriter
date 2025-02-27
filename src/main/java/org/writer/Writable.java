package org.writer;

import java.util.List;

/**
 * Интерфейс для записи данных в файл.
 */
public interface Writable {

    /**
     * Записывает список объектов в файл.
     *
     * @param data Список объектов для записи.
     * @param fileName Имя файла для записи.
     */
    void writeToFile(List<?> data, String fileName);
}