package org.writer;

import java.lang.annotation.*;

/**
 * Аннотация для указания, что поле класса должно быть включено в CSV-файл при сериализации.
 * <p>
 * Используется {@link org.writer.CsvWriter} для генерации заголовков и значений CSV.
 * </p>
 *
 * <pre>
 * Пример использования:
 * {@code
 * public class Person {
 *     @CsvField(name = "Имя")
 *     private String firstName;
 *
 *     @CsvField(name = "Фамилия")
 *     private String lastName;
 * }
 * }
 * </pre>
 *
 * <p>
 * Параметр {@code name} задаёт название столбца в CSV-файле. Если не указан, желательно обрабатывать
 * это в {@code CsvWriter}, например, использовать имя поля.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvField {

    /**
     * Имя столбца в CSV-файле.
     *
     * @return строка, представляющая заголовок столбца
     */
    String name() default "";
}
