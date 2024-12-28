package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.writer.util.ClassLabel;
import org.writer.util.FieldMark;
import java.util.List;

@Data
@Builder
@ClassLabel
@NoArgsConstructor
@AllArgsConstructor
public class Scores {

    /**
     * очки
     */
    @FieldMark
    private List<Integer> scores;
}
