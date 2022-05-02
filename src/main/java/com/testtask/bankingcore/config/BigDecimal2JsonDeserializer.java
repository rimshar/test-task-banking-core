package com.testtask.bankingcore.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimal2JsonDeserializer extends NumberDeserializers.BigDecimalDeserializer {

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        BigDecimal value = super.deserialize(parser, context);

        value = value.setScale(2, RoundingMode.HALF_EVEN);

        return value;
    }
}
