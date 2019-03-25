package com.yz.aac.exchange.service;

import com.yz.aac.exchange.model.request.CandlestickChartMsgRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.repository.domian.MerchantAssertTodayTradeRecord;

import java.util.List;

public interface CurrencyMallService {

    /**
     * 获取数字币商城首页相关信息
     * @return
     */
    List<CurrencyMallIndexInfoResponse> getIndexInfo() throws Exception;

    /**
     * 搜索币的相关信息
     * @param currencySymbol
     * @return
     */
    List<CurrencyMallIndexInfoResponse> searchIndexInfo(String currencySymbol) throws Exception;

    /**
     * 交易所买卖单信息
     * @param currencySymbol
     * @return
     */
    List<MerchantAssertTodayTradeRecord> exchangeTradInfo(String currencySymbol);

    /**
     * 获取 K线图(实时数据)
     * @return
     * @throws Exception
     */
    List<MerchantAssertTodayTradeRecord> candlestickChartMsgForNow(String currencySymbol) throws Exception ;

    /**
     * 获取 K线图(日、周、月)
     * @param request
     * @return
     * @throws Exception
     */
    List<CandlestickChartMsgResponse> candlestickChartMsg(CandlestickChartMsgRequest request) throws Exception ;

    /**
     * 获取日K线图基础信息
     * @param currencySymbol
     * @return
     */
    CandlestickChartBaseMsgResponse candlestickChartBaseMsg(String currencySymbol) throws Exception;

    /**
     * 币简介
     * @param currencySymbol
     * @return
     */
    CurrencyIntroductionResponse currencyIntroduction(String currencySymbol) throws Exception ;

    /**
     * 购买该商户币
     * @param currencySymbol
     * @return
     * @throws Exception
     */
    CurrencyTradingInfoResponse buyCurrencyBySymbol(String currencySymbol, Long userId) throws Exception ;

    /**
     * 出售该商户币
     * @param currencySymbol
     * @return
     * @throws Exception
     */
    CurrencyTradingInfoResponse sellCurrencyBySymbol(String currencySymbol, Long userId) throws Exception ;

}
