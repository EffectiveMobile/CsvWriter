package org.writer.csv.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация-маркер для исключения поля из сериализации в CSV-документ.
 * <p>
 * Поля, помеченные этой аннотацией, будут проигнорированы при формировании
 * заголовков и значений в {@code CsvDocument}.
 * </p>
 *
 * <p>Особенности:</p>
 * <ul>
 *   <li>Применяется только к полям класса.</li>
 *   <li>Сохраняется во время выполнения ({@link RetentionPolicy#RUNTIME}),
 *       что позволяет проверять её через рефлексию.</li>
 *   <li>Не влияет на сам объект, только на процесс сериализации.</li>
 * </ul>
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * public class Person {
 *     private String name;
 *
 *     @Transient
 *     private String password; // не попадёт в CSV
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transient {
}
