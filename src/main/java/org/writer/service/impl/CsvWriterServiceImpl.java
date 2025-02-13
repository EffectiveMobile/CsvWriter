package org.writer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvDataException;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.CsvReflectionException;
import org.writer.exception.CsvUnexpectedException;
import org.writer.model.CsvModel;
import org.writer.service.CsvWriterService;
import org.writer.util.CsvReflectionUtil;
import org.writer.validation.validator.CsvFieldValidator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link CsvWriterService} interface.
 * Handles writing data to CSV files, including headers and rows, with support for reflection-based field processing.
 */
@Slf4j
public class CsvWriterServiceImpl implements CsvWriterService {

    /**
     * Writes a list of data objects to a CSV file.
     *
     * @param data     the list of data objects to write. Each object must extend {@link CsvModel}.
     * @param fileName the name of the file to write the data to.
     * @throws CsvDataException       if the data list is empty or null.
     * @throws CsvFileWriteException  if an error occurs while writing to the file.
     * @throws CsvUnexpectedException if an unexpected error occurs during the process.
     */
    @Override
    public void writeToFile(List<? extends CsvModel> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new CsvDataException("Data list is empty or null");
        }

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            Class<?> clazz = data.get(0)
                    .getClass();
            List<Field> fields = CsvReflectionUtil.getAllFields(clazz);

            writeHeaders(writer, fields);

            for (CsvModel object : data) {
                writeRow(writer, fields, object);
            }

            log.info("CSV file generated successfully: {}", fileName);
        } catch (IOException ex) {
            log.error("Failed to write to CSV file: {}", fileName, ex);
            throw new CsvFileWriteException("Failed to write to CSV file", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while writing to CSV file: {}", fileName, ex);
            throw new CsvUnexpectedException("Unexpected error while writing to CSV file", ex);
        }
    }

    /**
     * Writes the headers (field names) to the CSV file.
     *
     * @param writer the {@link BufferedWriter} used to write to the file.
     * @param fields the list of fields to include as headers.
     * @throws CsvFileWriteException  if an error occurs while writing the headers.
     * @throws CsvUnexpectedException if an unexpected error occurs.
     */
    private void writeHeaders(BufferedWriter writer, List<Field> fields) {
        try {
            var headerLine = new StringBuilder();
            for (Field field : fields) {
                if (CsvFieldValidator.isValidField(field)) {
                    headerLine.append(field.getName())
                            .append(",");
                }
            }
            if (!headerLine.isEmpty()) {
                headerLine.deleteCharAt(headerLine.length() - 1);
            }
            writer.write(headerLine.toString());
            writer.newLine();
        } catch (IOException ex) {
            log.error("Failed to write headers to CSV file", ex);
            throw new CsvFileWriteException("Failed to write headers to CSV file", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while writing headers", ex);
            throw new CsvUnexpectedException("Unexpected error while writing headers", ex);
        }
    }

    /**
     * Writes a row of data to the CSV file.
     *
     * @param writer the {@link BufferedWriter} used to write to the file.
     * @param fields the list of fields to include in the row.
     * @param object the data object to write as a row.
     * @throws CsvFileWriteException  if an error occurs while writing the row.
     * @throws CsvUnexpectedException if an unexpected error occurs.
     */
    private void writeRow(BufferedWriter writer, List<Field> fields, CsvModel object) {
        try {
            var rowLine = new StringBuilder();
            for (Field field : fields) {
                if (CsvFieldValidator.isValidField(field)) {
                    try {
                        var value = CsvReflectionUtil.getFieldValue(object, field);
                        String formattedValue;

                        if (value instanceof List) {
                            formattedValue = ((List<?>) value).stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining(";"));
                        } else {
                            formattedValue = value.toString();
                        }

                        rowLine.append(formattedValue)
                                .append(",");
                    } catch (CsvReflectionException ex) {
                        log.error("Failed to get field value: {}", field.getName(), ex);
                        throw new CsvFileWriteException("Failed to get field value", ex);
                    }
                }
            }
            if (!rowLine.isEmpty()) {
                rowLine.deleteCharAt(rowLine.length() - 1);
            }
            writer.write(rowLine.toString());
            writer.newLine();
        } catch (IOException ex) {
            log.error("Failed to write row to CSV file", ex);
            throw new CsvFileWriteException("Failed to write row to CSV file", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while writing row", ex);
            throw new CsvUnexpectedException("Unexpected error while writing row", ex);
        }
    }
}