package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.yz.aac.exchange.repository.domian.OrderReferenceNumber;

@Mapper
public interface OrderReferenceNumberRepository {

    String SAVE_REFERENCE_NUMBER = "INSERT INTO order_reference_number(reference_number) VALUES(#{referenceNumber})";
    
    @Insert(SAVE_REFERENCE_NUMBER)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveReferenceNumber(OrderReferenceNumber orderReferenceNumber);
    
}
