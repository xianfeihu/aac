package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface MerchantAssertStatisticsRepository {

	String ADD_ASSERT_STATIC = " UPDATE merchant_assert_statistics SET `value` = `value` + #{value} WHERE currency_symbol = #{currencySymbol} AND `key` = #{key} ";

	String SUBTRACT_ASSERT_STATIC = " UPDATE merchant_assert_statistics SET `value` = `value` - #{value} WHERE currency_symbol = #{currencySymbol} AND `key` = #{key} ";

	@Update(ADD_ASSERT_STATIC)
	int addAssertByStatic(@Param("currencySymbol") String currencySymbol, @Param("key") String key, @Param("value") BigDecimal value);

	@Update(SUBTRACT_ASSERT_STATIC)
	int subtractAssertByStatic(@Param("currencySymbol") String currencySymbol, @Param("key") String key, @Param("value") BigDecimal value);

}
