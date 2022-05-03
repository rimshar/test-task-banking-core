package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.account.AccountService;
import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.common.meta.Annotations.IntegrationTest;
import com.testtask.bankingcore.customer.CustomerService;
import com.testtask.bankingcore.transaction.api.v1.TransactionCreationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.testtask.bankingcore.common.matchers.JsonMatcher.isJsonEqualTo;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isBadRequest;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isOk;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.notFound;
import static org.hamcrest.Matchers.notNullValue;

@IntegrationTest
@RequiredArgsConstructor
public class TransactionTest {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Test
    void happy_path_transactions_saved() {
        val accountId = createAccount(createTestCustomer());

        String expectedBefore = """
            {
                accountId: %s,
                amount: 100.50,
                currency: EUR,
                direction: in,
                description: Test transaction,
                updatedBalance: 100.50
            }
            """.formatted(accountId);

        postTransaction(
            accountId,
            createTransactionRequest(
                BigDecimal.valueOf(100.5000), "EUR", "IN", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body("transactionId", notNullValue())
            .body(isJsonEqualTo(expectedBefore));

        String expectedAfter = """
            {
                accountId: %s,
                amount: 40.25,
                currency: EUR,
                direction: out,
                description: Test transaction,
                updatedBalance: 60.25
            }
            """.formatted(accountId);

        postTransaction(
            accountId,
            createTransactionRequest(
                BigDecimal.valueOf(40.2500), "EUR", "OUT", "Test transaction"
            ))
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body("transactionId", notNullValue())
            .body(isJsonEqualTo(expectedAfter));
    }

    @Test
    void happy_path_get_transactions() {
        val accountId = createAccount(createTestCustomer());
        val transactionIds = new ArrayList<>();

        IntStream.rangeClosed(1, 3)
            .forEach(i -> {
                    val id = postTransaction(
                        accountId,
                        createTransactionRequest(
                            BigDecimal.valueOf(100.5000), "EUR", "IN", "Test transaction %s".formatted(i)
                        ))
                        .then()
                        .extract()
                        .jsonPath()
                        .getLong("transactionId");

                    transactionIds.add(id);
                }
            );

        String expected = """
            [
                {
                    accountId: %1$s,
                    transactionId: %2$s,
                    amount: 100.50,
                    currency: EUR,
                    direction: in,
                    description: Test transaction 1
                },
                {
                    accountId: %1$s,
                    transactionId: %3$s,
                    amount: 100.50,
                    currency: EUR,
                    direction: in,
                    description: Test transaction 2
                },
                {
                    accountId: %1$s,
                    transactionId: %4$s,
                    amount: 100.50,
                    currency: EUR,
                    direction: in,
                    description: Test transaction 3
                }
            ]
            """.formatted(accountId, transactionIds.get(0), transactionIds.get(1), transactionIds.get(2));

        getTransactions(accountId)
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body(isJsonEqualTo(expected));
    }

    @Nested
    class Validation {

        @Test
        void invalid_currency() {
            val accountId = createAccount(createTestCustomer());

            String expected = """
                {
                    message: Invalid currency: InvalidCurrency,
                    path: /api/v1/accounts/%s/transactions,
                    error: Bad Request,
                    status: 400
                }
                """.formatted(accountId);

            postTransaction(
                accountId,
                createTransactionRequest(
                    BigDecimal.valueOf(100.5000), "InvalidCurrency", "IN", "Test transaction"
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
            val accountId = createAccount(createTestCustomer());

            String expected = """
                {
                    message: Account %1$s does not support currency GBP,
                    path: /api/v1/accounts/%1$s/transactions,
                    error: Not Found,
                    status: 404
                }
                """.formatted(accountId);

            postTransaction(
                accountId,
                createTransactionRequest(
                    BigDecimal.valueOf(100.5000), "GBP", "IN", "Test transaction"
                ))
                .then()
                .assertThat()
                .statusCode(notFound())
                .contentType(ContentType.JSON)
                .body("timestamp", notNullValue())
                .body(isJsonEqualTo(expected));
        }

        @Test
        void invalid_fields() {
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

            postTransaction(
                123456L,
                createTransactionRequest(
                    BigDecimal.valueOf(-100.50), "EUR", "InvalidDirection", "iv"
                ))
                .then()
                .assertThat()
                .statusCode(isBadRequest())
                .contentType(ContentType.JSON)
                .body(isJsonEqualTo(expected));
        }

        @Test
        void insufficient_funds() {
            val accountId = createAccount(createTestCustomer());

            String expected = """
                {
                    message: Insufficient funds,
                    path: /api/v1/accounts/%s/transactions,
                    error: Bad Request,
                    status: 400
                }
                """.formatted(accountId);

            postTransaction(
                accountId,
                createTransactionRequest(
                    BigDecimal.valueOf(100.5000), "EUR", "OUT", "Test transaction"
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
                    path: /api/v1/accounts/123456/transactions,
                    error: Not Found,
                    status: 404
                }
                """;

            postTransaction(
                123456L,
                createTransactionRequest(
                    BigDecimal.valueOf(100.5000), "EUR", "OUT", "Test transaction"
                ))
                .then()
                .assertThat()
                .statusCode(notFound())
                .contentType(ContentType.JSON)
                .body("timestamp", notNullValue())
                .body(isJsonEqualTo(expected));
        }
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

    private Response postTransaction(Long accountId, TransactionCreationRequest request) {
        return RestAssured.given()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/v1/accounts/%s/transactions".formatted(accountId));
    }

    private Response getTransactions(Long accountId) {
        return RestAssured.get("/api/v1/accounts/%s/transactions".formatted(accountId));
    }

    private TransactionCreationRequest createTransactionRequest(
        BigDecimal amount,
        String currency,
        String direction,
        String description
    ) {
        return TransactionCreationRequest.builder()
            .amount(amount)
            .currency(currency)
            .direction(direction)
            .description(description)
            .build();
    }
}
