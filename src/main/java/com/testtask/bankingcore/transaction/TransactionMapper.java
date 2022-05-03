package com.testtask.bankingcore.transaction;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
interface TransactionMapper {

    @Insert("INSERT INTO transaction(account_id, amount, currency_id, direction, description) " +
        "VALUES(#{accountId},#{amount},#{currencyId},#{direction},#{description})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    void save(TransactionRecord record);

    @Select("SELECT * FROM transaction WHERE account_id=#{accountId}")
    List<TransactionRecord> findByAccountId(Long accountId);
}
