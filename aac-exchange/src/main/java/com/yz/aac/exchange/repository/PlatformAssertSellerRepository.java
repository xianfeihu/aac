package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.repository.domian.PlatformAssertSeller;

@Mapper
public interface PlatformAssertSellerRepository {
    
    String QUERY_SELLER_BY_ID = "select * from platform_assert_seller WHERE id = #{id}";
    
    @Select(QUERY_SELLER_BY_ID)
    PlatformAssertSeller getSellerById(@Param("id") Long id);
}
