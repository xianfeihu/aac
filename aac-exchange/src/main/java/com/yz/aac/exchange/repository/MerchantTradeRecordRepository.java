package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.model.response.PlatStockResponse;
import com.yz.aac.exchange.repository.domian.MerchantTradeRecord;
import com.yz.aac.exchange.repository.domian.PlatformAssertTradeRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface MerchantTradeRecordRepository {

    String FIND_TRADE_RECORD_BY_MERCHANT_ID = " select * from merchant_trade_record where merchant_id=#{merchantId} AND add_subtract=#{addSubtract} ";

    String FIND_TRADE_RECORD_SUM_BY_MERCHANT_ID = " select SUM(trade_amount) from merchant_trade_record where merchant_id=#{merchantId} AND add_subtract=#{addSubtract} ";

    String ADD_MERCHANT_TRADE_RECORD = " INSERT INTO merchant_trade_record (merchant_id,user_id,transaction_mode,flow_direction,trade_amount,platform_price,add_subtract,trade_time) VALUES (#{merchantId},#{userId},#{transactionMode},#{flowDirection},#{tradeAmount},#{platformPrice},#{addSubtract},#{tradeTime}) ";

    String FIND_MERCHANT_PLAT_TRADE_RECORD = " <script> SELECT * FROM ( " +
            " SELECT 'plt' AS `source`,trade_time,platform_price,trade_amount,trade_type FROM merchant_assert_trade_record WHERE initiator_id=#{userId} OR partner_id=#{userId} AND trade_type IN <foreach collection='pltTypes' item='pltType' open='(' close=')' separator=','> #{pltType.code} </foreach> " +
            " UNION " +
            " SELECT 'mch' AS `source`,trade_time,0,trade_amount,trade_type FROM platform_assert_trade_record WHERE initiator_id=#{userId} AND trade_type IN <foreach collection='mchTypes' item='mchType' open='(' close=')' separator=','> #{mchType.code} </foreach> " +
            " UNION " +
            " SELECT 'div' AS `source`,dividend_date,0,profit_amount-dividend_amount,0 FROM merchant_dividend_record WHERE merchant_id=#{merchantId} AND `status`=#{dividendStatus} " +
            " ) AS merchantPlatTradeRecord ORDER BY trade_time ASC </script> ";

    @Select(FIND_TRADE_RECORD_BY_MERCHANT_ID)
    List<MerchantTradeRecord> findTradeRecordByMerchantId(@Param("merchantId") Long merchantId, @Param("addSubtract") Integer addSubtract);

    @Select(FIND_TRADE_RECORD_SUM_BY_MERCHANT_ID)
    BigDecimal findTradeRecordSumByMerchantId(@Param("merchantId") Long merchantId, @Param("addSubtract") Integer addSubtract);

    @Insert(ADD_MERCHANT_TRADE_RECORD)
    void addMerchantTradeRecord(MerchantTradeRecord tradeRecord);

    @Select(FIND_MERCHANT_PLAT_TRADE_RECORD)
    List<PlatStockResponse.Item> getMerchantPlatTradeRecord(@Param("merchantId") Long merchantId
            , @Param("userId") Long userId
            , @Param("pltTypes") Constants.PlatformAssertTradeType[] pltTypes
            , @Param("mchTypes") Constants.MerchantTradeType[] mchTypes
            , @Param("dividendStatus") Integer dividendStatus);
}
