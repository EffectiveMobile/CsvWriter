package org.writer.service.impl.constants;

import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ConstantsForCsvWritableImpl {

    public static final List<Student> DATA_WITH_LIST_FIELD ;
    public static final List<Person> DATA ;

    public static final Student STUDENT1;
    public static final Student STUDENT2;
    public static final Student STUDENT3;
    public static final Student STUDENT4;

    public static final Person PERSON1;
    public static final Person PERSON2;
    public static final Person PERSON3;




    static {
        STUDENT1 = new Student("Oleg1", List.of(1, 2, 3, 4));
        STUDENT2 = new Student("Oleg2", List.of(5, 6, 7, 8));
        STUDENT3 = new Student("EmptyTest", new ArrayList<>());
        STUDENT4 = new Student(null, List.of(99, 100));

        PERSON1 = new Person("Karl1","Ivanov1",15, Months.JANUARY,1999);
        PERSON2 = new Person("Karl2","Ivanov2",15, Months.MARCH,2005);
        PERSON3 = new Person("Karl3","Ivanov3",15, Months.APRIL,1957);

        DATA_WITH_LIST_FIELD = new ArrayList<>();
        DATA_WITH_LIST_FIELD.addAll(List.of(STUDENT1, STUDENT2, STUDENT3, STUDENT4));

        DATA = new ArrayList<>();
        DATA.addAll(List.of(PERSON1, PERSON2, PERSON3));

    }

}
