package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import com.testtask.bankingcore.balance.exception.InsufficientFundsException;
import com.testtask.bankingcore.balance.exception.UnsupportedCurrencyException;
import com.testtask.bankingcore.common.Money;
import com.testtask.bankingcore.currency.CurrencyService;
import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceMapper mapper;
    private final CurrencyService currencyService;

    public BalanceResponse createBalance(Money balance, Long accountId) {
        mapper.save(
            BalanceRecord.builder()
                .currencyId(currencyService.findIdByCode(balance.getCurrency()))
                .amount(balance.getAmount())
                .accountId(accountId)
                .build()
        );

        return BalanceResponse.builder()
            .balance(balance)
            .accountId(accountId)
            .build();
    }

    public BalanceRecord updateCurrencyBalance(
        Long accountId,
        Long currencyId,
        BigDecimal amount,
        TransactionDirection direction
    ) {
        val balance = mapper.findCurrencyBalance(accountId, currencyId)
            .orElseThrow(() -> new UnsupportedCurrencyException(accountId, currencyService.findCodeById(currencyId)));

        if (direction == TransactionDirection.OUT) {
            val updatedAmount = balance.getAmount().subtract(amount);
            if (updatedAmount.compareTo(BigDecimal.ZERO) > 0) {
                throw new InsufficientFundsException();
            }
            balance.setAmount(updatedAmount);
        } else {
            balance.setAmount(balance.getAmount().add(amount));
        }

        mapper.updateBalance(balance);

        return balance;
    }
}
