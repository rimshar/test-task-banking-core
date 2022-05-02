package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.common.Money;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balance(account_id, amount, currency_id) " +
        "VALUES(#{accountId},#{amount},#{currencyId})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void save(BalanceRecord record);

    @Select("SELECT amount, currency_id FROM balance WHERE account_id=#{accountId}")
    @Results(value={
        @Result(property="amount", column ="amount", javaType = BigDecimal.class),
        @Result(property="currency", column="currency_id", javaType= String.class,
            one=@One(select="com.testtask.bankingcore.currency.CurrencyMapper.findCodeById"))
    })
    List<Money> findAccountBalances(Long accountId);
}
