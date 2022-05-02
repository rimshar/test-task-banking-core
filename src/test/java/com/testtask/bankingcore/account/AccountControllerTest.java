package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.account.exception.AccountNotFoundException;
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
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isBadRequest;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isOk;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@RequiredArgsConstructor
class AccountControllerTest {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Test
    void happy_path_account_saved() {
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
            .body("accountId", notNullValue())
            .body(isJsonEqualTo(expected));
    }

    @Test
    void happy_path_account_retrieved() {
        val customerId = customerService.createCustomer("Test customer");

        val accountId = saveAccount(createRequest(customerId, "EE", List.of("EUR", "USD")))
            .then()
            .extract()
            .jsonPath()
            .getLong("accountId");

        String expected = """
            {
                accountId: %s,
                customerId: %s,
                balances: [
                   { amount: 0.00, currency: EUR },
                   { amount: 0.00, currency: USD }
                ]
            }
            """.formatted(accountId, customerId);

        getAccount(accountId)
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body(isJsonEqualTo(expected));
    }

    @Test
    void exception_thrown_on_invalid_currency() {
        val customerId = customerService.createCustomer("Test customer");

        saveAccount(createRequest(customerId, "EE", List.of("EUR", "JPY")))
            .then()
            .assertThat()
            .statusCode(isBadRequest())
            .contentType(ContentType.JSON)
            .body("message", equalTo("Invalid currency: JPY"));

        assertThrows(AccountNotFoundException.class, () -> accountService.findByCustomerId(customerId));
    }

    private Response saveAccount(AccountCreationRequest request) {
        return RestAssured.given()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/v1/accounts");
    }


    private Response getAccount(Long accountId) {
        return RestAssured.get("/api/v1/accounts/" + accountId);
    }

    private AccountCreationRequest createRequest(Long customerId, String countryCode, List<String> currencies) {
        return AccountCreationRequest.builder()
            .customerId(customerId)
            .country(countryCode)
            .currencies(currencies)
            .build();
    }
}
