package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для маркировки полей,
 * которые должны быть включены в CSV
 *
 * @author Sidorin Aleksei
 * @version 1.0
 * @since 16.10.2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CSVField {
    /**
     * Название колонки в CSV файле
     * @return название колонки
     */
    String name() default "";

    /**
     * Порядок колонки в CSV файле
     * @return порядковый номер
     */
    int order() default 0;

    /**
     * Формат для преобразования значения (например, для дат)
     * @return строка формата
     */
    String format() default "";
}
