package com.testtask.bankingcore.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.testtask.bankingcore.config.BigDecimal2JsonDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Money {
    @JsonDeserialize(using = BigDecimal2JsonDeserializer.class)
    private BigDecimal amount;
    private String currency;
}
