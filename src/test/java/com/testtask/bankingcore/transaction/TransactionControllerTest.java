package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.account.AccountService;
import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.common.meta.Annotations.IntegrationTest;
import com.testtask.bankingcore.customer.CustomerService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.testtask.bankingcore.common.matchers.JsonMatcher.isJsonEqualTo;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isBadRequest;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isOk;
import static org.hamcrest.Matchers.notNullValue;

@IntegrationTest
@RequiredArgsConstructor
public class TransactionControllerTest {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Test
    void happy_path_transaction_saved() {
        val customerId = customerService.createCustomer("Test customer");
        val accountId = accountService.createAccount(
            getAccountCreationRequest(customerId, "EE", List.of("EUR", "USD"))
        ).getAccountId();

        String expected = """
            {
                accountId: %s,
                amount: 100.50,
                currency: EUR,
                direction: in,
                description: Test transaction,
                updatedBalance: 100.50
            }
            """.formatted(accountId);

        createTransaction(
            getTransactionRequest(
                accountId, BigDecimal.valueOf(100.5000), "EUR", "IN", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body("transactionId", notNullValue())
            .body(isJsonEqualTo(expected));
    }

    @Test
    void invalid_currency() {
        val customerId = customerService.createCustomer("Test customer");
        val accountId = accountService.createAccount(
            getAccountCreationRequest(customerId, "EE", List.of("EUR", "USD"))
        ).getAccountId();

        String expected = """
            {
                message: Invalid currency: InvalidCurrency,
                path: /api/v1/transactions,
                error: Bad Request,
                status: 400
            }
            """;

        createTransaction(
            getTransactionRequest(
                accountId, BigDecimal.valueOf(100.5000), "InvalidCurrency", "IN", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(isBadRequest())
            .contentType(ContentType.JSON)
            .body("timestamp", notNullValue())
            .body(isJsonEqualTo(expected));
    }

    @Test
    void invalid_direction() {
        val customerId = customerService.createCustomer("Test customer");
        val accountId = accountService.createAccount(
            getAccountCreationRequest(customerId, "EE", List.of("EUR", "USD"))
        ).getAccountId();

        String expected = """
            {
                message: Validation Failed,
                details: [
                    Description must be between 3 and 140 characters,
                    Transaction direction must be IN or OUT
                ]
            }
            """;

        createTransaction(
            getTransactionRequest(
                accountId, BigDecimal.valueOf(100.5000), "EUR", "InvalidDirection", "iv"
            ))
            .then()
            .assertThat()
            .statusCode(isBadRequest())
            .contentType(ContentType.JSON)
            .body(isJsonEqualTo(expected));
    }

    private AccountCreationRequest getAccountCreationRequest(Long customerId, String country, List<String> currencies) {
        return AccountCreationRequest.builder()
            .customerId(customerId)
            .country(country)
            .currencies(currencies)
            .build();
    }

    private Response createTransaction(Map<String, Object> request) {
        return RestAssured.given()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/v1/transactions");
    }

    private Map<String, Object> getTransactionRequest(
        Long accountId,
        BigDecimal amount,
        String currency,
        String direction,
        String description
    ) {
        return Map.of(
            "accountId", accountId,
            "amount", amount,
            "currency", currency,
            "direction", direction,
            "description", description
        );
    }
}
