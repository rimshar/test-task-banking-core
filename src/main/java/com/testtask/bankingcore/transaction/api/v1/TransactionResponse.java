package com.testtask.bankingcore.transaction.api.v1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.testtask.bankingcore.config.BigDecimal2JsonDeserializer;
import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class TransactionResponse {
    private Long accountId;
    private Long transactionId;
    @JsonDeserialize(using = BigDecimal2JsonDeserializer.class)
    private BigDecimal amount;
    private String currency;
    private TransactionDirection direction;
    private String description;
    @JsonDeserialize(using = BigDecimal2JsonDeserializer.class)
    private BigDecimal updatedBalance;
}
