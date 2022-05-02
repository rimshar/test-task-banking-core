package com.testtask.bankingcore.account;

import com.testtask.bankingcore.account.api.v1.AccountResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account")
    List<AccountRecord> selectAll();

    @Insert("INSERT INTO account(customer_id, country_code) " +
        "VALUES(#{customerId},#{countryCode})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int save(AccountRecord record);


    @Select("SELECT account_id, customer_id FROM account WHERE customer_id= #{id}")
    @Results(value = {
        @Result(property="accountId", column = "account_id"),
        @Result(property="customerId", column = "customerId"),
        @Result(property="balances", column="account_id", javaType= List.class, many=@Many(select="findAccountBalances"))
    })
    Optional<AccountResponse> findByCustomerId(Long id);
}
