package org.writer.service;

import java.util.List;

/**
 * Интерфейс для записи данных в файл.
 */
public interface Writable {

    void writeToFile(List<?> data, String fileName);

}
