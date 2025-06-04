package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, для указания метаданных поля при экспорте в Csv.
 * Применяется к полям класса для определения имени столбца и его позиции в итоговом Csv файле
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * Имя столбца в будущем Csv. Если не указано, то будет использоваться имя поля
     * @return имя столбца
     */
    String columnName() default "";

    /**
     * Позиция столбца при сортировке (отсчет начинается с 1).
     * По умолчанию столбец будет последним
     * @return позиция столбца
     */
    int position() default Integer.MAX_VALUE;
}
