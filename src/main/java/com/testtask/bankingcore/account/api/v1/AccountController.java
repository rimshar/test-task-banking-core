package com.testtask.bankingcore.account.api.v1;

import com.testtask.bankingcore.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService service;

    @PostMapping
    AccountResponse createAccount(@RequestBody AccountCreationRequest request) {
        return service.createAccount(request);
    }

    @GetMapping("/{accountId}")
    AccountResponse getAccount(@PathVariable Long accountId) {
        return service.findById(accountId);
    }

}
