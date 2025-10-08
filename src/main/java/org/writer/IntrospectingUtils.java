package org.writer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Утилитный класс содержащий методы для упрощения работы по интроспектированию
 */
final class IntrospectingUtils {

    /**
     * Позволяет определить реализует ли класс интерфейс {@link java.util.Collections}
     * @param clazz класс, который необходимо проверить
     * @return {@code true} - если класс реализует интерфейс {@link Collection}, иначе {@code false}
     */
    public static boolean isCollection(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        if (clazz.equals(Objects.class)) {
            return false;
        }

        if (clazz.equals(Collection.class)) {
            return true;
        }

        return Arrays.stream(clazz.getInterfaces()).anyMatch(IntrospectingUtils::isCollection);
    }

}
