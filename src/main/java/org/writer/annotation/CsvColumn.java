package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает поле класса для включения в CSV-файл.
 *
 * @see CsvEntity
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvColumn {
    /**
     * Название столбца в заголовке CSV-файла.
     *
     * @return название столбца
     */
    String name();

    /**
     * Порядок столбца в CSV-файле (слева направо), начиная с 1.
     * Чем меньше число, тем левее столбец.
     *
     * @return порядковый номер столбца
     */
    int order();
}
