package org.writer.csv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AAnnotation for marking fields that should be excluded
 * when converting to csv data using the CsvConverter interface.
 *
 * @Author Alexei Shvariov
 * @Date 26.06.2025
 * @Version 1.0
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvExclude {
}
