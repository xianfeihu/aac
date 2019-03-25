package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.PlatformAssertTradeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PlatformAssertTradeRecordRepository {

    String ALL_FIELDS = "id, initiator_id, initiator_name, trade_type, trade_time, rmb_price, available_trade_amount, trade_amount, min_amount_limit, max_amount_limit, wallet_address, balance, partner_id, partner_name, pay_number, status, selling_order_id";
    String QUERY_RECORDS = "<script>SELECT " + ALL_FIELDS + " FROM platform_assert_trade_record"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"payNumber != null\"> AND pay_number = #{payNumber}</if>"
            + "<if test=\"tradeTypes != null\"> AND trade_type IN <foreach collection='tradeTypes' item='tradeType' open='(' close=')' separator=','> #{tradeType} </foreach></if>"
            + "<if test=\"beginTradeTime != null\"> AND trade_time &gt;= #{beginTradeTime}</if>"
            + "<if test=\"endTradeTime != null\"> AND trade_time &lt;= #{endTradeTime}</if>"
            + "<if test=\"initiatorName != null and initiatorName != ''\"><bind name=\"fixedName\" value=\"'%' + initiatorName + '%'\" /> AND initiator_name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY status ASC, trade_time ASC"
            + "</script>";

    String COUNT_AMOUNT_FOR_DASHBOARD = "<script>SELECT IFNULL(SUM(trade_amount), 0) FROM platform_assert_trade_record r"
            + " INNER JOIN user_role ur ON ur.user_id = r.initiator_id"
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

    String QUERY_FOR_DASHBOARD = "<script>SELECT r.initiator_id, r.initiator_name, r.wallet_address, r.valid_balance AS balance, r.trade_time, r.trade_amount, r.partner_name FROM platform_assert_trade_record r"
            + " INNER JOIN user_role ur ON ur.user_id = r.initiator_id"
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

    String UPDATE_RECORD = "UPDATE platform_assert_trade_record SET status = #{status} WHERE id = #{id}";

    String UPDATE_EXPIRED_RECORDS = "UPDATE platform_assert_trade_record SET status = 3 WHERE trade_type = 2 AND status = 1 AND trade_time < #{tradeTime}";

    @Select(QUERY_RECORDS)
    List<PlatformAssertTradeRecord> query(PlatformAssertTradeRecord condition);

    @Select(QUERY_FOR_DASHBOARD)
    List<PlatformAssertTradeRecord> queryForDashboard(PlatformAssertTradeRecord condition);

    @Select(COUNT_AMOUNT_FOR_DASHBOARD)
    BigDecimal countAmountForDashboard(PlatformAssertTradeRecord condition);

    @Update(UPDATE_RECORD)
    void update(PlatformAssertTradeRecord record);

    @Update(UPDATE_EXPIRED_RECORDS)
    void updateExpiredRecords(PlatformAssertTradeRecord record);
}
