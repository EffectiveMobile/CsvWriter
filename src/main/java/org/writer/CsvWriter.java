package org.writer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.writer.annotations.CollectionMappingStrategy;
import org.writer.annotations.CsvInclude;
import org.writer.annotations.EnumMappingStrategy;
import org.writer.annotations.EscapeStrategy;
import org.writer.exceptions.CsvMappingException;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс позволяющий записать объекты в CSV-файл
 * </br></br>
 * Запись производится с помощью метода {@link Writable#writeToFile(List, String)}.
 * Перед записью в файл производится интроспектирование с целью определения структуры полей класса и определения
 * стратегии маппинга и экранирования. Затем идет накопление значений прошедших маппинг, а лишь затем производится запись в файл.
 * </br>
 * Таким образом для больших объемов данных данная реализация будет не сильно эффективна.
 */
public class CsvWriter implements Writable {

    /**
     * Класс позволяющий производить маппинг полей объекта в CSV-формат, применяя указанные стратегии
     */
    @AllArgsConstructor
    private static final class MappingPipeline {
        private final List<IntrospectNode> introspectNodes;

        private String mapEnum(Enum<?> enumValue, EnumMappingStrategy enumMappingStrategy) {
            return enumMappingStrategy == EnumMappingStrategy.NAME ? enumValue.name() : Integer.toString(enumValue.ordinal());
        }

        private String mapObject(Object object, EscapeStrategy escapeStrategy) {
            return escapeStrategy == EscapeStrategy.QUOTE_ESCAPING ? "\"" + object.toString() + "\"" : object.toString();
        }

        private String mapCollection(Collection<?> collection, CollectionMappingStrategy collectionMappingStrategy) {
            return collectionMappingStrategy == CollectionMappingStrategy.STRINGIFY ? "\"" + collection.toString() + "\"" :
                    collection.stream().map(Object::toString).collect(Collectors.joining(","));
        }

        /**
         * Формируем список замаппеных значений, переданного объекта
         *
         * @param object объект для маппинга
         * @return список строк - замаппеных значений полей объекта
         * @throws CsvMappingException в случаях если возникла непредвиденная ошибка, либо невозможно получить значение поля вследствие отсутствия доступа
         */
        public List<String> map(Object object) {
            Objects.requireNonNull(object);
            introspectNodes.forEach(node -> node.getField().setAccessible(true));

            try {
                return introspectNodes.stream().map(introspectNode -> {
                    try {
                        CsvInclude fieldMappingData = introspectNode.getMappingData();
                        Class<?> fieldClass = introspectNode.field.getType();
                        if (fieldClass.isPrimitive()) {
                            return introspectNode.getField().get(object).toString();
                        } else if (fieldClass.isEnum()) {
                            return mapEnum((Enum<?>) introspectNode.getField().get(object), fieldMappingData.enumMapping().strategy());
                        } else if (introspectNode.isCollection()) {
                            return mapCollection((Collection<?>) introspectNode.getField().get(object), fieldMappingData.collectionMapping().strategy());
                        } else {
                            return mapObject(introspectNode.getField().get(object), fieldMappingData.escape().strategy());
                        }
                    } catch (IllegalAccessException illegalAccessException) {
                        throw new CsvMappingException(
                                String.format("Violating field \"%s\" access while mapping object %s", introspectNode.getField().getName(), object)
                        );
                    }
                }).toList();
            } catch (CsvMappingException csvMappingException) {
                throw csvMappingException;
            } catch (Exception exception) {
                throw new CsvMappingException("Unexpected error occurred while mapping object", exception);
            } finally {
                introspectNodes.forEach(node -> node.getField().setAccessible(false));
            }
        }
    }

    /**
     * Класс содержащий информацию о поле объекта
     */
    @AllArgsConstructor
    @Getter
    private static final class IntrospectNode {
        private final Field field;
        private final CsvInclude mappingData;
        private final boolean isCollection;
    }

    /**
     * Получаем всю необходимую информацию о полях класса, которые необходимо записать в CSV-файл, после чего
     * создаем объект {@link MappingPipeline} для последующей обработки
     *
     * @param object объект структура полей которого будет интроспектирована
     * @return объект {@link MappingPipeline} для маппинга
     */
    private MappingPipeline introspect(Object object) {
        Objects.requireNonNull(object);
        List<IntrospectNode> introspectNodes = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvInclude.class))
                .map(field -> new IntrospectNode(field, field.getAnnotation(CsvInclude.class), IntrospectingUtils.isCollection(field.getType())))
                .toList();

        return new MappingPipeline(introspectNodes);
    }

    /**
     * Производит запись списка объектов в файл в CSV-формате. В данной реализации файл создается в директории откуда запущена программа.
     * @param data     список объектов для записи
     * @param fileName название файла, автоматически расширение {@code .csv} не проставляется, таким образом для сохранения расширения
     * вы должны указать его самостоятельно
     * @throws CsvMappingException в случаях если возникла непредвиденная ошибка, либо невозможно получить значение поля вследствие отсутствия доступа,
     * а также в случае попытки создания CSV-файла если такой файл уже существует
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (Objects.requireNonNull(data).isEmpty()) {
            throw new IllegalArgumentException("Expected non-empty data to write");
        }

        if (Objects.requireNonNull(fileName).isBlank()) {
            throw new IllegalArgumentException("Blank or empty filename");
        }

        MappingPipeline pipeline = introspect(data.get(0));

        // Складываем все замаппеные значения в список, конечно это не сильно эффективно по-сравнению другими реализациями,
        // но данная реализация пока, что для упрощения
        List<List<String>> mappedData = new ArrayList<>();
        data.forEach(value -> mappedData.add(pipeline.map(value)));


        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);
        try (Writer writer = new FileWriter(Files.createFile(filePath).toFile())) {
            mappedData.stream()
                    .map(mappedValues -> String.join(",", mappedValues))
                    .forEach(joinedValues -> {
                        try {
                            writer.write(joinedValues + "\n");
                        } catch (IOException internalIOException) {
                            throw new CsvMappingException("Unexpected error occurred while writing data to CSV file", internalIOException);
                        }
                    });
        } catch (IOException ioException) {
            throw new CsvMappingException("Error while creating file to writing data", ioException);
        }
    }

}
