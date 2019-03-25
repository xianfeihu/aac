package com.yz.aac.exchange.service;

import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.repository.domian.MerchantAssertTradeRecord;

import java.math.BigDecimal;

public interface MerchantAssertLatestDataService {

    /**
     * 更新当前商户币数据
     * @param tradeRecord
     * @throws Exception
     */
    void updateCurrencyMailMsg(MerchantAssertTradeRecord tradeRecord, Constants.MerchantOrderSource source) throws Exception;

    /**
     * 获取昨日收盘价
     * @param currencySymbol
     * @return
     * @throws Exception
     */
    BigDecimal getLatestClosingPrices(String currencySymbol) throws Exception;

}
