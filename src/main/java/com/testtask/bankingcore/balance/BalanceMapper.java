package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.common.Money;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balance(account_id, amount, currency_id) " +
        "VALUES(#{accountId},#{amount},#{currencyId})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int save(BalanceRecord record);

    @Select("SELECT amount FROM balance WHERE account_id = #{accountId}")
    @Results(value={
        @Result(property="amount", column ="amount"),
        @Result(property="currency", column="currency_id", javaType= String.class, one=@One(select="findCodeById"))
    })
    List<Money> findAccountBalances(String accountId);
}
