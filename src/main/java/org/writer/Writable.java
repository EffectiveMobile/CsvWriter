package org.writer;

import java.util.List;

/**
 * {@code Writable} определяет метод для записи данных в файл.
 */
public interface Writable {

    /**
     * Записывает список объектов в указанный файл.
     * @param data Список объектов, которые должны быть записаны в файл.
     * @param fileName имя файла, в который должны быть записаны данные.
     */
    void writeToFile(List<?> data, String fileName);

}
