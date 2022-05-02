package com.testtask.bankingcore.account.api.v1;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record AccountCreationRequest(
    @NotNull(message = "CustomerId must not be empty")
    Long customerId,
    @NotNull(message = "Country must not be empty")
    String country,
    List<String> currencies
) {}
