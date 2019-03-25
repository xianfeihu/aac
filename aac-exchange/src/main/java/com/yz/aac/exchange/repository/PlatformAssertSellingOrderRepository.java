package com.yz.aac.exchange.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.model.response.CurrencyTradingListResponse;
import com.yz.aac.exchange.repository.domian.PlatformAssertSellingOrder;

@Mapper
public interface PlatformAssertSellingOrderRepository {

    String PLATFORM_ASSERT_SELLER_ORDER_LIST = "select pas.name as personName, paso.available_trade_amount as number, paso.rmb_price as price, paso.id as orderId "
    		+ " from platform_assert_selling_order paso"
    		+ " left join platform_assert_seller pas on pas.id = paso.seller_id"
    		+ " order by paso.rmb_price";
    
    String QUERY_SELLER_ORDER_INFO_BY_ID = "select * from platform_assert_selling_order WHERE id = #{id}";
    
    /**
     * 分页获取系统AAB挂单信息
     */
    @Select(PLATFORM_ASSERT_SELLER_ORDER_LIST)
    List<CurrencyTradingListResponse> getPlatformAssertSellerOrderList();
    
    @Select(QUERY_SELLER_ORDER_INFO_BY_ID)
    PlatformAssertSellingOrder getSellerOrderInfoById(@Param("id") Long id);
    
}
