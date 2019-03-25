package com.yz.aac.exchange.repository;


import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.exchange.model.request.CandlestickChartMsgRequest;
import com.yz.aac.exchange.model.response.CandlestickChartMsgResponse;
import com.yz.aac.exchange.repository.domian.MerchantAssertLatestData;

@Mapper
public interface MerchantAssertLatestDataRepository {

    String ADD = " INSERT INTO merchant_assert_latest_data (currency_symbol,max_price,min_price,recent_price,open_price,yesterday_close_price,platform_trad_num,applet_trad_num,currency_num,count_type,create_time) VALUES (#{currencySymbol},#{maxPrice},#{minPrice},#{recentPrice},#{openPrice},#{yesterdayClosePrice},#{platformTradNum},#{appletTradNum},#{currencyNum},#{countType},#{createTime}) ";
    String UPDATE = " UPDATE merchant_assert_latest_data SET recent_price=#{recentPrice},platform_trad_num=platform_trad_num+#{platformTradNum},applet_trad_num=applet_trad_num+#{appletTradNum},currency_num=currency_num+1 WHERE id=#{id} ";
    String UPDATE_MAX_PRICE = " UPDATE merchant_assert_latest_data SET max_price=#{recentPrice} WHERE id=#{id} ";
    String UPDATE_MIN_PRICE = " UPDATE merchant_assert_latest_data SET min_price=#{recentPrice} WHERE id=#{id} ";
    String QUERY_CANDLESTICK_CHART_FRO_TIME = " SELECT id,currency_symbol,max_price,min_price,recent_price,open_price,platform_trad_num,applet_trad_num,currency_num,count_type,create_time FROM merchant_assert_latest_data WHERE currency_symbol=#{currencySymbol} AND count_type=#{countType} AND create_time>=#{time} ORDER BY create_time ASC LIMIT 1 ";
    String SELECT_YESTERDAY_MERCHANT_DATA = " SELECT id,currency_symbol,max_price,min_price,recent_price,open_price,platform_trad_num,applet_trad_num,currency_num,count_type,create_time FROM merchant_assert_latest_data WHERE create_time>=#{yesterdayBeginTime} AND create_time<#{toDayBeginTime} AND count_type=#{countType} AND currency_symbol=#{currencySymbol} ORDER BY create_time DESC LIMIT 1 ";
    String SELECT_LATEST_PRICE = " SELECT recent_price FROM merchant_assert_latest_data WHERE currency_symbol=#{currencySymbol} AND count_type=#{countType} ORDER BY create_time DESC LIMIT 1 ";
    String SELECT_LATEST_CLOSING_PRICE = " SELECT recent_price FROM merchant_assert_latest_data c1 JOIN (SELECT max(create_time) AS create_time,currency_symbol,count_type FROM merchant_assert_latest_data WHERE create_time<#{startOfTodDay} AND count_type=#{countType} AND currency_symbol=#{currencySymbol}) c2 USING (create_time,currency_symbol,count_type) ORDER BY create_time ASC LIMIT 1 ";
    String SELECT_YESTERDAY_CLOSING_PRICE = "  SELECT recent_price FROM merchant_assert_latest_data WHERE create_time>=#{yesterdayBeginTime} AND create_time<#{toDayBeginTime} AND count_type=#{countType} AND currency_symbol=#{currencySymbol} ORDER BY create_time DESC LIMIT 1  ";
    String SELECT_CANDLESTICK_CHART_MSG = " <script>SELECT create_time,open_price,recent_price,min_price,max_price,platform_trad_num,applet_trad_num,currency_num FROM merchant_assert_latest_data WHERE currency_symbol=#{currencySymbol} AND count_type=#{type} ORDER BY create_time DESC <if test=\"num != null\"> LIMIT #{num} </if> </script>";
    String SELECT_YESTERDAY_TRAD_NUM = " SELECT platform_trad_num+applet_trad_num FROM merchant_assert_latest_data WHERE currency_symbol=#{currencySymbol} AND create_time<#{startOfTodDay} AND create_time>=#{millisPerDay} ORDER BY create_time DESC LIMIT 1 ";

    String QUERY_LATELY_CLOSING_PRICE = " select yesterday_close_price from merchant_assert_latest_data where from_unixtime(create_time/1000,'%Y-%m-%d') < date_format(now() ,'%Y-%m-%d') AND currency_symbol = #{currencySymbol} order by create_time desc limit 1 ";

    @Insert(ADD)
    int add(MerchantAssertLatestData candlestickChart);

    @Update(UPDATE)
    int update(@Param("id") Long id,
               @Param("recentPrice") BigDecimal recentPrice,
               @Param("platformTradNum") BigDecimal platformTradNum,
               @Param("appletTradNum") BigDecimal appletTradNum);

    @Update(UPDATE_MAX_PRICE)
    int updateMaxPrice(@Param("id") Long id, @Param("recentPrice") BigDecimal recentPrice);

    @Update(UPDATE_MIN_PRICE)
    int updateMinPrice(@Param("id") Long id, @Param("recentPrice") BigDecimal recentPrice);

    @Select(QUERY_CANDLESTICK_CHART_FRO_TIME)
    MerchantAssertLatestData getCandlestickChartForTime(@Param("currencySymbol") String currencySymbol, @Param("countType") Integer countType, @Param("time") long time);

    /**
     * 获取最近收盘价
     *
     * @param currencySymbol
     * @return
     */
    @Select(QUERY_LATELY_CLOSING_PRICE)
    BigDecimal getLatelyClosingPrice(@Param("currencySymbol") String currencySymbol);

    /**
     * 获取最后一笔交易单价
     *
     * @param currencySymbol
     * @param countType
     * @return
     */
    @Select(SELECT_LATEST_PRICE)
    BigDecimal getLatestPrices(@Param("currencySymbol") String currencySymbol, @Param("countType") Integer countType);

    /**
     * 获取最新的收盘价
     *
     * @param currencySymbol
     * @param startOfTodDay
     * @param countType
     * @return
     */
    @Select(SELECT_LATEST_CLOSING_PRICE)
    BigDecimal getLatestClosingPrices(
            @Param("currencySymbol") String currencySymbol,
            @Param("startOfTodDay") Long startOfTodDay,
            @Param("countType") Integer countType);

    @Select(SELECT_CANDLESTICK_CHART_MSG)
    List<CandlestickChartMsgResponse> candlestickChartMsg(CandlestickChartMsgRequest request);

    @Select(SELECT_YESTERDAY_TRAD_NUM)
    Long getYesterdayTradNum(@Param("currencySymbol") String currencySymbol, @Param("startOfTodDay") Long startOfTodDay, @Param("millisPerDay") Long millisPerDay);

    @Select(SELECT_YESTERDAY_CLOSING_PRICE)
    BigDecimal getYesterdayClosingPrice(@Param("currencySymbol") String currencySymbol, @Param("countType") Integer countType, @Param("yesterdayBeginTime") Long yesterdayBeginTime, @Param("toDayBeginTime") Long toDayBeginTime);

    @Select(SELECT_YESTERDAY_MERCHANT_DATA)
    MerchantAssertLatestData getYesterdayMerchantData(@Param("currencySymbol") String currencySymbol, @Param("countType") Integer countType, @Param("yesterdayBeginTime") Long yesterdayBeginTime, @Param("toDayBeginTime") Long toDayBeginTime);
}
