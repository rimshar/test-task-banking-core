package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import com.testtask.bankingcore.balance.exception.InsufficientFundsException;
import com.testtask.bankingcore.balance.exception.UnsupportedCurrencyException;
import com.testtask.bankingcore.balance.notification.BalanceCreationNotificationMessage;
import com.testtask.bankingcore.balance.notification.BalanceNotificationRabbitClient;
import com.testtask.bankingcore.balance.notification.BalanceUpdateNotificationMessage;
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
    private final BalanceNotificationRabbitClient rabbitClient;

    public BalanceResponse createBalance(Money balance, Long accountId) {
        val balanceRecord = createBalanceRecord(balance, accountId);

        mapper.save(balanceRecord);

        rabbitClient.sendCreationNotification(
            createBalanceCreationNotificationMessage(
                balanceRecord,
                balance.getCurrency()
            )
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
        val currencyCode = currencyService.findCodeById(currencyId);

        val balance = mapper.findCurrencyBalance(accountId, currencyId)
            .orElseThrow(() -> new UnsupportedCurrencyException(accountId, currencyCode));

        if (direction == TransactionDirection.OUT && balance.getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        balance.setAmount(calculateBalanceChange(balance.getAmount(), amount, direction));

        mapper.update(balance);

        rabbitClient.sendUpdateNotification(createBalanceUpdateNotificationMessage(balance, currencyCode));

        return balance;
    }

    private BalanceRecord createBalanceRecord(Money balance, Long accountId) {
        return BalanceRecord.builder()
            .currencyId(currencyService.findIdByCode(balance.getCurrency()))
            .amount(balance.getAmount())
            .accountId(accountId)
            .build();
    }

    private BalanceCreationNotificationMessage createBalanceCreationNotificationMessage(BalanceRecord record, String currency) {
        return BalanceCreationNotificationMessage.builder()
            .balanceId(record.getId())
            .accountId(record.getAccountId())
            .amount(record.getAmount())
            .currency(currency)
            .build();
    }

    private BalanceUpdateNotificationMessage createBalanceUpdateNotificationMessage(BalanceRecord record, String currency) {
        return BalanceUpdateNotificationMessage.builder()
            .accountId(record.getAccountId())
            .balanceId(record.getId())
            .currency(currency)
            .updatedBalance(record.getAmount())
            .build();
    }

    private BigDecimal calculateBalanceChange(BigDecimal balance, BigDecimal transactionAmount, TransactionDirection direction) {
        return direction.calculateAmount(balance, transactionAmount);
    }
}
