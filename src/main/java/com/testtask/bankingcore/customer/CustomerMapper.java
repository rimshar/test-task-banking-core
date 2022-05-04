package com.testtask.bankingcore.customer;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
interface CustomerMapper {

    @Insert("INSERT INTO customer(name) VALUES(#{name})")
    @Options(useGeneratedKeys=true, keyProperty = "id", keyColumn = "id")
    void save(CustomerRecord customer);

    @Select("SELECT * FROM customer WHERE id=#{id}")
    Optional<CustomerRecord> findById(Long id);
}
