package com.testtask.bankingcore.customer;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
interface CustomerMapper {

    @Insert("INSERT INTO customer(name) VALUES(#{name})")
    @Options(useGeneratedKeys=true, keyProperty = "id", keyColumn = "id")
    void save(CustomerRecord customer);
}
