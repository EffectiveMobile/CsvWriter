package org.writer.service;

import java.util.List;

/**
 * Интерфейс для записи данных в файл.
 * Реализации этого интерфейса сохраняют список объектов в файл.
 *
 * @author Мельников Никита
 */
public interface Writable {
    /**
     * Сохраняет список объектов в файл.
     *
     * @param data     список объектов для записи.
     * @param fileName имя файла, в который будут записаны данные.
     */
    void writeToFile(List<?> data, String fileName);

}
