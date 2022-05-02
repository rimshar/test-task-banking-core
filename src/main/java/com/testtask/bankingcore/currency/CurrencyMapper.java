package com.testtask.bankingcore.currency;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface CurrencyMapper {

    @Select("SELECT * FROM currency WHERE currency_code = #{currencyCode}")
    Optional<CurrencyRecord> findByCode(String currencyCode);

    @Select("SELECT currency_code FROM currency WHERE id = #{currencyId}")
    Optional<String> findCodeById(Long currencyId);
}
