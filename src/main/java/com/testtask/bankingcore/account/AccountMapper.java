package com.testtask.bankingcore.account;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account")
    List<AccountRecord> selectAll();

    @Insert("INSERT INTO account(customer_id, country_code) VALUES customer_id=#{customerId},country_code=#{countryCode})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    AccountRecord save(AccountRecord record);
}
