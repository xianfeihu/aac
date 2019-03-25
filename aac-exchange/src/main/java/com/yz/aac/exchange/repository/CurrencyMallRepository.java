package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.model.response.CandlestickChartBaseMsgResponse;
import com.yz.aac.exchange.model.response.CurrencyMallIndexInfoForTaskResponse;
import com.yz.aac.exchange.model.response.CurrencyMallIndexInfoResponse;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CurrencyMallRepository {

    String DELETE_CURRENCY_MALL_INDEX_INFO = " TRUNCATE currency_mall_index_info ";
    String UPDATE_CURRENCY_MALL_INDEX_INFO = " INSERT INTO currency_mall_index_info SELECT a.currency_symbol,IFNULL(b.num,0),c.open_price,c.recent_price,#{time} FROM merchant_assert_issuance a JOIN merchant_assert_issuance_audit a1 ON a1.issuance_id=a.id LEFT JOIN (SELECT currency_symbol,COUNT(1) num FROM merchant_assert_trade_record WHERE trade_type<(#{time}-#{oneDayTimestamp}) GROUP BY currency_symbol) b USING (currency_symbol) LEFT JOIN (SELECT currency_symbol,open_price,recent_price FROM merchant_assert_latest_data WHERE count_type=#{countType} AND create_time>#{nowStartTimestamp}) c USING (currency_symbol) WHERE a1.status=#{status}";
    String QUERY_INDEX_INFO = " SELECT currency_symbol,trad_volume,yesterday_platform_price,last_platform_price FROM currency_mall_index_info WHERE 1=1 ";
    String SEARCH_INDEX_INFO = QUERY_INDEX_INFO + " and currency_symbol LIKE CONCAT('%',#{currencySymbol},'%') ";
    String SELECT_ALL_CURRENCY_NOW_MSG_FOR_SYMBOL = " SELECT currency_symbol,max_price,min_price,recent_price,open_price,platform_trad_num,applet_trad_num,currency_num,count_type,create_time,yesterday_close_price,b.total AS currency_sum FROM merchant_assert_latest_data JOIN (SELECT currency_symbol , total FROM merchant_assert_issuance) b USING (currency_symbol) WHERE create_time>=#{nowStartTimestamp} AND count_type=#{countType} AND currency_symbol=#{currencySymbol} LIMIT 1 ";
    String UPDATE_CURRENCY_MALL_INDEX_INFO_FOR_TASK = "INSERT INTO `merchant_assert_latest_data`(`currency_symbol`, `max_price`, `min_price`, `recent_price`, `open_price`, `yesterday_close_price`, `platform_trad_num`, `applet_trad_num`, `currency_num`, `count_type`, `create_time`) " +
            "SELECT tb1.currency_symbol,tb1.max_price,tb1.min_price,tb3.recent_price,tb2.open_price,tb3.recent_price,tb1.platform_trad_num,tb1.applet_trad_num,tb1.currency_num,#{countType},#{createTime} FROM ( " +
            "SELECT currency_symbol,MAX(max_price) AS max_price,MIN(min_price) AS min_price,SUM(platform_trad_num) AS platform_trad_num,SUM(applet_trad_num) AS applet_trad_num,SUM(currency_num) AS currency_num FROM merchant_assert_latest_data WHERE count_type=#{type} AND create_time>=#{beginTime} AND create_time<#{endTime} GROUP BY currency_symbol " +
            ") tb1 " +
            "LEFT JOIN ( " +
            "SELECT currency_symbol,open_price FROM merchant_assert_latest_data t1 JOIN (SELECT currency_symbol,MIN(create_time) AS create_time FROM merchant_assert_latest_data WHERE count_type=#{type} AND create_time>=#{beginTime} GROUP BY currency_symbol) t2 USING (currency_symbol,create_time) " +
            ") tb2 USING (currency_symbol) " +
            "LEFT JOIN ( " +
            "SELECT currency_symbol,recent_price FROM merchant_assert_latest_data t1 JOIN (SELECT currency_symbol,MAX(create_time) AS create_time FROM merchant_assert_latest_data WHERE count_type=#{type} AND create_time<#{endTime} GROUP BY currency_symbol) t2 USING (currency_symbol,create_time) " +
            ") tb3 USING (currency_symbol);";


    @Select(QUERY_INDEX_INFO)
    List<CurrencyMallIndexInfoResponse> getIndexInfo();

    @Select(SEARCH_INDEX_INFO)
    List<CurrencyMallIndexInfoResponse> searchIndexInfo(@Param("currencySymbol") String currencySymbol);

    @Insert(UPDATE_CURRENCY_MALL_INDEX_INFO)
    void updateCurrencyMallIndexInfo(
            @Param("oneDayTimestamp") Long oneDayTimestamp,
            @Param("nowStartTimestamp") Long nowStartTimestamp,
            @Param("countType") Integer countType,
            @Param("status") Integer status,
            @Param("time") Long time);

    @Delete(DELETE_CURRENCY_MALL_INDEX_INFO)
    void deleteCurrencyMallIndexInfo();

    @Select(SELECT_ALL_CURRENCY_NOW_MSG_FOR_SYMBOL)
    CandlestickChartBaseMsgResponse getAllCurrencyNowMsgForSymbol(@Param("nowStartTimestamp") Long nowStartTimestamp,@Param("currencySymbol") String currencySymbol, @Param("countType") Integer countType);

    @Insert(UPDATE_CURRENCY_MALL_INDEX_INFO_FOR_TASK)
    void updateCurrencyMallIndexInfoForTask(CurrencyMallIndexInfoForTaskResponse taskResponse);

}
