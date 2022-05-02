package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import com.testtask.bankingcore.common.Money;
import com.testtask.bankingcore.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceMapper mapper;
    private final CurrencyService currencyService;

    public BalanceResponse createBalance(Money balance, Long accountId) {
        mapper.save(
            BalanceRecord.builder()
                .currencyId(currencyService.findIdByCode(balance.currency()))
                .amount(balance.amount())
                .accountId(accountId)
                .build()
        );

        return BalanceResponse.builder()
            .balance(balance)
            .accountId(accountId)
            .build();
    }
}
