package org.writer.utils.writers.csvwriter;


import org.writer.annotations.CSVColumn;
import org.writer.annotations.CSVReport;
import org.writer.utils.FileType;
import org.writer.utils.writers.AbstractWriter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of writing data to CSV format.
 *
 * @param <T> type of written data
 */
public class CSVWriter<T> extends AbstractWriter<T> {
    @Override
    protected FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    protected List<Field> getExportFields(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(CSVReport.class)) {
            throw new IllegalArgumentException("Data model is not suitable for CSV export!");
        }

        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CSVColumn.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    @Override
    protected String formatHeaders(List<Field> fields) {
        StringBuilder headers = new StringBuilder();
        Iterator<Field> iterator = fields.iterator();

        while (iterator.hasNext()) {
            Field field = iterator.next();
            CSVColumn annotation = field.getAnnotation(CSVColumn.class);
            headers.append(annotation.columnName());
            if (iterator.hasNext()) {
                headers.append(",");
            }
        }
        return headers.toString();
    }

    @Override
    protected String formatRow(T obj, List<Field> fields) {
        StringBuilder row = new StringBuilder();
        Iterator<Field> iterator = fields.iterator();

        while (iterator.hasNext()) {
            Field field = iterator.next();
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String stringValue = (value != null) ? value.toString() : "";
                row.append(stringValue);
                if (iterator.hasNext()) {
                    row.append(",");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access field: " + field.getName(), e);
            }
        }

        return row.toString();
    }
}
