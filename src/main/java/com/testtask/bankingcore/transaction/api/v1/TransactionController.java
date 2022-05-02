package com.testtask.bankingcore.transaction.api.v1;

import com.testtask.bankingcore.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    TransactionResponse createTransaction(@Valid @RequestBody TransactionRequest request) {
        return service.createTransaction(request);
    }
}
