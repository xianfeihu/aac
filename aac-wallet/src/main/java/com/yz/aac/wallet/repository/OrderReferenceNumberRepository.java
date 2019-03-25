package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.yz.aac.wallet.repository.domain.OrderReferenceNumber;

@Mapper
public interface OrderReferenceNumberRepository {

    String SAVE_REFERENCE_NUMBER = "INSERT INTO order_reference_number(reference_number) VALUES(#{referenceNumber})";
    
    String DELETE_REFERENCE_NUMBER = "delete FROM order_reference_number WHERE TO_DAYS( FROM_UNIXTIME(create_time/1000,'%Y-%m-%d')) < (TO_DAYS(NOW()) - 7)";
    
    @Insert(SAVE_REFERENCE_NUMBER)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveReferenceNumber(OrderReferenceNumber orderReferenceNumber);
    
    /**
     * 删除一周前数据
     * @return
     */
    @Delete(DELETE_REFERENCE_NUMBER)
    int deleteReferenceNumber();
    
}
