package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.account.api.v1.AccountResponse;
import com.testtask.bankingcore.account.exception.AccountNotFoundException;
import com.testtask.bankingcore.account.notification.AccountCreationNotificationMessage;
import com.testtask.bankingcore.account.notification.AccountNotificationRabbitClient;
import com.testtask.bankingcore.balance.BalanceService;
import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import com.testtask.bankingcore.common.Money;
import com.testtask.bankingcore.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final BalanceService balanceService;
    private final CustomerService customerService;
    private final AccountNotificationRabbitClient rabbitClient;

    public AccountResponse createAccount(AccountCreationRequest request) {
        val customerId = customerService.findById(request.customerId()).getId();

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

        rabbitClient.sendCreationNotification(creationNotificationMessage(balances.get(0).accountId(), request));

        return createAccountResponse(customerId, balances);
    }

    public AccountResponse findByCustomerId(Long customerId) {
        val response = accountMapper.findByCustomerId(customerId);
        return response.orElseThrow(AccountNotFoundException::new);
    }

    public AccountResponse findById(Long accountId) {
        val response = accountMapper.findById(accountId);
        return response.orElseThrow(AccountNotFoundException::new);
    }

    private AccountCreationNotificationMessage creationNotificationMessage(Long accountId, AccountCreationRequest request) {
        return AccountCreationNotificationMessage.builder()
            .accountId(accountId)
            .customerId(request.customerId())
            .countryCode(request.country())
            .build();
    }

    private AccountResponse createAccountResponse(Long customerId, List<BalanceResponse> balances) {
        return AccountResponse.builder()
            .accountId(balances.get(0).accountId())
            .customerId(customerId)
            .balances(balances.stream().map(BalanceResponse::balance).toList())
            .build();
    }
}
