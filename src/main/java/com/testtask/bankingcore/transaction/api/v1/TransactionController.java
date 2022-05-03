package com.testtask.bankingcore.transaction.api.v1;

import com.testtask.bankingcore.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    TransactionCreationResponse createTransaction(
        @PathVariable Long accountId,
        @Valid @RequestBody TransactionCreationRequest request
    ) {
        return transactionService.createTransaction(accountId, request);
    }

    @GetMapping
    List<TransactionRetrievalResponse> getTransactions(@PathVariable Long accountId) {
        return transactionService.getTransactions(accountId);
    }
}
