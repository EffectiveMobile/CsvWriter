package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация CsvColumn используется для отмктки полей в моделях, которые которые необходимо сохранять в CSV.
 *
 * <p>Эта аннотация применяется к полям</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {
  String name();
}
