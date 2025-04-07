package org.writer.csv;

import java.lang.annotation.*;

/**
 * Аннотация для указания полей, которые должны быть записаны в CSV.
 * Может быть использована для задания пользовательского имени колонки.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {

//Имя столбца в CSV-файле.
    String name() default "";
}