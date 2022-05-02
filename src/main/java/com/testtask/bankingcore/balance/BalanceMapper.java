package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.common.Money;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    @Select("SELECT * FROM balance " +
        "WHERE account_id=#{accountId}" +
        "AND currency_id=#{currencyId}")
    @Results(value={
        @Result(property="id", column ="id"),
        @Result(property="accountId", column ="account_id"),
        @Result(property="amount", column ="amount"),
        @Result(property="currencyId", column ="currency_id"),
    })
    Optional<BalanceRecord> findCurrencyBalance(Long accountId, Long currencyId);

    @Update("UPDATE balance SET amount=#{amount} WHERE id =#{id}")
    void updateBalance(BalanceRecord record);
}
