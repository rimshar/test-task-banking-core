package com.testtask.bankingcore.currency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyRecord {
    private Long id;
    private String currencyCode;
}
