package com.testtask.bankingcore.account.api.v1;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
public record AccountCreationRequest(
    @NotNull(message = "CustomerId must not be empty")
    Long customerId,
    @NotNull(message = "Country must not be empty")
    @Size(min=3, max = 3, message = "Country code must be in Alpha-3 ISO 3166 format")
    String country,
    List<String> currencies
) { }
