package com.yz.aac.wallet.service;

/**
 * 用户行为统计
 */
public interface UserBehaviourStatisticsService {

    /**
     * 用户行为统计
     * @param id 累加用户ID
     * @param key
     * @param addValue
     */
    void addBehaviourStatistics(Long id, String key, int addValue) throws Exception;
}
