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

/**
 * Class for conversion different objects to {@link String} for writing from csv file.
 * Class supports the conversion of POJO classes, strings, primitives and their boxed classes.
 * For class instances can be set field values that define the conversion mode:
 * {@link SimpleCsvConverter#convertCollectionAsMultipleCell}, {@link SimpleCsvConverter#cellSeparator}
 *
 * @Author Alexei Shvariov
 * @Date 26.06.2025
 * @Version 1.0
 */

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class SimpleCsvConverter implements CsvConverter<String> {
    /**
     * If false Collection to be converted in single cell,
     * where values will be set in brackets with separator " |".
     * If true each value will be set in its cells.
     * Default - false.
     * */
    private boolean convertCollectionAsMultipleCell = false;
    /**
     * String element whose will be used as splitter for field values in csv data.
     * Default - ",".
     * */
    private String cellSeparator = ",";
    /**
     * StringBuilder for building csv data. Created new then invoke method {@link SimpleCsvConverter#toCsvFileData}*/
    private StringBuilder fileData;

    /**
     * Method for converting data for string in supported csv file format.
     * @param data - collection of objects or strings or primitives or their boxed types.
     * @return {@link String} in supported csv file format.
     * @exception IllegalArgumentException must be throws if parameter data is null or empty,
     * or collection contains elements different types.
     * */
    @Override
    public String toCsvFileData(List<?> data) {
        if (data == null || data.isEmpty()) {
            final String errorMessage = "No data to convert to Csv";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (!isValidList(data)) {
            final String errorMessage = "The input list contains objects of different classes." +
                    " The CSV data will not be correct";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        fileData = new StringBuilder();
        final Field[] fields = ReflectionUtils.getAllFields(data.get(0));
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

    /**
     * Check if collection contains objects different types.
     * This prevents you from defining the csv table structure.
     * @param data - collection of objects or strings or primitives or their boxed types.
     * @return true if type all collection elements equals type first element.
     * Return false if type any element not equals first element type.
     * */
    private boolean isValidList(List<?> data){
        final Class<?> fistElementType = data.get(0).getClass();
        return  data.stream()
                .map(Object::getClass)
                .allMatch(aClass -> aClass.equals(fistElementType));
    }

    /**
     * Checks if data collection contains standard java types (strings, primitives, etc.).
     * @param data - collection of objects or strings or primitives or their boxed types.
     * @return true if data collection objects type from java.lang package or is primitive type,
     * otherwise return false.
     * */
    private boolean isJavaLangClass(List<?> data) {
        final Class<?> fistElementType = data.get(0).getClass();
        return fistElementType.getPackageName().contains("java.lang") || fistElementType.isPrimitive();
    }

    /**
     * Adds data object class field names as column headers in first row csv string data.
     * @param fields - array of {@link Field} of declared fields object's class.
     * */
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

    /**
     * Converts and adds object's fields values into column,s values in next row csv data.
     * @param object - object, whose must be converted to csv row.
     * @param objectFields - array of {@link Field} of declared fields object's class.
     * */
    private void appendCsvRow(Object object, Field[] objectFields) {
        for (Field field : objectFields) {
            if (field.isAnnotationPresent(CsvExclude.class)) continue;
            final Object fieldValue = getFieldValue(object, field);
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

    /**
     * @return value of object's field, whose received as objectField parameter.
     * @param object - object, whose must be converted to csv row.
     * @param objectField - array of {@link Field} of declared fields object's class.
     * */
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

    /**
     * Converts and adds collection type field's values to csv row.
     * conversion mode defined value of variable {@link SimpleCsvConverter#isConvertCollectionAsMultipleCell()}
     * @param fieldValue - field's value, whose type is collection.
     * */
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

    /**
     * Converts and adds collection type field's values into csv row as single cell.
     * Converts collection's values in single cell,where values will be set in brackets with separator " |".
     * @param collectionValue - field's value, whose type is collection.
     * */
    private void appendCollectionAsCsvCell(Object collectionValue){
        if (collectionValue == null) {
            fileData.append(cellSeparator);
            return;
        }
        final String cellValue = collectionValue.toString().replaceAll(cellSeparator, " |");
        fileData.append(cellValue).append(cellSeparator);
    }

    /**
     * Converts and adds collection type field's values into csv row as multiple cells,
     * where each value will be set in its cells.
     * @param collectionValue - field's value, whose type is collection.
     * */
    private void appendCollectionAsCsvCells(Object collectionValue){
        if (collectionValue != null) {
            final Collection<?> collection = (Collection<?>) collectionValue;
            for (var element : collection) {
                fileData.append(element.toString()).append(cellSeparator);
            }
            removeLastCellSeparator();
        }
    }

    /**Removes unnecessary cell separators from StringBuilder data*/
    private void removeLastCellSeparator() {
        if (fileData.length() > cellSeparator.length()) {
            fileData.delete(fileData.length() - cellSeparator.length(), fileData.length());
        }
    }



}
