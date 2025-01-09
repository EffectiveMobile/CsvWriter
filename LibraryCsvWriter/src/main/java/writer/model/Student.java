package writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import writer.csv.Csv;


import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @Csv(headerName = "Name")
    private String name;

    @Csv(headerName = "Scores")
    private List<String> score;
}