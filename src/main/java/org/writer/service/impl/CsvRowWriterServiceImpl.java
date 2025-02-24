package org.writer.service.impl;

import lombok.RequiredArgsConstructor;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.CsvReflectionException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.formatter.CsvFieldFormatter;
import org.writer.model.CsvModel;
import org.writer.service.CsvRowWriterService;
import org.writer.util.CsvReflectionUtil;
import org.writer.validation.validator.CsvFieldValidator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

/**
 * Service for writing rows of data to a CSV file.
 */
@RequiredArgsConstructor
public class CsvRowWriterServiceImpl implements CsvRowWriterService {
    private final BufferedWriter bufferedWriter;
    private final CsvErrorHandler csvErrorHandler;
    private final CsvFieldFormatter csvFieldFormatter;

    /**
     * Writes a row of data to the CSV file.
     *
     * @param fields the list of fields to include in the row.
     * @param object the data object to write as a row.
     */
    @Override
    public void writeRow(List<Field> fields, CsvModel object) {
        try {
            var rowLine = new StringJoiner(",");
            for (Field field : fields) {
                if (CsvFieldValidator.isValidField(field)) {
                    var value = CsvReflectionUtil.getFieldValue(object, field);
                    rowLine.add(csvFieldFormatter.format(value));
                }
            }
            bufferedWriter.write(rowLine.toString());
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw csvErrorHandler.handleError("Error writing CSV row", e, CsvFileWriteException.class);
        } catch (CsvReflectionException e) {
            throw csvErrorHandler.handleError("Error accessing field values via reflection", e,
                    CsvReflectionException.class);
        }
    }
}
