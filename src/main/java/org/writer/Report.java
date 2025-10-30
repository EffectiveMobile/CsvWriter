package org.writer;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Класс-обёртка над {@link Writable}, предоставляющий удобный метод
 * для записи коллекции объектов в документ.
 * <p>
 * Использует внедрённую стратегию {@link Writable}, что позволяет
 * подменять реализацию (например, запись в CSV, JSON, XML и т.д.)
 * без изменения кода клиента.
 * </p>
 *
 * <p>Пример использования:</p>
 * <pre>{@code
 * Writable csvWriter = new CsvDocument();
 * Report report = new Report(csvWriter);
 * report.writeObjectAsDoc(List.of(new Person("Alice", 30)), "people");
 * }</pre>
 */
@RequiredArgsConstructor
public class Report {

    /**
     * Стратегия записи, определяющая формат выходного документа.
     */
    private final Writable writable;

    /**
     * Записывает список объектов в документ, используя внедрённую стратегию {@link Writable}.
     *
     * @param data     список объектов для сериализации
     * @param fileName имя файла (без расширения, если реализация добавляет его сама)
     * @throws RuntimeException если произошла ошибка при записи
     */
    public void writeObjectAsDoc(List<?> data, String fileName) {
        writable.writeToFile(data, fileName);
    }
}
