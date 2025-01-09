package writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import writer.csv.Csv;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @Csv(headerName = "First name")
    private String firstName;

    @Csv(headerName = "Last name")
    private String lastName;

    @Csv(headerName = "Day of birth")
    private int dayOfBirth;

    @Csv(headerName = "Month of birth")
    private Months monthOfBirth;

    @Csv(headerName = "Year of birth")
    private int yearOfBirth;

}
