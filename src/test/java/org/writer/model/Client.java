package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvMasked;
import org.writer.annotation.CsvRecord;
import org.writer.annotation.CsvTransient;
import org.writer.annotation.constans.MaskingStrategy;
import org.writer.annotation.constans.NamingStrategy;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@CsvRecord(defaultNamingStrategy = NamingStrategy.CAMEL_TO_SCREAMING_SNAKE_CASE)
public class Client {

    private String firstName;
    @CsvTransient
    private String lastName;
    @CsvMasked(maskCharacter = 'X', strategy = MaskingStrategy.ASTERISKS_PARTIAL_PREFIX)
    private String accountNumber;
    private BigDecimal amountToPay;
}
