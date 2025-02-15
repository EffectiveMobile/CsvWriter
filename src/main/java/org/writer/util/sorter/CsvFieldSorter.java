package org.writer.util.sorter;

import lombok.experimental.UtilityClass;
import org.writer.annotation.CsvFieldOrder;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for sorting fields based on the {@link CsvFieldOrder} annotation.
 */
@UtilityClass
public class CsvFieldSorter {

    /**
     * Sorts fields based on the {@link CsvFieldOrder} annotation.
     *
     * @param fields the list of fields to be sorted.
     */
    public static void sortFields(List<Field> fields) {
        fields.sort(Comparator.comparingInt(field -> {
            CsvFieldOrder order = field.getAnnotation(CsvFieldOrder.class);
            return order != null ?
                    order.value() :
                    Integer.MAX_VALUE;
        }));
    }
}
