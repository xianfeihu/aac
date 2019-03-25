package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.repository.domian.MerchantAssertTodayTradeRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MerchantAssertTodayTradeRecordRepository {

    String INSERT_TRADE_RECORD_MESSAGE = " INSERT INTO `merchant_assert_today_trade_record`( `currency_symbol`, `trade_type`, `platform_price`, `trade_amount`, `trade_time`, `order_id`) VALUES ( #{currencySymbol}, #{tradeType}, #{platformPrice}, #{tradeAmount}, #{tradeTime}, #{orderId}) ";

    String CLEAR_YESTERDAY_TRADE_RECORD = "delete from merchant_assert_today_trade_record where trade_time < #{startOfTodDay}";

    String CANDLESTICK_CHART_MESSAGE_FOR_NOW = " select id,currency_symbol,trade_type,platform_price,trade_amount,trade_time,order_id from  merchant_assert_today_trade_record WHERE currency_symbol=#{currencySymbol} AND trade_time>=#{beginTime} ORDER BY trade_time DESC ";

    String SELECT_BY_TPYE_AND_SYMBOL = " select id,currency_symbol,trade_type,platform_price,trade_amount,trade_time,order_id from  merchant_assert_today_trade_record WHERE currency_symbol=#{currencySymbol} AND trade_type=#{type} ORDER BY trade_amount DESC LIMIT 5 ";

    @Insert(INSERT_TRADE_RECORD_MESSAGE)
    Integer addTradeRecordMessage(MerchantAssertTodayTradeRecord tradeRecord);

    @Delete(CLEAR_YESTERDAY_TRADE_RECORD)
    void clearYesterdayTradeRecord(@Param("startOfTodDay") Long startOfTodDay);

    @Select(CANDLESTICK_CHART_MESSAGE_FOR_NOW)
    List<MerchantAssertTodayTradeRecord> candlestickChartMsgForNow(@Param("currencySymbol") String currencySymbol, @Param("beginTime") Long beginTime);

    @Select(SELECT_BY_TPYE_AND_SYMBOL)
    List<MerchantAssertTodayTradeRecord> selectByTpyeAndSymbol(@Param("type") Integer type, @Param("currencySymbol") String currencySymbol);
}
