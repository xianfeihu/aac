package com.yz.aac.mining.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserIncomeStatisticsRepository {

    String  UPDATE_USER_INCOME_STATISTICES = "UPDATE user_income_statistics SET `value` = `value` + #{value} WHERE user_id = #{userId} AND currency_symbol = #{currencySymbol} AND `key` = #{key} ";
    
    @Select(UPDATE_USER_INCOME_STATISTICES)
    void updateUserIncomeStatistices(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("key") String key, @Param("value") BigDecimal value);
}
