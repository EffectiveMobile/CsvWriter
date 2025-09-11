package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Класс сущности Student
 */

@Data
@Builder
@AllArgsConstructor
public class Student {
    /** Поле имя */
    private String name;
    /** Поле счета, представляет из себя список String */
    private List<String> score;
}