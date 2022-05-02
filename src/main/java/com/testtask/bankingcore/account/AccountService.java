package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.account.api.v1.AccountResponse;
import com.testtask.bankingcore.account.exception.AccountNotFoundException;
import com.testtask.bankingcore.balance.BalanceService;
import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import com.testtask.bankingcore.common.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final BalanceService balanceService;

    public AccountResponse createAccount(AccountCreationRequest request) {
        val account = AccountRecord.fromCreationRequest(request);
        accountMapper.save(account);

        val balances = request.currencies().stream().distinct()
            .map(currency ->
                balanceService.createBalance(
                    Money.builder()
                        .amount(BigDecimal.valueOf(0.00))
                        .currency(currency)
                        .build(),
                    account.getId()
                ))
            .toList();

        return AccountResponse.builder()
            .accountId(balances.get(0).accountId())
            .customerId(request.customerId())
            .balances(balances.stream().map(BalanceResponse::balance).toList())
            .build();
    }

    public AccountResponse findByCustomerId(Long customerId) {
        val response = accountMapper.findByCustomerId(customerId);
        return response.orElseThrow(AccountNotFoundException::new);
    }
}
