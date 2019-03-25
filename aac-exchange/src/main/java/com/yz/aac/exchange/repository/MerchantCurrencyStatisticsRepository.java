package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.Constants;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface MerchantCurrencyStatisticsRepository {

    String FIND_MERCHANT_CURRENCY_LAST_STATISTICS_BY_TYPE = " SELECT currency_num FROM merchant_currency_statistics WHERE merchant_id=#{merchantId} AND asset_type=#{assetType} AND create_time=#{time} LIMIT 1 ";

    String DELETE_LAST_MONTH_MERCHANT_STATISTICS = " delete from merchant_currency_statistics where create_time<#{lastMonthBeginTime} ";

    String ADD_MERCHANT_ACTIVE_STOCK_FOR_DAY = " INSERT INTO merchant_currency_statistics (merchant_id,asset_type,currency_num,create_time) SELECT m.id,#{assetType},IFNULL((mas.value-uaf.amount),mas.value),#{createTime} FROM `user` u JOIN merchant m USING (mobile_number) JOIN merchant_assert_issuance mai ON mai.merchant_id=m.id JOIN merchant_assert_issuance_audit maia ON maia.issuance_id=mai.id JOIN (select `value`,`currency_symbol` from merchant_assert_statistics where `key` = #{assertStatisticsKey}) mas USING (currency_symbol) LEFT JOIN (SELECT currency_symbol,amount,user_id FROM user_assert_freeze WHERE reason=#{freezeReason}) uaf ON uaf.currency_symbol=mai.currency_symbol AND uaf.user_id=u.id WHERE maia.status=#{merchantStatus} ";

    String ADD_MERCHANT_ORDER_FREEZE_STOCK_FOR_DAY = " INSERT INTO merchant_currency_statistics (merchant_id,asset_type,currency_num,create_time) SELECT result.id,#{assetType},result.sum,#{createTime} FROM (SELECT m.id,IFNULL(SUM(available_trade_amount),0) AS sum,mai.currency_symbol FROM `user` u JOIN merchant m USING (mobile_number) JOIN merchant_assert_issuance mai ON mai.merchant_id=m.id JOIN merchant_assert_issuance_audit maia ON maia.issuance_id=mai.id LEFT JOIN user_order AS uo ON uo.currency_symbol=mai.currency_symbol AND u.id=uo.user_id WHERE maia.status=#{merchantStatus} GROUP BY currency_symbol) AS result ";

    String FIND_LIQUIDITY = " <script> SELECT SUM(`value`) FROM merchant_assert_statistics WHERE merchant_id=#{merchantId}" +
            " AND `key` in <foreach collection='liquidityKey' item='liquidity' open='(' close=')' separator=','> #{liquidity} </foreach> </script>";

    @Select(FIND_MERCHANT_CURRENCY_LAST_STATISTICS_BY_TYPE)
    BigDecimal findMerchantCurrencyLastStatisticsByType(@Param("merchantId") Long merchantId,@Param("assetType") Integer assetType,@Param("time") Long time);

    @Insert(ADD_MERCHANT_ACTIVE_STOCK_FOR_DAY)
    void addMerchantActiveStockForDay(@Param("assetType") Integer assetType,@Param("createTime") Long createTime,@Param("assertStatisticsKey") String assertStatisticsKey,@Param("freezeReason") Integer freezeReason,@Param("merchantStatus") Integer merchantStatus);

    @Insert(ADD_MERCHANT_ORDER_FREEZE_STOCK_FOR_DAY)
    void addMerchantOrderFreezeStockForDay(@Param("assetType") Integer assetType,@Param("createTime") Long createTime,@Param("merchantStatus") Integer merchantStatus);

    @Delete(DELETE_LAST_MONTH_MERCHANT_STATISTICS)
    void deletelastMonthMerchantStatistics(@Param("lastMonthBeginTime") Long lastMonthBeginTime);

    @Select(FIND_LIQUIDITY)
    BigDecimal getLiquidity(@Param("merchantId") Long merchantId, @Param("liquidityKey") Constants.MerchantAssertStatisticsKey[] liquidityKey);
}
