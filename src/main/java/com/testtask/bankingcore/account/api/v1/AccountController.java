package com.testtask.bankingcore.account.api.v1;

import com.testtask.bankingcore.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    AccountResponse createAccount(@Valid @RequestBody AccountCreationRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{accountId}")
    AccountResponse getAccount(@PathVariable Long accountId) {
        return accountService.findById(accountId);
    }
}
