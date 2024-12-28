package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.writer.util.ClassLabel;
import org.writer.util.FieldMark;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@ClassLabel
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    /**
     * Возраст
     */
    @FieldMark
    private int age;

    /**
     * имя
      */
    @FieldMark
    private String firstName;

    /**
     * фамилия
     */
    @FieldMark
    private String lastName;

    /**
     * дата рождения
     */
   // @FieldMark
    private LocalDate dateOfBirth;
}
