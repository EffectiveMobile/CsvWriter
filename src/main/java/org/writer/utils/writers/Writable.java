package org.writer.utils.writers;

import java.io.IOException;
import java.util.List;

/**
 * Interface for writing file data.
 *
 * @param <T> Record data type.
 */
public interface Writable<T> {

    /**
     * Method for writing report file.
     *
     * @param data List of data to record.
     * @param path Absolute address of the file to write to.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    void writeToFile(List<T> data, String path) throws IOException;
}
