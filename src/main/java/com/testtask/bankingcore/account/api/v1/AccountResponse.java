package com.testtask.bankingcore.account.api.v1;

import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record AccountResponse(
    String accountId,
    String customerId,
    List<BalanceResponse> balances
) {}
