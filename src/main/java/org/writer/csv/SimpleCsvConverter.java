package org.writer.csv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.writer.csv.annotation.CsvExclude;
import org.writer.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class SimpleCsvConverter implements CsvConverter {
    private boolean convertCollectionAsMultipleCell = false;
    private String cellSeparator = ",";
    private StringBuilder fileData;

    @Override
    public String toCsvString(List<?> data) {
        if (data == null || data.isEmpty()) {
            throw new RuntimeException("No data to convert to Csv");
        }
        if (!isValidList(data)) {
            throw new IllegalArgumentException("The input list contains objects of different classes." +
                    " The CSV data will not be correct");
        }
        fileData = new StringBuilder();
        Field[] fields = ReflectionUtils.getAllFields(data.get(0));
        if (isJavaLangClass(data)) {
            for (var object : data) {
                fileData.append(object.toString())
                        .append(System.lineSeparator());
            }
        } else {
            appendColumnHeaders(fields);
            for (var object : data) {
                appendCsvRow(object, fields);
            }
        }

        return fileData.toString();
    }

    private boolean isValidList(List<?> data){
        Class<?> fistElementType = data.get(0).getClass();
        return  data.stream()
                .map(Object::getClass)
                .allMatch(aClass -> aClass.equals(fistElementType));
    }

    private boolean isJavaLangClass(List<?> data) {
        Class<?> fistElementType = data.get(0).getClass();
        return fistElementType.getPackageName().contains("java.lang") || fistElementType.isPrimitive();
    }

    private void appendColumnHeaders(Field[] fields) {
        if (isConvertCollectionAsMultipleCell()) {
            return;
        }
        for (Field field: fields) {
            if (field.isAnnotationPresent(CsvExclude.class)) continue;
            fileData.append(field.getName()).append(cellSeparator);
        }
        removeLastCellSeparator();
        fileData.append(System.lineSeparator());
    }

    private void appendCsvRow(Object object, Field[] objectFields) {
        for (Field field : objectFields) {
            if (field.isAnnotationPresent(CsvExclude.class)) continue;
            Object fieldValue = getFieldValue(object, field);
            if (ReflectionUtils.isCollection(field)) {
                appendCollectionField(fieldValue);
            } else {
                fileData.append(fieldValue.toString());
            }
            fileData.append(cellSeparator);
        }
        removeLastCellSeparator();
        fileData.append(System.lineSeparator());
    }

    private Object getFieldValue(Object object, Field objectField) {
        try {
            var fieldValue = ReflectionUtils.getPrivateFieldValue(object, objectField);
            return fieldValue != null ? fieldValue : "";
        } catch (InaccessibleObjectException ex) {
            return object.toString();
        } catch (IllegalAccessException ex) {
            log.warn("Field {} has not converted because access is not allowed. Error message: {}",
                    objectField.getName(), ex.getMessage());
        }
        return "";
    }

    private void appendCollectionField(Object fieldValue) {
        if (fieldValue == null || fieldValue.equals("")){
            return;
        }
        if (isConvertCollectionAsMultipleCell()) {
            appendCollectionAsCsvCells(fieldValue);
            return;
        }
        appendCollectionAsCsvCell(fieldValue);
    }

    /**Convert collection into simple field value*/
    private void appendCollectionAsCsvCell(Object collectionValue){
        if (collectionValue == null) {
            fileData.append(cellSeparator);
            return;
        }
        String cellValue = collectionValue.toString().replaceAll(cellSeparator, " |");
        fileData.append(cellValue).append(cellSeparator);
    }

    /**Convert a collection into multiple columns*/
    private void appendCollectionAsCsvCells(Object collectionValue){
        if (collectionValue != null) {
            Collection<?> collection = (Collection<?>) collectionValue;
            for (var element : collection) {
                fileData.append(element.toString()).append(cellSeparator);
            }
            removeLastCellSeparator();
        }
    }

    private void removeLastCellSeparator() {
        if (fileData.length() > cellSeparator.length()) {
            fileData.delete(fileData.length() - cellSeparator.length(), fileData.length());
        }
    }



}
