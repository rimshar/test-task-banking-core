package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.account.AccountService;
import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.common.meta.Annotations.IntegrationTest;
import com.testtask.bankingcore.customer.CustomerService;
import com.testtask.bankingcore.transaction.api.v1.TransactionRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.testtask.bankingcore.common.matchers.JsonMatcher.isJsonEqualTo;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isBadRequest;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isOk;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.notFound;
import static org.hamcrest.Matchers.notNullValue;

@IntegrationTest
@RequiredArgsConstructor
public class TransactionControllerTest {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Test
    void happy_path_transaction_saved() {
        val customerId = createTestCustomer();
        val accountId = createAccount(customerId);

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
        val customerId = createTestCustomer();
        val accountId = createAccount(customerId);

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
    void unsupported_currency() {
        val customerId = createTestCustomer();
        val accountId = createAccount(customerId);

        String expected = """
            {
                message: Account %s does not support currency GBP,
                path: /api/v1/transactions,
                error: Not Found,
                status: 404
            }
            """.formatted(accountId);

        createTransaction(
            getTransactionRequest(
                accountId, BigDecimal.valueOf(100.5000), "GBP", "IN", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(notFound())
            .contentType(ContentType.JSON)
            .body("timestamp", notNullValue())
            .body(isJsonEqualTo(expected));
    }

    @Test
    void invalid_direction_and_description() {
        val customerId = createTestCustomer();
        val accountId = createAccount(customerId);

        String expected = """
            {
                message: Bad Request,
                details: [
                    Description must be between 3 and 140 characters,
                    Transaction direction must be IN or OUT,
                    Invalid amount
                ]
            }
            """;

        createTransaction(
            getTransactionRequest(
                accountId, BigDecimal.valueOf(-100.5000), "EUR", "InvalidDirection", "iv"
            ))
            .then()
            .assertThat()
            .statusCode(isBadRequest())
            .contentType(ContentType.JSON)
            .body(isJsonEqualTo(expected));
    }

    @Test
    void insufficient_funds() {
        val customerId = createTestCustomer();
        val accountId = createAccount(customerId);

        String expected = """
            {
                message: Insufficient funds,
                path: /api/v1/transactions,
                error: Bad Request,
                status: 400
            }
            """;

        createTransaction(
            getTransactionRequest(
                accountId, BigDecimal.valueOf(100.5000), "EUR", "OUT", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(isBadRequest())
            .contentType(ContentType.JSON)
            .body("timestamp", notNullValue())
            .body(isJsonEqualTo(expected));
    }

    @Test
    void account_missing() {
        String expected = """
            {
                message: Account not found,
                path: /api/v1/transactions,
                error: Not Found,
                status: 404
            }
            """;

        createTransaction(
            getTransactionRequest(
                123456L, BigDecimal.valueOf(100.5000), "EUR", "OUT", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(notFound())
            .contentType(ContentType.JSON)
            .body("timestamp", notNullValue())
            .body(isJsonEqualTo(expected));
    }

    private Long createTestCustomer() {
        return customerService.createCustomer("Test customer");
    }

    private Long createAccount(Long customerId) {
        return accountService.createAccount(
            getAccountCreationRequest(customerId, "EE", List.of("EUR", "USD"))
        ).getAccountId();
    }

    private AccountCreationRequest getAccountCreationRequest(Long customerId, String country, List<String> currencies) {
        return AccountCreationRequest.builder()
            .customerId(customerId)
            .country(country)
            .currencies(currencies)
            .build();
    }

    private Response createTransaction(TransactionRequest request) {
        return RestAssured.given()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/v1/transactions");
    }

    private TransactionRequest getTransactionRequest(
        Long accountId,
        BigDecimal amount,
        String currency,
        String direction,
        String description
    ) {
        return TransactionRequest.builder()
            .accountId(accountId)
            .amount(amount)
            .currency(currency)
            .direction(direction)
            .description(description)
            .build();
    }
}
