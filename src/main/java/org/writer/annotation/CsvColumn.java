package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания порядка и имени столбцов в CSV-файле.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {

    /**
     * Порядок столбца в CSV.
     * @return порядок столбца
     */
    int order();

    /**
     * Имя столбца в CSV. Если не указано, используется имя поля.
     * @return имя столбца
     */
    String name() default "";
}