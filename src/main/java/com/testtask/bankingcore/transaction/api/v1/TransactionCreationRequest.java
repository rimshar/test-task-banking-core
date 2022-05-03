package com.testtask.bankingcore.transaction.api.v1;

import com.testtask.bankingcore.common.validation.ValueOfEnum;
import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Builder
public class TransactionCreationRequest {
    @NotNull(message = "Amount must not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Invalid amount")
    private BigDecimal amount;
    @NotNull(message = "Currency must not be empty")
    private String currency;
    @ValueOfEnum(enumClass = TransactionDirection.class, message = "Transaction direction must be IN or OUT")
    private String direction;
    @Size(min = 3, max = 140, message = "Description must be between 3 and 140 characters")
    private String description;
}
