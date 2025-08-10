package org.writer;

import java.util.List;

/**
 * Interface for writing data to a file.
 * Implementations define the format and logic.
 */
public interface Writable {

    /**
     * Writes the given list of objects to a file.
     *
     * @param data     List of objects to be written.
     * @param fileName Output file name (e.g., "data.csv").
     */
    void writeToFile(List<?> data, String fileName);

}
