package org.writer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для помечания полей,
 * которые необходимо сохранять в
 * CSV файл
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Csv {
    /**
     * Имя колонки в CSV файле
     */
    String name() default "";
}
