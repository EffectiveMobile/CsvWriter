# CSV Writer

Библиотека для генерации CSV-файлов из объектов с использованием рефлексии и аннотаций.

## Описание

Проект реализует интерфейс `Writable` для записи списка объектов в CSV-файл. Поддерживает произвольные классы с полями, помеченными аннотацией `@CsvColumn`, включая коллекции (например, списки оценок в `Student`).

## Установка

1. Склонируйте репозиторий:
   ```bash
   git clone <your-repo-url>
   
2. Убедитесь, что Maven установлен
   ```bash
   mvn -v

3. Соберите проект:
   ```bash
   mvn clean install

## Использование

1. Создайте класс с полями, помеченными @CsvColumn:

``` 
@Data
public class Person {
    @CsvColumn(order = 0, name = "First Name")
    private String firstName;
    @CsvColumn(order = 1, name = "Last Name")
    private String lastName;
}
```
2. Используйте CsvWriterImpl:

``` 
CsvWriterImpl csvWriter = new CsvWriterImpl();
List<Person> people = List.of(new Person("John", "Doe"));
csvWriter.writeToFile(people, "people.csv");
``` 

Примеры
- Запись списка Person в people.csv.
- Запись списка Student с оценками в students.csv (см. Main.java).

Структура проекта

- src/main/java/org/writer/:
  - CsvWriterImpl — основная реализация.
  - Writable — интерфейс.
- src/main/java/org/writer/model/:
  - Person, Student, Months — модели данных.
- src/main/java/org/writer/annotation/:
  - CsvColumn — аннотация для полей.
- src/test/java/:
  - Тесты с Datafaker.

Зависимости
- Lombok — для упрощения моделей.
- Datafaker — для генерации тестовых данных.
- JUnit 5 — для unit-тестов.
