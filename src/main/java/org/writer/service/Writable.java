package org.writer.service;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс {@code Writable} определяет контракт для записи данных в файл.
 * Используется для сохранения списка данных в файл с указанным именем.
 *
 * @since 1.0
 */
public interface Writable {

    /**
     * Записывает список данных в файл с указанным именем.
     *
     * @param data список данных для записи
     * @param fileName имя файла, в который будут записаны данные
     * @throws IOException если произошла ошибка при записи в файл
     */
    void writeToFile(List<?> data, String fileName) throws IOException;

}
