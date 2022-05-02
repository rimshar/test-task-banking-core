package com.testtask.bankingcore.account.api.v1;

import com.testtask.bankingcore.common.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long accountId;
    private Long customerId;
    private List<Money> balances;
}
