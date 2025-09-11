package org.writer;

import org.writer.exception.AccessFileException;
import org.writer.exception.InvalidDataException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Класс реализации записи данных в файл в формате CSV
 */

public class WritableImpl implements Writable {
    private final Logger logger = Logger.getLogger(WritableImpl.class.getName());
    private final StringBuilder stringBuilder = new StringBuilder();


    /**
     * Вадидация входных даннных
     * @param data - список обьектов
     * @param fileName - имя файла для записи
     * @throws InvalidDataException - бросает исключение
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
            if (validateMethod(data, fileName)) {
                try (FileWriter fw = new FileWriter(fileName))
                {
                    for (Object object : data) {
                        Class<?> aClass = object.getClass();
                        Field[] fields = aClass.getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            Object valueField = field.get(object);
                            resolveFieldClass(valueField);
                        }
                    }
                    if (!stringBuilder.isEmpty()) {
                        stringBuilder.setLength(stringBuilder.length() - 1);
                    }
                    fw.write(stringBuilder.toString());
                } catch (IllegalAccessException e) {
                    throw new InvalidDataException("Invalid data!");
                } catch (IOException e) {
                    throw new AccessFileException("Access file error!");
                }
            } else {
                throw new InvalidDataException("Invalid data!");
            }
    }

    /**
     * Вадидация входных даннных
     * @param data - список обьектов
     * @param fileName - имя файла для записи
     */
    private boolean validateMethod(List<?> data, String fileName) {
        if (data == null || fileName == null) {
            logger.warning("Data or File Name is null!");
            return false;
        } else if (data.isEmpty() || fileName.isEmpty()) {
            logger.warning("Data or File Name is empty!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Проверка типа поля и заполнение stringbuilder
     * @param valueField - поле обьекта
     */
    private void resolveFieldClass (Object valueField) {
        if (valueField instanceof Collection) {
            String collect = ((Collection<?>) valueField).stream().map(value -> value.toString()).collect(Collectors.joining("|"));
            stringBuilder.append(collect).append(',');
        } else {
            stringBuilder.append(valueField).append(',');
        }
    }
}
