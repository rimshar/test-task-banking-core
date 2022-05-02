package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO account(customer_id, country_code) " +
        "VALUES(#{customerId},#{countryCode})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void save(AccountRecord record);


    @Select("SELECT id, customer_id FROM account WHERE customer_id=#{id}")
    @Results(value = {
        @Result(property="accountId", column = "id"),
        @Result(property="customerId", column = "customer_id"),
        @Result(property="balances", column="account_id", javaType= List.class,
            many=@Many(select="com.testtask.bankingcore.balance.BalanceMapper.findAccountBalances"))
    })
    Optional<AccountResponse> findByCustomerId(Long id);

    @Select("SELECT id, customer_id FROM account WHERE id=#{id}")
    @Results(value = {
        @Result(property="accountId", column = "id"),
        @Result(property="customerId", column = "customer_id"),
        @Result(property="balances", column="id", javaType= List.class,
            many=@Many(select="com.testtask.bankingcore.balance.BalanceMapper.findAccountBalances"))
    })
    Optional<AccountResponse> findById(Long id);
}
