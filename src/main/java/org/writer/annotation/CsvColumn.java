package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания, какие поля класса должны быть записаны в CSV.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {

    /**
     * Имя столбца в CSV.
     */
    String name() default "";

    /**
     * Порядок следования столбца в CSV.
     */
    int order() default 0;
}
