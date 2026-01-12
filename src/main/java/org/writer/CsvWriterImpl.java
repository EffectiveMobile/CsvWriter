package org.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Класс для записи данных в CSV-файл
 * Это самая простая реализация для записи данных в CSV-файл
 * Так как передаваемые обьекты могут содержать разные поля
 * Из-за чего не получится оптимальная реализация с генерацией отчета, тк столбцы будут разные.
 * А проверка полей на соответствие столбцу будет неоптимальна (имею опыт генерации excel отчетов в Java)
 *
 */
public class CsvWriterImpl implements Writable {


	/**
	 * Метод, который записывает данные в CSV-файл
	 * @param data список объектов, которые нужно записать
	 * @param fileName имя CSV-файла
	 */
	@Override
	public void writeToFile(List<?> data, String fileName) {

		if(data.isEmpty() || fileName.isBlank()) {
			throw new IllegalArgumentException("Data or file name is empty");
		}

		try (FileWriter fileWriter = new FileWriter(fileName)) {

			//Записываем каждый передаваемый объект в CSV-файл
			for (Object obj : data) {
				List<String> row = getFieldValues(obj);
				fileWriter.write(String.join(", ", row));
				fileWriter.write("\n");
			}

		} catch (IOException e) {
			throw new RuntimeException("Error writing to file", e);
		}
	}


	/**
	 * Метод для получения значений полей объекта
	 * @param object - объект
	 * @return список значений
	 */
	private List<String> getFieldValues(Object object) {
		return Arrays.stream(object.getClass().getDeclaredFields())
				.map(field -> {
					try {
						field.setAccessible(true);
						return field.get(object);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("Error getting field value", e);
					}
				})
				.map(Objects::toString)
				.toList();
	}


}
