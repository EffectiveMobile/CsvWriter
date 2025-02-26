package org.writer.service;

import java.util.List;

/**
 * Writes data to a file.
 */
public interface Writable {

    /**
     * Writes the given data to a file.
     *
     * @param data     the data to write
     * @param fileName the name of the file
     */
    void writeToFile(List<?> data, String fileName);
}
