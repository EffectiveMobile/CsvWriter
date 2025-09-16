package org.writer.service;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс {@code ReportGenerator} определяет контракт для генерации отчета на основе предоставленных данных.
 * Используется для записи данных в файл с указанным именем.
 *
 * @since 1.0
 */
public interface ReportGenerator {

    /**
     * Генерирует отчет на основе списка данных и записывает его в файл с указанным именем.
     *
     * @param data список данных для генерации отчета
     * @param fileName имя файла, в который будет записан отчет
     * @throws IOException если произошла ошибка при записи в файл
     */
    void generate(List<?> data, String fileName) throws IOException;
}
