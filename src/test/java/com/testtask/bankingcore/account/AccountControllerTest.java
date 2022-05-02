package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.common.meta.Annotations.IntegrationTest;
import com.testtask.bankingcore.customer.CustomerService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.testtask.bankingcore.common.matchers.JsonMatcher.isJsonEqualTo;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isOk;

@IntegrationTest
@RequiredArgsConstructor
class AccountControllerTest {

    private final AccountMapper mapper;
    private final AccountService accountService;
    private final CustomerService customerService;

    @Test
    void accountSavedSuccessfully() {
        val customerId = customerService.createCustomer("Test customer");

        String expected = """
            {
                customerId: %s,
                balances: [
                   { amount: 0.00, currency: EUR },
                   { amount: 0.00, currency: USD }
                ]
            }
            """.formatted(customerId);

        saveAccount(createRequest(customerId, "EE", List.of("EUR", "USD")))
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body(isJsonEqualTo(expected));
    }

    private Response saveAccount(AccountCreationRequest request) {
        return RestAssured.given()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/v1/accounts");
    }

    private AccountCreationRequest createRequest(Long customerId, String countryCode, List<String> currencies) {
        return AccountCreationRequest.builder()
            .customerId(customerId)
            .country(countryCode)
            .currencies(currencies)
            .build();
    }
}
