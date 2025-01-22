package org.writer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Идентифицирует класс, объекты которого могут быть записаны в файл в формате CSV.
 *
 * @author Даниил Астафьев
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CSV {
}
