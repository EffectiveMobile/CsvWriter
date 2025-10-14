package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что поле класса должно быть включено в CSV файл как колонка
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
    /**
     * Имя колонки в CSV файле. Если не указано, то будет использовано имя поля.
     * @return имя колонки
     */
    String name() default "";
}
