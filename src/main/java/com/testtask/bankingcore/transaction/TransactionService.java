package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.account.AccountService;
import com.testtask.bankingcore.balance.BalanceService;
import com.testtask.bankingcore.currency.CurrencyService;
import com.testtask.bankingcore.transaction.api.v1.TransactionCreationRequest;
import com.testtask.bankingcore.transaction.api.v1.TransactionCreationResponse;
import com.testtask.bankingcore.transaction.api.v1.TransactionRetrievalResponse;
import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import com.testtask.bankingcore.transaction.notification.TransactionCreationNotificationMessage;
import com.testtask.bankingcore.transaction.notification.TransactionNotificationRabbitClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper;
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;
    private final TransactionNotificationRabbitClient rabbitClient;

    public TransactionCreationResponse createTransaction(Long accountId, TransactionCreationRequest request) {
        accountService.findById(accountId);

        val transaction = createTransactionRecord(accountId, request);
        mapper.save(transaction);

        rabbitClient.sendCreationNotification(createTransactionCreationNotificationMessage(transaction, request.getCurrency()));

        val balance = balanceService.updateCurrencyBalance(
            transaction.getAccountId(),
            transaction.getCurrencyId(),
            transaction.getAmount(),
            transaction.getDirection()
        );

        return TransactionCreationResponse.builder()
            .accountId(accountId)
            .transactionId(transaction.getId())
            .amount(request.getAmount())
            .currency(request.getCurrency())
            .direction(TransactionDirection.valueOf(request.getDirection()))
            .description(request.getDescription())
            .updatedBalance(balance.getAmount())
            .build();
    }

    public List<TransactionRetrievalResponse> getTransactions(Long accountId) {
        accountService.findById(accountId);

        val transactions = mapper.findByAccountId(accountId);

        return transactions.stream().map(
            record -> TransactionRetrievalResponse.builder()
                .accountId(record.getAccountId())
                .transactionId(record.getId())
                .amount(record.getAmount())
                .currency(currencyService.findCodeById(record.getCurrencyId()))
                .direction(record.getDirection())
                .description(record.getDescription())
                .build()
        ).toList();
    }

    private TransactionRecord createTransactionRecord(Long accountId, TransactionCreationRequest request) {
        return TransactionRecord.builder()
            .accountId(accountId)
            .amount(request.getAmount())
            .currencyId(currencyService.findIdByCode(request.getCurrency()))
            .direction(TransactionDirection.valueOf(request.getDirection()))
            .description(request.getDescription())
            .build();
    }

    private TransactionCreationNotificationMessage createTransactionCreationNotificationMessage(
        TransactionRecord transaction,
        String currency
    ) {
        return TransactionCreationNotificationMessage.builder()
            .transactionId(transaction.getId())
            .amount(transaction.getAmount())
            .currency(currency)
            .direction(transaction.getDirection())
            .description(transaction.getDescription())
            .build();
    }
}
