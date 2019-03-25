package com.yz.aac.exchange.service;

import com.yz.aac.exchange.model.request.MerchantCurrencyExchangeRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.repository.domian.MerchantDividendRecord;
import com.yz.aac.exchange.repository.domian.MerchantTradeRecord;

import java.math.BigDecimal;
import java.util.List;

public interface MerchantService {

    /**
     * 商户货币统计信息
     * @param merchantId
     * @param assetType
     * @param countType
     * @return
     */
    MerchantCurrencyStatisticsResponse getMerchantCurrencyStatistics(Long merchantId, Integer assetType, Integer countType) throws Exception ;

    /**
     * 获取账户存量明细
     * @param merchantId
     * @param pageNo
     * @param pageSize
     * @param addOrSubtract
     * @param flag
     * @return
     */
    MerchantActiveStockResponse getMerchantActiveStockByAddOrSubtract(Long merchantId, Integer pageNo, Integer pageSize, Integer addOrSubtract, Boolean flag) throws Exception;

    /**
     * 获取账户冻结存量
     * @param merchantId
     * @return
     */
    List<MerchantFreezeStockResponse> getMerchantOrderFreezeStock(Long merchantId) throws Exception;

    /**
     * 获取冻结卖单详情
     * @param merchantId
     * @param orderId
     * @return
     */
    List<MerchantFreezeStockDetailsResponse> getMerchantOrderFreezeStockDetails(Long merchantId, Long orderId) throws Exception;

    /**
     * 平台币交易明细
     * @param merchantId
     * @param pageNo
     * @param pageSize
     * @param flag
     * @return
     * @throws Exception
     */
    PlatStockResponse getPlatTradeRecord(Long merchantId, Integer pageNo, Integer pageSize, Boolean flag) throws Exception;

    /**
     * 添加商户交易记录
     * @param tradeRecord
     * @return
     * @throws Exception
     */
    void addMerchantTradeRecord(MerchantTradeRecord tradeRecord) throws Exception;

    /**
     * 商家 --> 用户转币
     * @param request
     * @return
     */
    Boolean merchantTransferUser(MerchantCurrencyExchangeRequest request, Long serviceId) throws Exception;

    /**
     * 商户和平台币汇率
     * @param currencySymbol
     * @return
     */
    MerchantAndPlatExchangeRateResponse exchangeRate(String currencySymbol) throws Exception;

    /**
     * 添加分红周期信息
     * @param merchantId
     * @param incomePeriod
     * @return
     */
    void addMerchantDividendRecord(Long merchantId, Integer incomePeriod);

    /**
     * 设置盈利额
     * @param merchantId
     * @param amount
     * @param serviceId
     */
    Boolean setProfitAmount(Long merchantId, BigDecimal amount, Long serviceId) throws Exception;

    /**
     *  定时任务--更新分红信息表
     */
    void updateMerchantDividendRecord();
}
