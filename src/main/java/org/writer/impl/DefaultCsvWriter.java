package org.writer.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.writer.Writable;
import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvMasked;
import org.writer.annotation.CsvRecord;
import org.writer.annotation.CsvTransient;
import org.writer.annotation.constans.MaskingStrategy;
import org.writer.annotation.constans.NamingStrategy;
import org.writer.exception.CsvRecordAnnotationMissingException;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация записи в формате CSV
 *
 * @author Астонский Шпион
 */
public class DefaultCsvWriter implements Writable {

    private final Writer writer;
    private final char delimiter;
    private final String lineSeparator;

    /**
     * Конструктор.
     *
     * @param writer        Куда будут записываться данные.
     * @param delimiter     Символ-разделитель полей.
     * @param lineSeparator Символ(ы) для разделения строк.
     */
    public DefaultCsvWriter(Writer writer, char delimiter, String lineSeparator) {
        Objects.requireNonNull(writer, "Writer не может быть null");
        Objects.requireNonNull(lineSeparator, "Line separator не может быть null");
        if (lineSeparator.isEmpty()) {
            throw new IllegalArgumentException("Line separator не может быть пустым");
        }
        this.writer = writer;
        this.delimiter = delimiter;
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void write(List<?> data) throws IOException {
        if (isInvalidData(data)) return;

        Object firstObject = data.get(0);
        Class<?> clazz = firstObject.getClass();
        CsvRecord csvRecordAnnotation = clazz.getAnnotation(CsvRecord.class);
        boolean includeHeader = (csvRecordAnnotation == null) || csvRecordAnnotation.includeHeader();
        NamingStrategy classNamingStrategy = (csvRecordAnnotation != null) ?
                csvRecordAnnotation.defaultNamingStrategy() : NamingStrategy.AS_IS_TO_SPACE_SEPARATED_CAPITALIZED;

        List<ProcessedField> processedFields = processFields(clazz, classNamingStrategy);
        if (processedFields.isEmpty()) return;

        processHeaders(includeHeader, processedFields);
        processDataFields(data, processedFields, clazz);
    }

    private static boolean isInvalidData(List<?> data) {
        boolean isInvalid = data == null || data.isEmpty() || data.get(0) == null;
        if (isInvalid) return true;
        Class<?> clazz = data.get(0).getClass();
        if (clazz.getAnnotation(CsvRecord.class) == null) {
            throw new CsvRecordAnnotationMissingException(clazz);
        }
        return false;
    }

    private void processDataFields(List<?> data, List<ProcessedField> processedFields, Class<?> clazz) throws IOException {
        for (Object obj : data) {
            if (obj == null) {
                writeRowInternal(Collections.nCopies(processedFields.size(), null));
                continue;
            }
            if (!clazz.isInstance(obj)) {
                throw new IllegalArgumentException("Все объекты в списке должны быть одного типа: " + clazz.getName() +
                        ", встречен: " + obj.getClass().getName());
            }
            List<String> rowValues = new ArrayList<>();
            for (ProcessedField pf : processedFields) {
                try {
                    Object value = pf.getField().get(obj);
                    String stringValue = convertFieldValueToString(value, pf);
                    rowValues.add(stringValue);
                } catch (IllegalAccessException e) {
                    throw new IOException("Ошибка доступа к полю: " + pf.getField().getName(), e);
                }
            }
            writeRowInternal(rowValues);
        }
    }

    private void processHeaders(boolean includeHeader, List<ProcessedField> processedFields) throws IOException {
        if (includeHeader) {
            List<String> headers = processedFields.stream()
                    .map(ProcessedField::getHeaderName)
                    .collect(Collectors.toList());
            writeRowInternal(headers);
        }
    }

    private void writeRowInternal(List<String> row) throws IOException {
        if (row == null) {
            writer.append(lineSeparator);
            return;
        }
        for (int i = 0; i < row.size(); i++) {
            writer.append(escapeAndQuote(row.get(i)));
            if (i < row.size() - 1) {
                writer.append(delimiter);
            }
        }
        writer.append(lineSeparator);
    }

    private List<ProcessedField> processFields(Class<?> clazz, NamingStrategy classNamingStrategy) {
        List<ProcessedField> tempFields = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(CsvTransient.class) ||
                    java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);

            String headerName = getHeader(field, classNamingStrategy);

            tempFields.add(new ProcessedField(field, headerName,
                    field.getAnnotation(CsvRecord.class),
                    field.getAnnotation(CsvMasked.class)));
        }

        return tempFields;
    }

    private String getHeader(Field field, NamingStrategy fieldNamingStrategy) {
        String headerName;
        CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
        if (csvColumn != null) {
            if (!csvColumn.name().isEmpty()) {
                headerName = csvColumn.name();
            } else {
                if (csvColumn.strategy() != NamingStrategy.DEFAULT) {
                    fieldNamingStrategy = csvColumn.strategy();
                }
                headerName = applyNamingStrategy(field.getName(), fieldNamingStrategy);
            }
        } else {
            headerName = applyNamingStrategy(field.getName(), fieldNamingStrategy);
        }
        return headerName;
    }

    private String applyNamingStrategy(String fieldName, NamingStrategy strategy) {
        if (strategy == NamingStrategy.AS_IS_TO_SPACE_SEPARATED_CAPITALIZED) {
            if (fieldName == null || fieldName.isEmpty()) return "";
            StringBuilder result = new StringBuilder();
            result.append(Character.toUpperCase(fieldName.charAt(0)));
            for (int i = 1; i < fieldName.length(); i++) {
                char currentChar = fieldName.charAt(i);
                if (Character.isUpperCase(currentChar)) {
                    result.append(' ');
                }
                result.append(currentChar);
            }
            return result.toString();
        }
        if (strategy == NamingStrategy.CAMEL_TO_SNAKE_CASE) {
            if (fieldName == null || fieldName.isEmpty()) return "";
            return fieldName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
        }
        if (strategy == NamingStrategy.CAMEL_TO_SCREAMING_SNAKE_CASE) {
            if (fieldName == null || fieldName.isEmpty()) return "";
            return fieldName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();
        }
        return fieldName;
    }

    private String convertFieldValueToString(Object value, ProcessedField processedField) {
        if (value == null) {
            return "";
        }

        CsvMasked csvMasked = processedField.getCsvMasked();
        String stringValue = value.toString();

        if (csvMasked != null) {
            stringValue = applyMasking(stringValue, csvMasked);
        }
        return stringValue;
    }

    private String applyMasking(String originalValue, CsvMasked csvMasked) {
        if (originalValue == null || originalValue.isEmpty()) return "";

        MaskingStrategy strategy = csvMasked.strategy();
        char maskChar = csvMasked.maskCharacter();
        int visibleChars = csvMasked.visibleChars();

        return switch (strategy) {
            case ASTERISKS_FULL -> repeatChar(maskChar, originalValue.length());
            case ASTERISKS_PARTIAL_PREFIX -> {
                if (originalValue.length() <= visibleChars) yield originalValue;
                yield originalValue.substring(0, visibleChars) + repeatChar(maskChar, originalValue.length() - visibleChars);
            }
            case ASTERISKS_PARTIAL_SUFFIX -> {
                if (originalValue.length() <= visibleChars) yield originalValue;
                yield repeatChar(maskChar, originalValue.length() - visibleChars) + originalValue.substring(originalValue.length() - visibleChars);
            }
        };
    }

    private String repeatChar(char c, int times) {
        if (times <= 0) return "";
        char[] chars = new char[times];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    private String escapeAndQuote(String value) {
        if (value == null) {
            return "";
        }

        boolean needsQuoting = false;
        if (value.indexOf(delimiter) != -1 ||
                value.indexOf('\n') != -1 ||
                value.indexOf('\r') != -1 ||
                value.indexOf('"') != -1) {
            needsQuoting = true;
        }
        if (value.contains(this.lineSeparator)) {
            needsQuoting = true;
        }


        String result = value.replace("\"", "\"\"");

        if (needsQuoting) {
            return "\"" + result + "\"";
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Getter
    @AllArgsConstructor
    private static class ProcessedField {
        private final Field field;
        private final String headerName;
        private final CsvRecord csvRecord;
        private final CsvMasked csvMasked;
    }
}
