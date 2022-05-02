package com.testtask.bankingcore.transaction.api.v1;

import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
public class TransactionRequest {
    @NotNull(message = "AccountID must not be empty")
    private Long accountId;
    @NotNull(message = "Amount must not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Invalid amount")
    private BigDecimal amount;
    @NotNull(message = "Currency must not be empty")
    private String currency;
    @NotNull(message = "Direction must not be empty")
    private TransactionDirection direction;
    @Size(min = 3, max = 140, message = "Description must be between 3 and 140 characters")
    private String description;
}
