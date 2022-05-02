package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountCreationRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRecord {

    private Long id;
    private Long customerId;
    private String countryCode;

    public static AccountRecord fromCreationRequest(AccountCreationRequest request) {
        return AccountRecord.builder()
            .customerId(request.customerId())
            .countryCode(request.country())
            .build();
    }
}
