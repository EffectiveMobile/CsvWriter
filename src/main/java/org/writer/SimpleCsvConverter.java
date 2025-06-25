package org.writer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class SimpleCsvConverter implements CsvConverter {
    private static final String CELL_SEPARATOR = ",";


    @Override
    public String convertToCsvRow(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder line = new StringBuilder();
        for (Field f : fields) {
            f.setAccessible(true);
            if (isCollection(f)) {
                var value = f.get(object);
                line.append(collectionFieldToCsvCells(value));
            } else {
                var value = f.get(object).toString();
                line.append(value).append(CELL_SEPARATOR);
            }
        }
        removeLastCellSeparator(line);
        return line.toString();
    }
    private boolean isCollection(Field field){
        return Collection.class.isAssignableFrom(field.getType());
    }

    /**Convert collection as simple field value*/
    private String collectionFieldToCsvCell(Object value){
        return value.toString();
    }

    /**Convert collection as columns*/
    private String collectionFieldToCsvCells(Object value){
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
