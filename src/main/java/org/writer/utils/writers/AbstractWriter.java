package org.writer.utils.writers;


import org.writer.utils.FileType;
import org.writer.utils.FileUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for writing data to files of various formats.
 * Provides a general implementation for working with annotated fields.
 *
 * @param <T> type of data being written
 */
public abstract class AbstractWriter<T> implements Writable<T>{

    /**
     * Returns the file type for the current implementation.
     *
     * @return file type
     */
    protected abstract FileType getFileType();

    /**
     * Gets a list of fields suitable for export to the current format.
     *
     * @param clazz class to parse
     * @return list of fields to export
     */
    protected abstract List<Field> getExportFields(Class<?> clazz);

    /**
     * Converts an object to a string for writing to a file.
     *
     * @param obj the object to convert
     * @param fields the list of fields to process
     * @return the string representation of the object
     */
    protected abstract String formatRow(T obj, List<Field> fields);

    /**
     * Generates file headers.
     *
     * @param fields list of fields
     * @return string with headers
     */
    protected abstract String formatHeaders(List<Field> fields);

    @Override
    public void writeToFile(List<T> data, String path) throws IOException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data is empty!");
        }

        Class<?> clazz = data.get(0).getClass();
        List<Field> fields = getExportFields(clazz);

        List<String> report = new ArrayList<>();
        report.add(formatHeaders(fields));

        for (T item : data) {
            report.add(formatRow(item, fields));
        }

        String fullPath = ensureFileExtension(path, getFileType());
        FileUtil.writeLinesToFile(fullPath, report);
    }

    /**
     * Adds file extension if it is missing.
     *
     * @param path file path
     * @param fileType file type
     * @return path with extension
     */
    protected String ensureFileExtension(String path, FileType fileType) {
        if (!path.endsWith(fileType.getFileFormat())) {
            return path + fileType.getFileFormat();
        }
        return path;
    }
}