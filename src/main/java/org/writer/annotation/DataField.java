package org.writer.annotation;

import org.writer.annotation.constans.NamingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, как поле объекта должно быть отображено на именованную колонку/поле
 * в сериализованном представлении данных.
 * Позволяет задать явное имя или стратегию именования.
 *
 * @author Астонский Шпион
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {

    /**
     * Явное имя колонки/поля в сериализованном представлении.
     * Если указано, имеет приоритет над стратегией именования.
     */
    String name() default "";

    /**
     * Стратегия именования для этого поля.
     * Если name() не указан, будет использована эта стратегия.
     * Если и name() и strategy() не указаны (или strategy() = DEFAULT),
     * будет использована defaultNamingStrategy из @CsvRecord (или аналогичной аннотации уровня класса),
     * или стратегия по умолчанию библиотеки.
     */
    NamingStrategy strategy() default NamingStrategy.DEFAULT;
}
