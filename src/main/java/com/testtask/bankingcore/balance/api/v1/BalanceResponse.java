package com.testtask.bankingcore.balance.api.v1;

import com.testtask.bankingcore.common.Money;
import lombok.Builder;

@Builder
public record BalanceResponse(
    Money balance,
    Long accountId
) {}
