package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MerchantAssertTradeRecord;
import com.yz.aac.opadmin.repository.domain.PlatformAssertTradeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface MerchantAssertTradeRecordRepository {

    String ALL_FIELDS = "id, initiator_id, initiator_name, trade_type, trade_time, currency_symbol, platform_price, trade_amount, balance, partner_id, partner_name, trade_result";
    String QUERY_RECORDS = "<script>SELECT " + ALL_FIELDS + " FROM merchant_assert_trade_record"
            + "<where>"
            + "<if test=\"tradeType == null\"> AND trade_type IN (1, 2)</if>"
            + "<if test=\"tradeType != null\"> AND trade_type = #{tradeType}</if>"
            + "<if test=\"beginTradeTime != null\"> AND trade_time &gt;= #{beginTradeTime}</if>"
            + "<if test=\"endTradeTime != null\"> AND trade_time &lt;= #{endTradeTime}</if>"
            + "<if test=\"currencySymbol != null\"> AND currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"initiatorId != null\"> AND initiator_id = #{initiatorId}</if>"
            + "<if test=\"initiatorName != null and initiatorName != ''\"><bind name=\"fixedName\" value=\"'%' + initiatorName + '%'\" /> AND initiator_name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY trade_time DESC"
            + "</script>";

    String QUERY_USER_ALL_RECORDS = "<script>"
            + " ("
            + " SELECT"
            + "   p.trade_time AS tradeTime,"
            + "   CASE"
            + "     WHEN p.trade_type IN (1, 2) THEN 1"
            + "     WHEN p.trade_type = 3 AND p.initiator_id = #{userId} THEN 3"
            + "     WHEN p.trade_type = 3 AND p.partner_id = #{userId} THEN 4"
            + "     WHEN p.trade_type = 4 AND p.initiator_id = #{userId} THEN 5"
            + "     WHEN p.trade_type = 4 AND p.partner_id = #{userId} THEN 6"
            + "   END AS tradeType,"
            + "   #{platformCurrentSymbol} AS currencySymbol,"
            + "   p.trade_amount AS tradeAmount,"
            + "   CASE"
            + "     WHEN p.trade_type IN (1, 2) THEN p.partner_name"
            + "     WHEN p.trade_type IN (3, 4) AND p.initiator_id = #{userId} THEN p.partner_name"
            + "     WHEN p.trade_type IN (3, 4) AND p.partner_id = #{userId} THEN p.initiator_name"
            + "   END AS partnerName"
            + " FROM platform_assert_trade_record p"
            + " WHERE (p.initiator_id = #{userId} OR p.partner_id = #{userId})"
            + " AND p.trade_type IN (1, 2, 3, 4)"
            + " AND (p.status IS NULL OR p.status = 2)"
            + " )"
            + " UNION"
            + " ("
            + " SELECT"
            + "   m.trade_time AS tradeTime,"
            + "   CASE"
            + "     WHEN m.trade_type = 1 AND m.initiator_id = #{userId} THEN 1"
            + "     WHEN m.trade_type = 1 AND m.partner_id = #{userId} THEN 2"
            + "     WHEN m.trade_type = 2 AND m.initiator_id = #{userId} THEN 2"
            + "     WHEN m.trade_type = 2 AND m.partner_id = #{userId} THEN 1"
            + "     WHEN m.trade_type = 3 AND m.initiator_id = #{userId} THEN 3"
            + "     WHEN m.trade_type = 3 AND m.partner_id = #{userId} THEN 4"
            + "   END AS tradeType,"
            + "   m.currency_symbol AS currencySymbol,"
            + "   m.trade_amount AS tradeAmount,"
            + "   CASE"
            + "     WHEN m.trade_type = 1 AND m.initiator_id = #{userId} THEN m.partner_name"
            + "     WHEN m.trade_type = 1 AND m.partner_id = #{userId} THEN m.initiator_name"
            + "     WHEN m.trade_type = 2 AND m.initiator_id = #{userId} THEN m.partner_name"
            + "     WHEN m.trade_type = 2 AND m.partner_id = #{userId} THEN m.initiator_name"
            + "     WHEN m.trade_type = 3 AND m.initiator_id = #{userId} THEN m.partner_name"
            + "     WHEN m.trade_type = 3 AND m.partner_id = #{userId} THEN m.initiator_name"
            + "   END AS partnerName"
            + " FROM merchant_assert_trade_record m"
            + " WHERE (m.initiator_id = #{userId} OR m.partner_id = #{userId})"
            + " )"
            + " ORDER BY tradeTime DESC"
            + "</script>";

    String QUERY_SELL_RECORDS = "SELECT"
            + " r.trade_time,"
            + " r.trade_amount,"
            + " CASE"
            + "   WHEN r.trade_type = 1 THEN r.initiator_id"
            + "   WHEN r.trade_type = 2 THEN r.partner_id"
            + " END AS partner_id,"
            + " CASE"
            + "   WHEN r.trade_type = 1 THEN r.initiator_name"
            + "   WHEN r.trade_type = 2 THEN r.partner_name"
            + " END AS partner_name"
            + " FROM merchant_assert_trade_record r"
            + " WHERE r.currency_symbol = #{currencySymbol}"
            + " AND ("
            + "   r.trade_type = 2 AND r.initiator_id = #{initiatorId}"
            + "   OR"
            + "   r.trade_type = 1 AND partner_id = #{initiatorId}"
            + " )"
            + " ORDER BY r.trade_time DESC";

    String COUNT_PLATFORM_AMOUNT_FOR_DASHBOARD = "<script>SELECT IFNULL(SUM(r.platform_price * r.trade_amount), 0) FROM merchant_assert_trade_record r"
            + " INNER JOIN user_role ur ON ur.user_id = r.initiator_id"
            + " INNER JOIN user_assert us ON us.user_id = r.initiator_id AND us.currency_symbol = #{platformCurrencySymbol}"
            + "<where>"
            + "<if test=\"tradeType != null\"> AND r.trade_type = #{tradeType}</if>"
            + "<if test=\"isMerchant != null\"> AND ur.is_merchant = #{isMerchant}</if>"
            + "<if test=\"isAdvertiser != null\"> AND ur.is_advertiser = #{isAdvertiser}</if>"
            + "<if test=\"initiatorId != null\"> AND r.initiator_id = #{initiatorId}</if>"
            + "<if test=\"beginTradeTime != null\"> AND r.trade_time &gt;= #{beginTradeTime}</if>"
            + "<if test=\"endTradeTime != null\"> AND r.trade_time &lt;= #{endTradeTime}</if>"
            + "<if test=\"initiatorName != null and initiatorName != ''\"><bind name=\"fixedName\" value=\"'%' + initiatorName + '%'\" /> AND r.initiator_name LIKE #{fixedName}</if>"
            + "</where>"
            + "</script>";

    String QUERY_PLATFORM_FOR_DASHBOARD = "<script>SELECT r.initiator_id, r.initiator_name, us.wallet_address, r.balance, r.trade_time, r.partner_name,"
            + " (r.platform_price * r.trade_amount) AS trade_amount,"
            + " CONCAT(r.currency_symbol, '-', r.trade_amount) AS trade_result"
            + " FROM merchant_assert_trade_record r"
            + " INNER JOIN user_role ur ON ur.user_id = r.initiator_id"
            + " INNER JOIN user_assert us ON us.user_id = r.initiator_id AND us.currency_symbol = #{platformCurrencySymbol}"
            + "<where>"
            + "<if test=\"tradeType != null\"> AND r.trade_type = #{tradeType}</if>"
            + "<if test=\"isMerchant != null\"> AND ur.is_merchant = #{isMerchant}</if>"
            + "<if test=\"isAdvertiser != null\"> AND ur.is_advertiser = #{isAdvertiser}</if>"
            + "<if test=\"initiatorId != null\"> AND r.initiator_id = #{initiatorId}</if>"
            + "<if test=\"beginTradeTime != null\"> AND r.trade_time &gt;= #{beginTradeTime}</if>"
            + "<if test=\"endTradeTime != null\"> AND r.trade_time &lt;= #{endTradeTime}</if>"
            + "<if test=\"initiatorName != null and initiatorName != ''\"><bind name=\"fixedName\" value=\"'%' + initiatorName + '%'\" /> AND r.initiator_name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY r.trade_time DESC"
            + "</script>";


    String COUNT_MERCHANT_AMOUNT_FOR_DASHBOARD = "<script>SELECT IFNULL(SUM(r.trade_amount), 0)"
            + " FROM merchant_assert_trade_record r"
            + " INNER JOIN user_assert us ON us.user_id = r.initiator_id AND us.currency_symbol = r.currency_symbol"
            + "<where>"
            + "<if test=\"tradeType != null\"> AND r.trade_type = #{tradeType}</if>"
            + "<if test=\"currencySymbol != null\"> AND r.currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"initiatorId != null\"> AND r.initiator_id = #{initiatorId}</if>"
            + "<if test=\"beginTradeTime != null\"> AND r.trade_time &gt;= #{beginTradeTime}</if>"
            + "<if test=\"endTradeTime != null\"> AND r.trade_time &lt;= #{endTradeTime}</if>"
            + "<if test=\"initiatorName != null and initiatorName != ''\"><bind name=\"fixedName\" value=\"'%' + initiatorName + '%'\" /> AND r.initiator_name LIKE #{fixedName}</if>"
            + "</where>"
            + "</script>";

    String QUERY_MERCHANT_FOR_DASHBOARD = "<script>SELECT r.initiator_id, r.initiator_name, us.wallet_address, r.merchant_valid_balance AS balance, r.trade_time, r.trade_amount, r.partner_name"
            + " FROM merchant_assert_trade_record r"
            + " INNER JOIN user_assert us ON us.user_id = r.initiator_id AND us.currency_symbol = r.currency_symbol"
            + "<where>"
            + "<if test=\"tradeType != null\"> AND r.trade_type = #{tradeType}</if>"
            + "<if test=\"currencySymbol != null\"> AND r.currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"initiatorId != null\"> AND r.initiator_id = #{initiatorId}</if>"
            + "<if test=\"beginTradeTime != null\"> AND r.trade_time &gt;= #{beginTradeTime}</if>"
            + "<if test=\"endTradeTime != null\"> AND r.trade_time &lt;= #{endTradeTime}</if>"
            + "<if test=\"initiatorName != null and initiatorName != ''\"><bind name=\"fixedName\" value=\"'%' + initiatorName + '%'\" /> AND r.initiator_name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY r.trade_time DESC"
            + "</script>";

    @Select(QUERY_SELL_RECORDS)
    List<MerchantAssertTradeRecord> querySellRecords(MerchantAssertTradeRecord condition);

    @Select(QUERY_RECORDS)
    List<MerchantAssertTradeRecord> query(MerchantAssertTradeRecord condition);

    @Select(QUERY_USER_ALL_RECORDS)
    List<MerchantAssertTradeRecord> queryAllRecordsByUserId(@Param("userId") Long userId, @Param("platformCurrentSymbol") String platformCurrentSymbol);

    @Select(COUNT_PLATFORM_AMOUNT_FOR_DASHBOARD)
    BigDecimal countPlatformAmountForDashboard(MerchantAssertTradeRecord condition);

    @Select(QUERY_PLATFORM_FOR_DASHBOARD)
    List<MerchantAssertTradeRecord> queryPlatformForDashboard(MerchantAssertTradeRecord condition);

    @Select(COUNT_MERCHANT_AMOUNT_FOR_DASHBOARD)
    BigDecimal countMerchantAmountForDashboard(MerchantAssertTradeRecord condition);

    @Select(QUERY_MERCHANT_FOR_DASHBOARD)
    List<MerchantAssertTradeRecord> queryMerchantForDashboard(MerchantAssertTradeRecord condition);
}
