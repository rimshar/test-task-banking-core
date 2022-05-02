package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.account.AccountService;
import com.testtask.bankingcore.balance.BalanceService;
import com.testtask.bankingcore.currency.CurrencyService;
import com.testtask.bankingcore.transaction.api.v1.TransactionRequest;
import com.testtask.bankingcore.transaction.api.v1.TransactionResponse;
import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper;
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;

    public TransactionResponse createTransaction(TransactionRequest request) {
        accountService.findById(request.getAccountId());

        val transaction = createTransactionRecord(request);
        mapper.save(transaction);

        val balance = balanceService.updateCurrencyBalance(
            transaction.getAccountId(),
            transaction.getCurrencyId(),
            transaction.getAmount(),
            transaction.getDirection()
        );

        return TransactionResponse.builder()
            .accountId(request.getAccountId())
            .transactionId(transaction.getId())
            .amount(request.getAmount())
            .currency(request.getCurrency())
            .direction(TransactionDirection.valueOf(request.getDirection()))
            .description(request.getDescription())
            .updatedBalance(balance.getAmount())
            .build();
    }

    private TransactionRecord createTransactionRecord(TransactionRequest request) {
        return TransactionRecord.builder()
            .accountId(request.getAccountId())
            .amount(request.getAmount())
            .currencyId(currencyService.findIdByCode(request.getCurrency()))
            .direction(TransactionDirection.valueOf(request.getDirection()))
            .description(request.getDescription())
            .build();
    }
}
