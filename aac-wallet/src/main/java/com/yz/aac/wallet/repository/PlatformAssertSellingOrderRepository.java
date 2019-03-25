package com.yz.aac.wallet.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.PlatformAssertSellingOrder;

@Mapper
public interface PlatformAssertSellingOrderRepository {

    String QUERY_ASSERT_SELLER_ORDER_RANDOM = "SELECT * FROM platform_assert_selling_order where available_trade_amount >= #{availableTradeAmount} LIMIT 1";
    
    String QUERY_ASSERT_SELLER_ORDER = "SELECT * FROM platform_assert_selling_order where id = #{id}";
    
    @Select(QUERY_ASSERT_SELLER_ORDER_RANDOM)
    PlatformAssertSellingOrder getAssertSellerOrderRandom(@Param("availableTradeAmount") BigDecimal availableTradeAmount);
    
    @Select(QUERY_ASSERT_SELLER_ORDER)
    PlatformAssertSellingOrder getAssertSellerOrder(@Param("id") Long id);
}
