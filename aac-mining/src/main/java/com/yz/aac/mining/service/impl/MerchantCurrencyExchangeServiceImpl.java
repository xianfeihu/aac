package com.yz.aac.mining.service.impl;

import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.DateUtil;
import com.yz.aac.mining.model.request.MerchantCurrencyExchangeRequest;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.MerchantTradeRecord;
import com.yz.aac.mining.service.MerchantCurrencyExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION;
import static com.yz.aac.mining.Constants.MerchantAssertLatestDataCountTypeEnum.DAY;
import static com.yz.aac.mining.Constants.MerchantAssertStatisticsKey.MINING_MIND;
import static com.yz.aac.mining.Constants.MerchantAssertStatisticsKey.MINING_REST;
import static com.yz.aac.mining.Constants.MerchantTradeRecordTransactionModeEnum.PERSONAL_MINING;

@Service
@Slf4j
public class MerchantCurrencyExchangeServiceImpl implements MerchantCurrencyExchangeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;

    @Autowired
    private MerchantAssertLatestDataRepository merchantAssertLatestDataRepository;

    @Autowired
    private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;

    @Autowired
    private MerchantTradeRecordRepository merchantTradeRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean getMiningRewards(MerchantCurrencyExchangeRequest request) throws Exception {
        String currencySymbol = this.merchantAssertIssuanceRepository.queryCurrencyByMerchantId(request.getMerchantId());
        if (currencySymbol==null) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"未找到该商户");
        }
        int i = 0;
        // 扣除商户挖矿金额
        i += this.merchantAssertStatisticsRepository.addAssertByStatic(currencySymbol,MINING_MIND.name(),request.getAmount());
        i += this.merchantAssertStatisticsRepository.subtractAssertByStatic(currencySymbol,MINING_REST.name(),request.getAmount());
        if (i!=2) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"更新失败，请重试！！！");
        }
        // 添加用户资产数据
        if (request.getUserId() != null) {
            i += this.userAssertRepository.addUserAssertBalance(request.getUserId(), currencySymbol, request.getAmount(), request.getAmount());
            if (i!=3) {
                throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"更新失败，请重试！！！");
            }
            // 商户自己挖矿记录活跃存量增长明细
            if (request.getUserId().equals(this.userRepository.getUserIdByMerchantId(request.getMerchantId()))) {
                this.merchantTradeRecordRepository.addMerchantTradeRecord(new MerchantTradeRecord(
                        request.getMerchantId(),
                        request.getUserId(),
                        PERSONAL_MINING.code(),
                        PERSONAL_MINING.flowDirectionEnum().code(),
                        request.getAmount(),
                        this.merchantAssertLatestDataRepository.getLatestClosingPrices(currencySymbol, DateUtil.startOfTodDay(new Date()).getTime(), DAY.code()),
                        System.currentTimeMillis(),
                        PERSONAL_MINING.addSubtractEnum().code())
                );
            }
        }
        return true;
    }
}
