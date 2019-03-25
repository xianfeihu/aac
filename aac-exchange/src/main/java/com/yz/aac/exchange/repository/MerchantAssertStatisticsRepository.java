package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.repository.domian.MerchantAssertStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface MerchantAssertStatisticsRepository {

	String QUERY_ASSERT_STATIC = " SELECT * FROM merchant_assert_statistics WHERE currency_symbol = #{currencySymbol} AND `key` = #{key} ";

	String UPDATE_ASSERT_STATIC = " UPDATE merchant_assert_statistics SET `value` = `value` + #{value} WHERE currency_symbol = #{currencySymbol} AND `key` = #{key} ";

	String SUBTRACT_MERCHANT_ASSERT_STATIC = " UPDATE merchant_assert_statistics SET `value` = `value` - #{value} WHERE currency_symbol = #{currencySymbol} AND `key` = #{key} AND `value` >= #{value}";

	String QUERY_MARKET_LIQUIDITY = " SELECT IFNULL(SUM(`value`),0) FROM merchant_assert_statistics WHERE currency_symbol=#{currencySymbol} AND (`key`=#{miningMind} OR `key`=#{sellSold}) ";

	String QUERY_SELL_REST = "select `value` -("
			+ "select CASE WHEN sum(amount) is null then 0 ELSE sum(amount) END AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol} "
			+ ") as AvailableFunds from merchant_assert_statistics where currency_symbol = #{currencySymbol} and `key` = #{key}";

	@Select(QUERY_ASSERT_STATIC)
	MerchantAssertStatistics queryAssertStatic(
			@Param("currencySymbol") String currencySymbol,
			@Param("key") String key);

	@Update(UPDATE_ASSERT_STATIC)
	int updateAssertStatic(@Param("currencySymbol") String currencySymbol,
			@Param("key") String key, @Param("value") BigDecimal value);

	/**
	 * 获取实际可出售额度（排除挂单冻结额度）
	 * @param currencySymbol
	 * @param userId
	 * @param key
	 * @return
	 */
	@Select(QUERY_SELL_REST)
	BigDecimal queryAssertStaticSellRest(@Param("currencySymbol") String currencySymbol, @Param("userId") Long userId, @Param("key") String key);

	@Select(QUERY_MARKET_LIQUIDITY)
    BigDecimal queryMarketLiquidity(@Param("currencySymbol") String currencySymbol, @Param("miningMind") String miningMind, @Param("sellSold") String sellSold);

	@Update(SUBTRACT_MERCHANT_ASSERT_STATIC)
	int subtractMerchantAssertStatic(@Param("currencySymbol") String currencySymbol, @Param("key") String key, @Param("value") BigDecimal value);
}
