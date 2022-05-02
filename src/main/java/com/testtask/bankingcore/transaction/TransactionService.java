package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.currency.CurrencyService;
import com.testtask.bankingcore.transaction.api.v1.TransactionRequest;
import com.testtask.bankingcore.transaction.api.v1.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper;
    private final CurrencyService currencyService;

    public TransactionResponse createTransaction(TransactionRequest request) {
        val transaction = createTransactionRecord(request);
        mapper.save(transaction);


        return TransactionResponse.builder().build();
    }

    private TransactionRecord createTransactionRecord(TransactionRequest request) {
        return TransactionRecord.builder()
            .accountId(request.getAccountId())
            .amount(request.getAmount())
            .currencyId(currencyService.findIdByCode(request.getCurrency()))
            .direction(request.getDirection())
            .description(request.getDescription())
            .build();
    }
}
