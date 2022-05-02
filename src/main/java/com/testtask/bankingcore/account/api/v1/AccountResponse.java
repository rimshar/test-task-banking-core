package com.testtask.bankingcore.account.api.v1;

import com.testtask.bankingcore.common.Money;
import lombok.Builder;

import java.util.List;

@Builder
public record AccountResponse(
    Long accountId,
    Long customerId,
    List<Money> balances
) {}
