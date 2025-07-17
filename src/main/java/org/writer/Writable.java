package org.writer;

import java.util.List;

/**
 * Основной интерфейс, который позже реализуется в {@link WriterImpl}.
 */
public interface Writable {

    void writeToFile(List<?> data, String fileName);

}
