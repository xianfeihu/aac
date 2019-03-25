package com.yz.aac.wallet.service;

/**
 * 商户资产统计
 */
public interface MerchantAssertStatisticsService {

    /**
     * 商户资产统计
     * @param coinType
     * @param key
     * @param addValue
     * @throws Exception
     */
    void addBehaviourStatistics(String coinType, String key, int addValue) throws Exception;
}
