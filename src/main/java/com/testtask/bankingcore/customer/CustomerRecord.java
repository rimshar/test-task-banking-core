package com.testtask.bankingcore.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRecord {
    private Long id;
    private String name;
}
