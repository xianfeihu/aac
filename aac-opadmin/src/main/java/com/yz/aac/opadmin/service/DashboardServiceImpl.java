package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.model.request.QueryDashboardRecordRequest;
import com.yz.aac.opadmin.model.response.PlatformAssetOverviewResponse;
import com.yz.aac.opadmin.model.response.QueryDashboardRecordResponse;
import com.yz.aac.opadmin.repository.MerchantAssertTradeRecordRepository;
import com.yz.aac.opadmin.repository.PlatformAssertTradeRecordRepository;
import com.yz.aac.opadmin.repository.PlatformAssetStatisticsRepository;
import com.yz.aac.opadmin.repository.domain.CurrencyKeyValuePair;
import com.yz.aac.opadmin.repository.domain.MerchantAssertTradeRecord;
import com.yz.aac.opadmin.repository.domain.PlatformAssertTradeRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.DashboardTradeType.BUY;
import static com.yz.aac.opadmin.Constants.DashboardTradeType.CONSUME;
import static com.yz.aac.opadmin.Constants.DashboardTradeType.SELL;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.INVALID_USER_CODE;
import static com.yz.aac.opadmin.Constants.PlatformAssertStatistics.*;
import static com.yz.aac.opadmin.Constants.UserRole.*;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PlatformAssetStatisticsRepository platformAssetStatisticsRepository;

    @Autowired
    private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;

    @Autowired
    private MerchantAssertTradeRecordRepository merchantAssertTradeRecordRepository;

    @Override
    public PlatformAssetOverviewResponse queryPlatformAssetOverview() throws Exception {
        List<CurrencyKeyValuePair> resultList = platformAssetStatisticsRepository.query();
        Map<String, BigDecimal> resultMap = new HashMap<>(resultList.size());
        for (CurrencyKeyValuePair item : resultList) {
            resultMap.put(item.getKey(), item.getValue());
        }
        return new PlatformAssetOverviewResponse(
                PLATFORM_CURRENCY_SYMBOL.value(),
                resultMap.get(UPDATE_TIME.value()).longValue(),
                resultMap.get(TOTAL.value()),
                resultMap.get(ACTIVE.value()),
                resultMap.get(ACTIVE_INCREASE.value()),
                resultMap.get(ACTIVE_INCREASE_MINED.value()),
                resultMap.get(ACTIVE_INCREASE_REST.value()),
                resultMap.get(ACTIVE_MINING.value()),
                resultMap.get(ACTIVE_MINING_MINED.value()),
                resultMap.get(ACTIVE_MINING_REST.value()),
                resultMap.get(FIXED.value()),
                resultMap.get(FIXED_SOLD.value()),
                resultMap.get(FIXED_REST.value())
        );
    }

    @Override
    public QueryDashboardRecordResponse queryPlatformAssetRecords(QueryDashboardRecordRequest request) throws Exception {
        QueryDashboardRecordResponse result = null;
        BigDecimal totalTradeAmount = null;
        Long userId;
        Integer isMerchant = null, isAdvertiser = null;
        if (GENERAL.value() == request.getUserRole()) {
            isMerchant = NO.value();
            isAdvertiser = NO.value();
        } else if (MERCHANT.value() == request.getUserRole()) {
            isMerchant = YES.value();
        } else if (ADVERTISER.value() == request.getUserRole()) {
            isAdvertiser = YES.value();
        }
        try {
            userId = StringUtils.isBlank(request.getUserCode()) ? null : Long.parseLong(request.getUserCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        if (BUY.value() == request.getTradeType()) {
            PlatformAssertTradeRecord param = new PlatformAssertTradeRecord();
            param.setIsMerchant(isMerchant);
            param.setIsAdvertiser(isAdvertiser);
            param.setTradeType(Constants.PlatformTradeType.BUY.value());
            param.setBeginTradeTime(request.getBeginTime());
            param.setEndTradeTime(request.getEndTime());
            param.setInitiatorId(userId);
            param.setInitiatorName(StringUtils.isBlank(request.getUserName()) ? null : request.getUserName().trim());
            //统计
            if (request.getOutputStatistics()) {
                totalTradeAmount = platformAssertTradeRecordRepository.countAmountForDashboard(param);
            }
            //查询
            PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
            Page<PlatformAssertTradeRecord> page = (Page<PlatformAssertTradeRecord>) platformAssertTradeRecordRepository.queryForDashboard(param);
            List<QueryDashboardRecordResponse.Item> items = page.getResult().stream()
                    .map(x -> new QueryDashboardRecordResponse.Item(
                            USER_CODE_PREFIX.value() + x.getInitiatorId(),
                            x.getInitiatorName(),
                            x.getWalletAddress(),
                            x.getBalance(),
                            x.getTradeTime(),
                            x.getTradeAmount(),
                            x.getPartnerName(),
                            null
                    )).collect(Collectors.toList());
            result = new QueryDashboardRecordResponse(page.getTotal(), PLATFORM_CURRENCY_SYMBOL.value(), totalTradeAmount, items);
        } else if (CONSUME.value() == request.getTradeType()) {
            MerchantAssertTradeRecord param = new MerchantAssertTradeRecord();
            param.setIsMerchant(isMerchant);
            param.setIsAdvertiser(isAdvertiser);
            param.setTradeType(Constants.MerchantTradeType.BUY.value());
            param.setBeginTradeTime(request.getBeginTime());
            param.setEndTradeTime(request.getEndTime());
            param.setInitiatorId(userId);
            param.setInitiatorName(StringUtils.isBlank(request.getUserName()) ? null : request.getUserName().trim());
            param.setPlatformCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
            //统计
            if (request.getOutputStatistics()) {
                totalTradeAmount = merchantAssertTradeRecordRepository.countPlatformAmountForDashboard(param);
            }
            //查询
            PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
            Page<MerchantAssertTradeRecord> page = (Page<MerchantAssertTradeRecord>) merchantAssertTradeRecordRepository.queryPlatformForDashboard(param);
            List<QueryDashboardRecordResponse.Item> items = page.getResult().stream()
                    .map(x -> new QueryDashboardRecordResponse.Item(
                            USER_CODE_PREFIX.value() + x.getInitiatorId(),
                            x.getInitiatorName(),
                            x.getWalletAddress(),
                            x.getBalance(),
                            x.getTradeTime(),
                            x.getTradeAmount(),
                            null,
                            x.getTradeResult()
                    )).collect(Collectors.toList());
            result = new QueryDashboardRecordResponse(page.getTotal(), PLATFORM_CURRENCY_SYMBOL.value(), totalTradeAmount, items);
        }
        return result;
    }

    @Override
    public QueryDashboardRecordResponse queryMerchantAssetRecords(QueryDashboardRecordRequest request) throws Exception {
        BigDecimal totalTradeAmount = null;
        Long userId;
        try {
            userId = StringUtils.isBlank(request.getUserCode()) ? null : Long.parseLong(request.getUserCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        MerchantAssertTradeRecord param = new MerchantAssertTradeRecord();
        param.setCurrencySymbol(request.getCurrency().trim());
        param.setTradeType(SELL.value() == request.getTradeType() ? Constants.MerchantTradeType.SELL.value() : request.getTradeType());
        param.setBeginTradeTime(request.getBeginTime());
        param.setEndTradeTime(request.getEndTime());
        param.setInitiatorId(userId);
        param.setInitiatorName(StringUtils.isBlank(request.getUserName()) ? null : request.getUserName().trim());
        //统计
        if (request.getOutputStatistics()) {
            totalTradeAmount = merchantAssertTradeRecordRepository.countMerchantAmountForDashboard(param);
        }
        //查询
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<MerchantAssertTradeRecord> page = (Page<MerchantAssertTradeRecord>) merchantAssertTradeRecordRepository.queryMerchantForDashboard(param);
        List<QueryDashboardRecordResponse.Item> items = page.getResult().stream()
                .map(x -> new QueryDashboardRecordResponse.Item(
                        USER_CODE_PREFIX.value() + x.getInitiatorId(),
                        x.getInitiatorName(),
                        x.getWalletAddress(),
                        x.getBalance(),
                        x.getTradeTime(),
                        x.getTradeAmount(),
                        x.getPartnerName(),
                        null
                )).collect(Collectors.toList());
        return new QueryDashboardRecordResponse(page.getTotal(), PLATFORM_CURRENCY_SYMBOL.value(), totalTradeAmount, items);
    }
}
