package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация {@code CsvField} используется для указания, что поле класса должно быть включено в процесс
 * сериализации или десериализации CSV-файла. Позволяет задать имя столбца в CSV и порядок его следования.
 *
 * <p>
 * Аннотация применима только к полям класса ({@link ElementType#FIELD}) и сохраняется во время выполнения
 * программы ({@link RetentionPolicy#RUNTIME}).
 * </p>
 *
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvField {

    /**
     * Указывает имя столбца в CSV-файле, соответствующее аннотированному полю.
     * Если не указано, используется имя поля по умолчанию.
     *
     * @return имя столбца в CSV-файле
     */
    String columnName() default "";

    /**
     * Определяет порядок столбца в CSV-файле. Меньшие значения соответствуют более раннему положению.
     * По умолчанию равно 0.
     *
     * @return порядок столбца
     */
    int order() default 0;
}
