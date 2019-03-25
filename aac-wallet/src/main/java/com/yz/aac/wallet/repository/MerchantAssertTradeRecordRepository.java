package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.model.response.TradeRecordResponse;
import com.yz.aac.wallet.model.response.UserTransferRecordResponse;
import com.yz.aac.wallet.repository.domain.MerchantAssertTradeRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface MerchantAssertTradeRecordRepository {

    String QUERY_TRANSACTION_RECORD = " select #{userId}, initiator_id,initiator_name,partner_id,partner_name,trade_type*1,trade_time,trade_amount,currency_symbol from merchant_assert_trade_record where ( initiator_id=#{userId} or partner_id=#{userId}) AND currency_symbol=#{currencySymbol} and trade_type=#{tradeType} ";
    String QUERY_TRADE_RECORD = " select #{userId},initiator_id,partner_id,trade_type*1,trade_amount,currency_symbol,platform_price,trade_time  from merchant_assert_trade_record where ( initiator_id=#{userId} or partner_id=#{userId}) and trade_type in (${tradeType}) AND currency_symbol=#{currencySymbol} ";
    String QUERY_ALL_TRADE_RECORD = "<script>"
            + "select #{userId},initiator_id,partner_id,trade_type*1,trade_amount,currency_symbol,platform_price,trade_time "
            + "from merchant_assert_trade_record "
            + "where (initiator_id=#{userId} or partner_id=#{userId}) "

            + "<if test=\"merchantTradeTypes != null and merchantTradeTypes.length > 0\"> "
            + "AND trade_type in "
            + "<foreach collection='merchantTradeTypes' item='mtt' open='(' close=')' separator=','>"
            + "#{mtt}"
            + "</foreach>"
            + "</if> "

            + "UNION ALL "
            + "SELECT  #{userId},initiator_id,partner_id,trade_type*1,trade_amount,null,rmb_price,trade_time "
            + "FROM platform_assert_trade_record "
            + "where (initiator_id=#{userId} or partner_id=#{userId}) "

            + "<if test=\"platformTradeTypes != null and platformTradeTypes.length > 0\"> "
            + "AND trade_type in "
            + "<foreach collection='platformTradeTypes' item='ptt' open='(' close=')' separator=','>"
            + "#{ptt}"
            + "</foreach>"
            + "</if> "

            + "ORDER BY trade_time DESC"
            + "</script>";
    String QUERY_MCH_LAST_PLAT_FORM_PRICE = "SELECT platform_price FROM merchant_assert_trade_record WHERE currency_symbol=#{coinType} ORDER BY trade_time DESC LIMIT 1";
    String INSERT_TRANSFER_MESSAGE = " INSERT INTO merchant_assert_trade_record (initiator_id,initiator_name,trade_type,trade_time,currency_symbol,platform_price,trade_amount,balance,valid_balance,merchant_balance,merchant_valid_balance,partner_id,partner_name,trade_result) VALUES (#{initiatorId},#{initiatorName},#{tradeType},#{tradeTime},#{currencySymbol},#{platformPrice},#{tradeAmount},#{balance},#{validBalance},#{merchantBalance},#{merchantValidBalance},#{partnerId},#{partnerName},#{tradeResult}) ";
    String QUERY_TRADE_COUNT = " SELECT COUNT(0) FROM merchant_assert_trade_record WHERE trade_type=#{tradeType} AND (initiator_id=#{userId} OR partner_id=#{userId}) ";
    String QUERY_RECEIPT_COUNT = " SELECT COUNT(0) FROM merchant_assert_trade_record WHERE (initiator_id=#{userId} AND trade_type=#{sell}) OR (partner_id=#{userId} AND trade_type=#{buy}) OR (partner_id=#{userId} AND trade_type=#{transfer}) ";

    @Select(QUERY_TRANSACTION_RECORD)
    List<UserTransferRecordResponse> getTransactionRecordHistory(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("tradeType") Integer tradeType);

    @Select(QUERY_TRADE_RECORD)
    List<TradeRecordResponse> getTradeRecordHistory(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("tradeType") String tradeType);

    @Select(QUERY_ALL_TRADE_RECORD)
    List<TradeRecordResponse> getAllTradeRecordHistory(@Param("userId") Long userId, @Param("merchantTradeTypes") Integer[] merchantTradeTypes, @Param("platformTradeTypes") Integer[] platformTradeTypes);


    /**
     * 获取商户最后一笔交易记录的汇率
     * @return
     */
    @Select(QUERY_MCH_LAST_PLAT_FORM_PRICE)
    BigDecimal getMchLastPlatformPrice(@Param("coinType") String coinType);

    @Insert(INSERT_TRANSFER_MESSAGE)
    @Options(useGeneratedKeys = true)
    int addTradeRecord(MerchantAssertTradeRecord transferMsg);

    @Select(QUERY_TRADE_COUNT)
    Integer getTradeCount(@Param("userId") Long userId,@Param("tradeType") Integer tradeType);

    @Select(QUERY_RECEIPT_COUNT)
    Integer getReceiptCount(@Param("userId") Long userId,@Param("buy") Integer buy,@Param("sell") Integer sell,@Param("transfer") Integer transfer);
}
