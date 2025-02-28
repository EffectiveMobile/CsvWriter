package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что поле должно быть записано в CSV.
 * Если header не указан, будет использовано имя поля.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvField {
    /**
     * Название столбца в CSV. Если не указано, используется имя поля.
     */
    String header() default "";
}
