package com.testtask.bankingcore.transaction;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
interface TransactionMapper {

    @Insert("INSERT INTO transaction(account_id, amount, currency_id, direction, description) " +
        "VALUES(#{accountId},#{amount},#{currencyId},#{direction},#{description})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void save(TransactionRecord record);
}
