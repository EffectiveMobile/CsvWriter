package org.writer;

import java.lang.annotation.*;

/**
 * Annotation for mapping a Java field to a CSV column.
 * The 'name' value specifies the CSV column header.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {
    /**
     * @return Name of the CSV column. Defaults to "Default Column Name".
     */
    String name() default "Default Column Name";
}
