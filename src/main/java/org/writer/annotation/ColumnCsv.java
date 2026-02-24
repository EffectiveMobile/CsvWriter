package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Кастомная аннотация для обозначения полей, которые необходимо добавить в CSV-файл
 * <p>
 *     Позволяет задать пользовательское имя столбца в итоговом файле.
 *     Если имя не указано, рекомендуется использовать имя самого поля.
 * </p>
 *
 * @author ZhelnovachevRoman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnCsv {

    /**
     * Имя колонки в CSV-файле.
     *
     * @return имя колонки
     */
    String name() default "";
}
