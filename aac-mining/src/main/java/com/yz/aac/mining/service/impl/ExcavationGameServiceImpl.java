package com.yz.aac.mining.service.impl;

import com.yz.aac.common.Constants;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.DateUtil;
import com.yz.aac.mining.aspect.AccountAspect;
import com.yz.aac.mining.model.request.MiningForSettlementAmountRequest;
import com.yz.aac.mining.model.response.ExcavationGameIndexInfoResponse;
import com.yz.aac.mining.model.response.JoinExcavationGameResponse;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import com.yz.aac.mining.service.ExcavationGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.mining.Constants.*;

@Slf4j
@Service
public class ExcavationGameServiceImpl implements ExcavationGameService {

    @Autowired
    private AccountAspect accountAspect;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private UserMiningRecordRepository userMiningRecordRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private MiningActivityRepository miningActivityRepository;

    @Autowired
    private MiningActivityStatisticsRepository miningActivityStatisticsRepository;

    @Autowired
    private MiningActivityParticipantRepository miningActivityParticipantRepository;

    @Override
    public List<ExcavationGameIndexInfoResponse> getExcavationGameIndexInfo(Long userId) throws Exception {
        List<ExcavationGameIndexInfoResponse> responses = this.miningActivityRepository.getExcavationGameIndexInfo(System.currentTimeMillis(), MiningActivityStatus.NORMAL.code());
        if (responses==null || responses.isEmpty()) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "暂无挖矿游戏开放！请稍后再来！");
        }
        String userLevelName = this.userLevelRepository.getUserLevelNameByUserId(userId , PLATFORM_CURRENCY_SYMBOL.value()).getName();
        responses.stream().forEach(x -> x.setUserLevelName(userLevelName));
        return responses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JoinExcavationGameResponse joinExcavationGame(Long userId, Long activityId) throws Exception {

        Long nowTime = System.currentTimeMillis();

        // 获取挖矿数据
        MiningActivity activity = this.miningActivityRepository.findMiningActivityById(activityId, MiningActivityStatus.NORMAL.code());
        if (activity==null || activity.getBeginTime()-nowTime < DateUtil.MILLIS_PER_MINUTE) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "下次早点来哦，游戏已经开始了");
        }

        // 获取该用户等级
        UserLevel userLevel = this.userLevelRepository.getUserLevelNameByUserId(userId ,PLATFORM_CURRENCY_SYMBOL.value());

        // 根据用户等级获取挖矿活动项
        MiningActivityItem item = this.miningActivityRepository.findItemByLevelAndActivityId(userLevel.getId(), activity.getId());
        if (item==null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您的等级较低不可参加此活动");
        }

        // 用户是否已结算该场次游戏
        MiningActivityStatistics statistics = this.miningActivityStatisticsRepository.findStatisticsByUserIdAndItemId(userId, item.getId());
        if (statistics!=null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您已参与过此场游戏！不可二次参与");
        }

        // 入场成功，生成相关数据
        MiningActivityParticipant participant = this.miningActivityParticipantRepository.findParticipantByItemIdAndUserId(item.getId(), userId);
        if (participant==null) {
            this.miningActivityParticipantRepository.add(item.getId(), userId);
        }

        Random random= new Random();
        JoinExcavationGameResponse response = new JoinExcavationGameResponse();
        response.setItemId(item.getId());
        response.setLevelId(userLevel.getId());
        response.setActivityBeginTime(activity.getBeginTime() - nowTime);
        response.setActivityProcessingTime(activity.getEndTime() - activity.getBeginTime());
        response.setAjaxBeginTime(activity.getBeginTime() - nowTime - DateUtil.MILLIS_PER_MINUTE + random.nextInt(Long.valueOf(DateUtil.MILLIS_PER_MINUTE).intValue()));
        response.setLuckyRate(item.getLuckyRate());
        response.setLuckyTimes(item.getLuckyTimes());
        response.setHitAdNumber(item.getHitAdNumber());

        return response;
    }

    @Override
    public Integer joinExcavationGameNumber(Long itemId) throws Exception {
        return this.miningActivityParticipantRepository.findJoinExcavationGameNumber(itemId);
    }

    @Override
    public BigDecimal mostAmountOfMining(Long itemId, Long userId) throws Exception {

        Long nowTime = System.currentTimeMillis();

        // 获取指定的有效活动
        MiningActivityItem item = this.miningActivityRepository.findValidMiningActivityItemByItemId(itemId, nowTime, MiningActivityStatus.NORMAL.code());
        if (item == null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "该活动暂未开始或以结束");
        }

        MiningActivityParticipant participant = this.miningActivityParticipantRepository.findParticipantByItemIdAndUserId(itemId, userId);
        if (participant == null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您未提前一分钟入场，本次活动参与失败。");
        }

        // 获取参与人数
        Integer joinNumber = joinExcavationGameNumber(itemId);

        return getMostAmountOfMining(item.getTotalBonus(), participant.getQueuingCode(), joinNumber);
    }

    @Override
    public BigDecimal settlementAmountForMining(MiningForSettlementAmountRequest request) throws Exception {

        Long nowTime = System.currentTimeMillis();

        // 用户是否已结算该场次游戏
        MiningActivityStatistics statistics = this.miningActivityStatisticsRepository.findStatisticsByUserIdAndItemId(request.getUserId(), request.getItemId());
        if (statistics!=null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您已结算成功");
        }
        MiningActivityItem item = this.miningActivityRepository.findItemByItemId(request.getItemId());
        if (item == null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "未找到该活动，结算失败");
        }

        MiningActivity activity = this.miningActivityRepository.findActivityByItemId(request.getItemId());
        if (activity == null || activity.getEndTime() < nowTime-DateUtil.MILLIS_PER_MINUTE * 5) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "活动已结束，结算或超时");
        }

        MiningActivityParticipant participant = this.miningActivityParticipantRepository.findParticipantByItemIdAndUserId(request.getItemId(), request.getUserId());
        if (participant == null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "抱歉，您未参与此次活动");
        }

        BigDecimal settlementAmount = BigDecimal.ZERO;

        BigDecimal mostAmountOfMining =  getMostAmountOfMining(item.getTotalBonus(), participant.getQueuingCode(), joinExcavationGameNumber(request.getItemId()));
        if (mostAmountOfMining.compareTo(BigDecimal.ZERO) > 0) {
            if (mostAmountOfMining.compareTo(request.getOrdinaryAmount()) < 0
                    || request.getOrdinaryAmount().multiply(BigDecimal.valueOf(item.getLuckyTimes())).compareTo(request.getExtraAmount()) < 0) {
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "非法操作！！！");
            }
            settlementAmount = request.getOrdinaryAmount().add(request.getExtraAmount()).setScale(2, BigDecimal.ROUND_FLOOR);
        }

        // 用户挖矿统计
        this.miningActivityStatisticsRepository.save(new MiningActivityStatistics(null,item.getId(),request.getUserId(),settlementAmount, request.getHitAdNumber()));

        // 用户挖矿金额存入用户资金账户
        this.userAssertRepository.addUserAssertBalance(request.getUserId(), Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value(), settlementAmount, settlementAmount);

        // 用户行为统计
        this.accountAspect.addAccountBehaviour(request.getUserId(), UserBehaviourStatisticsKey.MINING_EVENT.name());
        this.accountAspect.addAccountBehaviour(request.getUserId(), UserBehaviourStatisticsKey.CLICK_AD.name());

        // 记录挖矿游戏记录
        this.userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(request.getUserId(), null, MiningActionEnum.MINING.code(), nowTime, settlementAmount, MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));

        return settlementAmount;
    }

    /**
     * 计算当前参与者能分得的金额
     * @param totalBonus
     * @param queuingCode
     * @param joinNumber
     * @return
     */
    private BigDecimal getMostAmountOfMining(BigDecimal totalBonus, Integer queuingCode, Integer joinNumber) {
        BigDecimal capitaValue = totalBonus.divide(BigDecimal.valueOf(joinNumber), 2, BigDecimal.ROUND_FLOOR);
        return capitaValue.compareTo(BigDecimal.valueOf(0.01)) > -1 ? capitaValue : (totalBonus.divide(BigDecimal.valueOf(0.01)).compareTo(BigDecimal.valueOf(queuingCode)) > -1 ? BigDecimal.valueOf(0.01) : BigDecimal.ZERO);
    }

}
