package com.testtask.bankingcore.account.api.v1;

import lombok.Builder;

import java.util.List;

@Builder
public record AccountCreationRequest(
    Long customerId,
    String country,
    List<String> currencies
) {}
