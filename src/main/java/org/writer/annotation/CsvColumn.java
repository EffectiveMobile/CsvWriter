package org.writer.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация для указания имени колонки и порядка колонки
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {
    String name();
    int order();
}
