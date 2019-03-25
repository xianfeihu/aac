package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface MerchantAssertStatisticsRepository {

    String UPDATE_VALUE_BY_KEY = " UPDATE merchant_assert_statistics mas SET mas.value = mas.value+#{addValue} WHERE mas.currency_symbol = #{currencySymbol} AND mas.key=#{key} ";

    String QUERY_MARKET_LIQUIDITY = " SELECT IFNULL(SUM(`value`),0) FROM merchant_assert_statistics WHERE currency_symbol=#{currencySymbol} AND `key`=#{miningMind} OR `key`=#{sellSold} ";

    String QUERY_ASSERT_STATISTICS_BY_KEY = " SELECT IFNULL(`value`,0) FROM merchant_assert_statistics WHERE currency_symbol=#{currencySymbol} AND `key`=#{key} ";

    String QUERY_SELL_REST = "select `value` -("
            + "select CASE WHEN sum(amount) is null then 0 ELSE sum(amount) END AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol} "
            + ") as AvailableFunds from merchant_assert_statistics where currency_symbol = #{currencySymbol} and `key` = #{key}";

    @Update(UPDATE_VALUE_BY_KEY)
    Integer addBehaviourStatistics(@Param("currencySymbol") String currencySymbol, @Param("key") String key, @Param("addValue") int addValue);

    @Select(QUERY_MARKET_LIQUIDITY)
    BigDecimal queryMarketLiquidity(@Param("currencySymbol") String currencySymbol, @Param("miningMind") String miningMind, @Param("sellSold") String sellSold);

    @Select(QUERY_ASSERT_STATISTICS_BY_KEY)
    BigDecimal queryAssertStatisticsByKey(@Param("currencySymbol") String currencySymbol, @Param("key") String key);

    /**
     * 获取实际可出售额度（排除挂单冻结额度）
     * @param currencySymbol
     * @param userId
     * @param key
     * @return
     */
    @Select(QUERY_SELL_REST)
    BigDecimal queryAssertStaticSellRest(@Param("currencySymbol") String currencySymbol, @Param("userId") Long userId, @Param("key") String key);

}
