package com.yz.aac.exchange.service.impl;

import com.yz.aac.common.util.DateUtil;
import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.repository.MerchantAssertLatestDataRepository;
import com.yz.aac.exchange.repository.MerchantAssertTodayTradeRecordRepository;
import com.yz.aac.exchange.repository.domian.MerchantAssertLatestData;
import com.yz.aac.exchange.repository.domian.MerchantAssertTodayTradeRecord;
import com.yz.aac.exchange.repository.domian.MerchantAssertTradeRecord;
import com.yz.aac.exchange.service.MerchantAssertLatestDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.yz.aac.exchange.Constants.MerchantAssertLatestDataCountTypeEnum.DAY;
import static com.yz.aac.exchange.Constants.MerchantOrderSource.SOURCE_APP;
import static com.yz.aac.exchange.Constants.MerchantOrderSource.SOURCE_APPLETS;

@Slf4j
@Service
public class MerchantAssertLatestDataServiceImpl implements MerchantAssertLatestDataService {

    @Autowired
    private MerchantAssertLatestDataRepository latestDataRepository;

    @Autowired
    private MerchantAssertTodayTradeRecordRepository merchantAssertTodayTradeRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrencyMailMsg(MerchantAssertTradeRecord tradeRecord, Constants.MerchantOrderSource source) throws Exception {

        BigDecimal platformTradNum = source.code()==SOURCE_APP.code() ? tradeRecord.getTradeAmount() : BigDecimal.ZERO;
        BigDecimal appletTradNum = source.code()==SOURCE_APPLETS.code() ? tradeRecord.getTradeAmount() : BigDecimal.ZERO;

        MerchantAssertLatestData chart;
        Long startOfTodDay = DateUtil.startOfTodDay(new Date()).getTime();
        // 获取当日最新数据
        if (tradeRecord.getTradeTime() < startOfTodDay) {
            // 获取昨日的统计信息
            chart = this.latestDataRepository.getCandlestickChartForTime(tradeRecord.getCurrencySymbol(), DAY.code(), startOfTodDay - DateUtils.MILLIS_PER_DAY);
        } else {
            // 获取今日的统计信息
            chart = this.latestDataRepository.getCandlestickChartForTime(tradeRecord.getCurrencySymbol(), DAY.code(), startOfTodDay);
        }
        if (chart != null) {
            this.latestDataRepository.update(chart.getId(), tradeRecord.getPlatformPrice(), platformTradNum, appletTradNum);
            if ( tradeRecord.getPlatformPrice().compareTo(chart.getMaxPrice()) == 1 ) {
                this.latestDataRepository.updateMaxPrice(chart.getId(), tradeRecord.getPlatformPrice());
            }
            if ( tradeRecord.getPlatformPrice().compareTo(chart.getMinPrice()) == -1 ) {
                this.latestDataRepository.updateMinPrice(chart.getId(), tradeRecord.getPlatformPrice());
            }
        } else {
            // 组装数据
            this.latestDataRepository.add(new MerchantAssertLatestData(
                    null,
                    tradeRecord.getCurrencySymbol(),
                    tradeRecord.getPlatformPrice(),
                    tradeRecord.getPlatformPrice(),
                    tradeRecord.getPlatformPrice(),
                    tradeRecord.getPlatformPrice(),
                    getLatestClosingPrices(tradeRecord.getCurrencySymbol()),
                    platformTradNum,
                    appletTradNum,
                    1,
                    DAY.code(),
                    tradeRecord.getTradeTime()
            ));
        }

        // 添加今日交易数据
        if (tradeRecord.getOrderId()!=null) {
            this.merchantAssertTodayTradeRecordRepository.addTradeRecordMessage(new MerchantAssertTodayTradeRecord(null,
                    tradeRecord.getCurrencySymbol(),
                    tradeRecord.getTradeType(),
                    tradeRecord.getPlatformPrice(),
                    tradeRecord.getTradeAmount(),
                    tradeRecord.getTradeTime(),
                    tradeRecord.getOrderId()));
        }
    }

    @Override
    public BigDecimal getLatestClosingPrices(String currencySymbol) throws Exception {
        return this.latestDataRepository.getLatestClosingPrices(currencySymbol, DateUtil.startOfTodDay(new Date()).getTime(), DAY.code());
    }
}
