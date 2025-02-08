package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для пометки полей, которые должны быть включены в CSV-файл.
 * Эта аннотация используется для указания того, что поле класса должно быть
 * записано в CSV-файл, а также позволяет задать имя столбца в заголовке CSV.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {

    /**
     * Имя столбца в CSV-файле. Если имя не указано, будет использовано имя поля.
     *
     * @return строка, представляющая имя столбца. По умолчанию — пустая строка.
     */
    String name() default "";
}
