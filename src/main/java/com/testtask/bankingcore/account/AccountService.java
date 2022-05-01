package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.account.api.v1.AccountResponse;
import com.testtask.bankingcore.balance.BalanceMapper;
import com.testtask.bankingcore.balance.BalanceRecord;
import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import com.testtask.bankingcore.currency.CurrencyMapper;
import com.testtask.bankingcore.currency.CurrencyRecord;
import com.testtask.bankingcore.currency.exception.InvalidCurrencyException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;
    private final CurrencyMapper currencyMapper;

    public AccountResponse createAccount(AccountCreationRequest request) {
        val accountId = accountMapper.save(AccountRecord.fromCreationRequest(request)).id();

        val balances = saveBalances(accountId, getCurrencies(request));

        return AccountResponse.builder()
            .accountId(accountId)
            .customerId(request.customerId())
            .balances(balances)
            .build();
    }

    private List<CurrencyRecord> getCurrencies(AccountCreationRequest request) {
        return request.currencies().stream()
            .map(currency -> {
                    val currencyID = currencyMapper.findByCode(currency);

                    if (currencyID.isEmpty()) {
                        throw new InvalidCurrencyException(currency);
                    }

                    return currencyID.get();
                }
            ).toList();
    }

    private List<BalanceResponse> saveBalances(String accountId, List<CurrencyRecord> currencyIds) {
        return currencyIds.stream()
            .map(currency -> {
                    balanceMapper.save(
                        BalanceRecord.builder()
                            .accountId(accountId)
                            .amount(BigDecimal.valueOf(0))
                            .currencyId(currency.id())
                            .build()
                    );

                    return BalanceResponse.builder()
                        .availableAmount(BigDecimal.valueOf(0))
                        .currency(currency.currencyCode())
                        .build();
                }
            ).toList();
    }
}
