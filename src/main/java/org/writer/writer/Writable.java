package org.writer.writer;

import java.util.List;

/**
 * Определяет контракт для классов, которые могут записывать
 * списки объектов в файл в определённом формате.
 */
public interface Writable {
    /**
     * Записывает переданный список объектов в файл с указанным именем.
     * <p>
     * Реализации этого метода должны чётко определять формат записи
     * и требования к аннотациям и/или структуре объектов в списке {@code data}.
     *
     * @param data список объектов для записи.
     * @param fileName полное имя файла, в который будут сохранены данные.
     *
     * @throws IllegalArgumentException если какой-либо из входных параметров невалиден.
     *                                  Например, если список {@code data} равен {@code null} или {@code data.isEmpty()},
     *                                  или если {@code fileName} равен {@code null} или {@code fileName.isBlank()}.
     * @throws RuntimeException если в процессе записи в файл возникает ошибка ввода-вывода или другая критическая ошибка.
     */
    void writeToFile(List<?> data, String fileName);
}
