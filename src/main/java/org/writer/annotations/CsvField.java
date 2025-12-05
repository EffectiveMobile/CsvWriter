package org.writer.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация для обозначения имени полей в csv файле.
 * Есть возможность игнорировать поля.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface CsvField {
    String value() default  "";
    boolean ignore() default  false;
}
