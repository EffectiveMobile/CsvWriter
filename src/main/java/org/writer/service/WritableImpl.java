package org.writer.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * класс для записи csv файла
 */
@RequiredArgsConstructor
public class WritableImpl implements Writable {

    private static final String PATH = "output/";

    private static final String END = ".csv";

    private static final String FILE = "data.csv";

    /**
     * Метод для записи на диск csv файла
     *
     * @param data     список сущностей для записи
     * @param fileName имя файла для записи
     */
    @Override
    public void writeToFile(List<?> data, final String head, final String fileName) {

        if (data == null || data.isEmpty()) {
            System.out.println("file empty");
            return;
        }

        this.headWriter(head, fileName);

        try (FileWriter writer = new FileWriter(fileName != null && !fileName.isBlank() ?

                PATH + fileName + END : PATH + FILE, true)) {

            for (Object i : data) {

                writer.append(i.toString()); //).append(";"
            }

            writer.write(System.lineSeparator());
            writer.flush();

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     *
     * @param head заголовок
     * @param fileName имя файла для записи
     */
    @SneakyThrows
    public void headWriter(final String head, final String fileName) {

        try (FileWriter writer = new FileWriter(fileName != null && !fileName.isBlank() ?

                PATH + fileName + END : PATH + FILE, true)) {

            writer.write(head);
            writer.append(System.lineSeparator());
            writer.flush();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
