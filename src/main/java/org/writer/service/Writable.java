package org.writer.service;

import java.util.List;

/**
 * Интерфейс для записи данных в файл.
 */
public interface Writable {

    /**
     * Запись объектов в файл в формате csv.
     *
     * @param data - лист с объектами.
     * @param fileName - имя файла, куда нужно записать данные.
     */
    void writeToFile(List<?> data, String fileName);

}
