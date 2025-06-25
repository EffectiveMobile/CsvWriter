package org.writer;

import lombok.RequiredArgsConstructor;
import org.writer.exception.ClassNotSupportedException;
import org.writer.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
public class SimpleCsvConverter implements CsvConverter {
    private final static String CLASS_SUPPORT_ANNOTATION = "org.writer.annotation.Csv";
    private static final String CELL_SEPARATOR = ",";

    @Override
    public String toCsvString(List<?> objects) throws IllegalAccessException {
        StringBuilder fileData =  new StringBuilder();
        Field[] fields = ReflectionUtils.getAllFields(objects.get(0));
        if (!isValidList(objects)) {
            throw new IllegalArgumentException("The input list contains objects of different classes." +
                    " The CSV data will not be correct");
        }
        if (!isClassSupported(objects.get(0))) {
            throw new ClassNotSupportedException("Input list object class, not annotated with @Csv" +
                    " and not supported by conversion");
        }
        for (var object : objects) {
                fileData.append(objectToCsvRow(object, fields));
                fileData.append(System.lineSeparator());
        }
        return fileData.toString();
    }

    private boolean isValidList(List<?> objects){
        Class<?> classType = ReflectionUtils.getClassType(objects.get(0));
        List<?> list = objects.stream()
                .map(Object::getClass)
                .filter(aClass -> !aClass.equals(classType))
                .toList();
        return list.isEmpty();
    }

    private boolean isClassSupported(Object object) {
        return ReflectionUtils.isAnnotatedBy(object, CLASS_SUPPORT_ANNOTATION);
    }

    private String objectToCsvRow(Object object, Field[] fields) throws IllegalAccessException {
        StringBuilder line = new StringBuilder();
        for (Field field : fields) {
            if (ReflectionUtils.isCollection(field)) {
                var value = ReflectionUtils.getPrivateFieldValue(object, field);
                line.append(collectionFieldToCsvCells(value));
            } else {
                var value = ReflectionUtils.getPrivateFieldValue(object, field);
                if (value != null) {
                    line.append(value.toString());
                }
                line.append(CELL_SEPARATOR);
            }
        }
        removeLastCellSeparator(line);
        return line.toString();
    }


    /**Convert collection into simple field value*/
    private String collectionFieldToCsvCell(Object value){
        if (value != null) {
           return value.toString().replaceAll(CELL_SEPARATOR, " |");
        }
        return "";
    }

    /**Convert a collection into multiple columns*/
    private String collectionFieldToCsvCells(Object value){
        if (value == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        List<?> valueList = (List<?>) value;
        for (var element : valueList) {
            builder.append(element.toString());
            builder.append(CELL_SEPARATOR);
        }
        return builder.toString();
    }

    private void removeLastCellSeparator(StringBuilder builder) {
        if (builder.length() > CELL_SEPARATOR.length()) {
            builder.replace(builder.length() - CELL_SEPARATOR.length(), builder.length() - 1, "");
        }
    }



}
