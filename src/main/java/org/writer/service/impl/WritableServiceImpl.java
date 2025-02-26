package org.writer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvDataException;
import org.writer.exception.CsvUnexpectedException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.service.CsvHeaderWriterService;
import org.writer.service.CsvRowWriterService;
import org.writer.service.Writable;
import org.writer.util.CsvReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Service for writing data to a CSV file.
 * Handles headers, rows, and error management.
 */
@Slf4j
@RequiredArgsConstructor
public class WritableServiceImpl implements Writable {
    private final CsvErrorHandler csvErrorHandler;
    private final CsvRowWriterService csvRowWriterService;
    private final CsvHeaderWriterService csvHeaderWriterService;

    /**
     * Writes data to a CSV file.
     *
     * @param data     the list of objects to write
     * @param fileName the name of the output CSV file
     * @throws CsvDataException if data or filename is invalid
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new CsvDataException("Data list is null or empty. Provided data: " + data);
        }

        if (fileName == null || fileName.isBlank()) {
            throw new CsvDataException("Filename is null or empty. Provided filename: '" + fileName + "'");
        }

        try {
            Class<?> clazz = data.get(0)
                    .getClass();
            List<Field> fields = CsvReflectionUtil.getAllFields(clazz);

            csvHeaderWriterService.writeHeaders(fields);

            for (Object object : data) {
                csvRowWriterService.writeRow(fields, object);
            }

            log.info("CSV file generated successfully: {}", fileName);
        } catch (Exception ex) {
            throw csvErrorHandler.handleError("Unexpected error while writing to CSV file: " + fileName, ex,
                    CsvUnexpectedException.class);
        }
    }
}