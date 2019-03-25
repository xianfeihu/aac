package com.yz.aac.mining.service;

import com.yz.aac.mining.model.request.MiningForSettlementAmountRequest;
import com.yz.aac.mining.model.response.ExcavationGameIndexInfoResponse;
import com.yz.aac.mining.model.response.JoinExcavationGameResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挖矿游戏服务
 */
public interface ExcavationGameService {

    /**
     * 获取挖矿游戏首页信息
     * @param userId
     * @return
     * @throws Exception
     */
    List<ExcavationGameIndexInfoResponse> getExcavationGameIndexInfo(Long userId) throws Exception;

    /**
     * 加入挖矿游戏
     * @param userId
     * @return
     * @throws Exception
     */
    JoinExcavationGameResponse joinExcavationGame(Long userId, Long activityId) throws Exception;

    /**
     * 当前挖矿游戏项参与人数
     * @param itemId
     * @return
     */
    Integer joinExcavationGameNumber(Long itemId) throws Exception;

    /**
     * 挖矿游戏-获取本次挖矿最多获取挖矿量（返回MAX挖矿量）
     * @param itemId
     * @param userId
     * @return
     */
    BigDecimal mostAmountOfMining(Long itemId, Long userId) throws Exception;

    /**
     * 挖矿游戏结算
     * @param request
     * @return
     */
    BigDecimal settlementAmountForMining(MiningForSettlementAmountRequest request) throws Exception;
}
