package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.MaskedField;
import org.writer.annotation.csv.CsvRecord;
import org.writer.annotation.TransientField;
import org.writer.annotation.constans.MaskingStrategy;
import org.writer.annotation.constans.NamingStrategy;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@CsvRecord(defaultNamingStrategy = NamingStrategy.CAMEL_TO_SCREAMING_SNAKE_CASE)
public class Client {

    private String firstName;
    @TransientField
    private String lastName;
    @MaskedField(maskCharacter = 'X', strategy = MaskingStrategy.ASTERISKS_PARTIAL_PREFIX)
    private String accountNumber;
    private BigDecimal amountToPay;
}
