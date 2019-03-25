package com.yz.aac.mining.repository;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface MerchantAssertLatestDataRepository {

	String SELECT_LATEST_CLOSING_PRICE = " SELECT recent_price FROM merchant_assert_latest_data c1 JOIN (SELECT max(create_time) AS create_time,currency_symbol,count_type FROM merchant_assert_latest_data WHERE create_time<#{startOfTodDay} AND count_type=#{countType} AND currency_symbol=#{currencySymbol}) c2 USING (create_time,currency_symbol,count_type) ORDER BY create_time ASC LIMIT 1 ";

    @Select(SELECT_LATEST_CLOSING_PRICE)
    BigDecimal getLatestClosingPrices(
            @Param("currencySymbol") String currencySymbol,
            @Param("startOfTodDay") Long startOfTodDay,
            @Param("countType") Integer countType);

}
