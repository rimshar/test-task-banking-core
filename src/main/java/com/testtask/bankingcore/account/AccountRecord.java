package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import lombok.Builder;

@Builder
public record AccountRecord(
    String id,
    String customerId,
    String countryCode
) {
    public static AccountRecord fromCreationRequest(AccountCreationRequest request) {
        return AccountRecord.builder()
            .customerId(request.customerId())
            .countryCode(request.country())
            .build();
    }
}
