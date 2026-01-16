package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация {@code CsvColumn} используется для пометки полей класса,
 * которые должны быть включены в CSV‑отчёт.
 * <p>
 * Может применяться только к полям. Сохраняется во время выполнения,
 * что позволяет работать с ней через Reflection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
    /**
     * Имя колонки в CSV‑файле, соответствующее данному полю.
     *
     * @return название столбца в CSV
     */
    String name();
}
