package com.testtask.bankingcore.currency;

import com.testtask.bankingcore.currency.exception.InvalidCurrencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyMapper mapper;

    public Long findIdByCode(String currency) {
        return mapper.findByCode(currency)
            .orElseThrow(() -> new InvalidCurrencyException(currency))
            .getId();
    }

    public String findCodeById(Long currencyId) {
        return mapper.findCodeById(currencyId)
            .orElse("Unknown currency");
    }
}
