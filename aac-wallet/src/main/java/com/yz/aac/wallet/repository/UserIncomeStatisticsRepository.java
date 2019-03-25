package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserIncomeStatisticsRepository {

    String SAVE_USER_INCOM_STATISTICS = "<script> "
            + "INSERT INTO user_income_statistics(user_id, currency_symbol, `key`, `value`) values"
            + "<foreach collection=\"keyList\" item=\"ky\" separator=\",\">"
            +"(#{userId}, #{currencySymbol}, #{ky}, #{value})"
            + "</foreach></script>";

    String  UPDATE_USER_INCOME_STATISTICES = "UPDATE user_income_statistics SET `value` = `value` + #{value} WHERE user_id = #{userId} AND currency_symbol = #{currencySymbol} AND `key` = #{key} ";
    
    @Insert(SAVE_USER_INCOM_STATISTICS)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserIncomeStatistics(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("keyList") List<String> keyList, @Param("value") BigDecimal value);

    @Select(UPDATE_USER_INCOME_STATISTICES)
    Integer updateUserIncomeStatistices(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("key") String key, @Param("value") BigDecimal value);
}
