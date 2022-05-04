package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import com.testtask.bankingcore.account.exception.AccountNotFoundException;
import com.testtask.bankingcore.common.extensions.RabbitMessageExtension;
import com.testtask.bankingcore.common.listeners.rabbit.RabbitTestHarness;
import com.testtask.bankingcore.common.meta.Annotations.IntegrationTest;
import com.testtask.bankingcore.config.amqp.account.AccountAmqpProperties;
import com.testtask.bankingcore.config.amqp.balance.BalanceAmqpProperties;
import com.testtask.bankingcore.config.amqp.customer.CustomerAmqpProperties;
import com.testtask.bankingcore.customer.CustomerService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.testtask.bankingcore.common.assertions.JsonAssert.assertThatJson;
import static com.testtask.bankingcore.common.matchers.JsonMatcher.isEqualTo;
import static com.testtask.bankingcore.common.matchers.JsonMatcher.isStrictlyEqualTo;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isBadRequest;
import static com.testtask.bankingcore.common.matchers.StatusMatcher.isOk;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@RequiredArgsConstructor
@ExtensionMethod(RabbitMessageExtension.class)
class AccountTest {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final RabbitTestHarness rabbitHarness;
    private final AccountAmqpProperties accountProperties;
    private final CustomerAmqpProperties customerProperties;
    private final BalanceAmqpProperties balanceProperties;

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

        postAccount(createAccountCreationRequest(customerId, "EE", List.of("EUR", "USD")))
            .then()
            .assertThat()
            .statusCode(isOk())
            .contentType(ContentType.JSON)
            .body("accountId", notNullValue())
            .body(isEqualTo(expected));
    }

    @Test
    void happy_path_account_retrieved() {
        val customerId = customerService.createCustomer("Test customer");

        val accountId = postAccount(createAccountCreationRequest(customerId, "EE", List.of("EUR", "USD")))
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
            .body(isStrictlyEqualTo(expected));
    }

    @Nested
    class RabbitMq {

        @Test
        void happy_path_messages_received() throws InterruptedException {
            val accountQueue = rabbitHarness.listen(
                accountProperties.creation().amqp().exchange(),
                accountProperties.creation().amqp().routingKey()
            );

            val customerQueue = rabbitHarness.listen(
                customerProperties.creation().amqp().exchange(),
                customerProperties.creation().amqp().routingKey()
            );

            val balanceQueue = rabbitHarness.listen(
                balanceProperties.creation().amqp().exchange(),
                balanceProperties.creation().amqp().routingKey()
            );

            val customerId = customerService.createCustomer("Test customer");

            val accountId = postAccount(createAccountCreationRequest(customerId, "EE", List.of("EUR")))
                .then()
                .extract()
                .jsonPath()
                .getLong("accountId");


            String expectedAccountCreationMessage = """
            {
                accountId: %s,
                customerId: %s,
                countryCode: EE
            }
            """.formatted(accountId, customerId);

            String expectedCustomerCreationMessage = """
            {
                customerId: %s,
                name: 'Test customer'
            }
            """.formatted(customerId);

            String expectedBalanceCreationMessage = """
            {
                accountId: %s,
                amount: 0.00,
                currency: EUR
            }
            """.formatted(accountId);

            val accountCreationMessage = accountQueue.await().json();
            val customerCreationMessage = customerQueue.await().json();
            val balanceCreationMessage = balanceQueue.await().json();

            assertThatJson(accountCreationMessage).isStrictlyEqualTo(expectedAccountCreationMessage);
            assertThatJson(customerCreationMessage).isStrictlyEqualTo(expectedCustomerCreationMessage);
            assertThatJson(balanceCreationMessage).isEqualTo(expectedBalanceCreationMessage);
        }

    }

    @Nested
    class Validation {

        @Test
        void exception_thrown_on_invalid_currency() {
            val customerId = customerService.createCustomer("Test customer");

            postAccount(createAccountCreationRequest(customerId, "EE", List.of("EUR", "JPY")))
                .then()
                .assertThat()
                .statusCode(isBadRequest())
                .contentType(ContentType.JSON)
                .body("message", equalTo("Invalid currency: JPY"));

            assertThrows(AccountNotFoundException.class, () -> accountService.findByCustomerId(customerId));
        }

        @Test
        void invalid_request() {
            String expected = """
                {
                    message: Bad Request,
                    details: [
                        CustomerId must not be empty,
                        Country must not be empty
                    ]
                }
                """;

            postAccount(createAccountCreationRequest(null, null, List.of("EUR", "JPY")))
                .then()
                .assertThat()
                .statusCode(isBadRequest())
                .contentType(ContentType.JSON)
                .body(isEqualTo(expected));
        }
    }

    private Response postAccount(AccountCreationRequest request) {
        return RestAssured.given()
            .body(request)
            .contentType(ContentType.JSON)
            .post("/api/v1/accounts");
    }

    private Response getAccount(Long accountId) {
        return RestAssured.get("/api/v1/accounts/%s".formatted(accountId));
    }

    private AccountCreationRequest createAccountCreationRequest(Long customerId, String countryCode, List<String> currencies) {
        return AccountCreationRequest.builder()
            .customerId(customerId)
            .country(countryCode)
            .currencies(currencies)
            .build();
    }
}
