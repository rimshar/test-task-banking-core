package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.common.meta.Annotations.IntegrationTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

@IntegrationTest
@RequiredArgsConstructor
class AccountControllerTest {

    private final AccountService service;

    @Test
    void accountSavedSuccesfully() {
        saveAccount(createRequest("1234", "EE", List.of("EUR", "USD")));
    }

    private Response saveAccount(AccountCreationRequest request) {
        return RestAssured.post("/api/v1/accounts", request);
    }

    private AccountCreationRequest createRequest(String customerId, String countryCode, List<String> currencies) {
        return AccountCreationRequest.builder()
            .customerId(customerId)
            .country(countryCode)
            .currencies(currencies)
            .build();
    }
}
