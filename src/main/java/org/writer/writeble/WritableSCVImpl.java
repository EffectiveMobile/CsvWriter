package org.writer.writeble;



import com.opencsv.CSVWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса Writable с использованием OpenCSV.
 * Поддерживает запись списков объектов в CSV-файл через рефлексию.
 */
public class WritableSCVImpl implements Writable {

    private static final Log log = LogFactory.getLog(WritableSCVImpl.class);

    /**
     * Метод реализации записи списка объектов в указанный файл.записи в CSV-файл
     * @param data     список объектов для записи
     * @param fileName имя CSV-файла
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {

        List<List<String>> resultList = data.stream()
                .map(this::objToArray)
                .toList();

        writeInCSV(resultList, fileName);



    }

    /**
     * Преобразует объект любого класса в список строк, представляющих значения его полей.
     * Использует рефлексию для получения значений всех declared полей, включая private.
     *
     * @param obj объект, который необходимо преобразовать в список строк
     * @return список строк, где каждая строка — это значение одного поля объекта;
     *         если поле null, будет вызвано value.toString() и может вызвать NullPointerException
     * @implNote Используется для подготовки данных перед записью в CSV.
     *           Все поля объекта будут конвертированы в строку в порядке их объявления в классе.
     *           Исключения доступа к полям (IllegalAccessException, SecurityException) логируются через log.error.
     */
    private List<String> objToArray(Object obj) {

        List<String> result = new ArrayList<>();

        Class<?> clazz = obj.getClass();
        log.info("class: " + clazz.getName());

        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                result.add(value.toString());
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.error(e);
        }

        return result;


    }

    /**
     * Записывает список строковых списков в CSV-файл.
     * Если файл уже существует, новые данные добавляются в конец.
     * Если файл не существует, он создаётся и данные записываются с начала.
     *
     * @param resultList список строковых списков, где каждая внутренняя List<String> — это одна строка CSV
     * @param fileName   имя CSV-файла (может содержать путь)
     * @implNote Используется OpenCSV для записи.
     *           Метод проверяет существование файла через Files.exists(path) и выбирает режим добавления или создания.
     *           Любые ошибки ввода/вывода логируются через log.error.
     */
    private void writeInCSV(List<List<String>> resultList, String fileName) {

        Path path = Paths.get(fileName);

        try {
            if (Files.exists(path)) {

                try (CSVWriter writer = new CSVWriter(new FileWriter(fileName, true))) {
                    for (List<String> row : resultList) {
                        writer.writeNext(row.toArray(new String[0]));
                    }
                }
                log.info("Запись добавлена");

            } else {

                try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
                    for (List<String> row : resultList) {
                        writer.writeNext(row.toArray(new String[0]));
                    }
                }
                log.info("Файл создан и заполнен начальными данными");
            }

        } catch (IOException e) {
            log.error(e);
        }


    }
}
