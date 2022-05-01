package com.testtask.bankingcore.balance;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balance(account_id, amount, currency_id) " +
        "VALUES account_id=#{accountId}," +
        "amount=#{amount})," +
        "currency_id=#{currencyId}")
    @Options(useGeneratedKeys=true, keyProperty="id")
    BalanceRecord save(BalanceRecord record);
}
