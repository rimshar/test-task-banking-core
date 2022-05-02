package com.testtask.bankingcore.customer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerRecord {
    private Long id;
    private String name;
}
