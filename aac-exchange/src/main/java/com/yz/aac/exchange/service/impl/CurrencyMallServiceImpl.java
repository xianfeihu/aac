package com.yz.aac.exchange.service.impl;

import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.DateUtil;
import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.model.request.CandlestickChartMsgRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.repository.*;
import com.yz.aac.exchange.repository.domian.MerchantAssertLatestData;
import com.yz.aac.exchange.repository.domian.MerchantAssertTodayTradeRecord;
import com.yz.aac.exchange.service.CurrencyMallService;
import com.yz.aac.exchange.service.ExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION;
import static com.yz.aac.exchange.Constants.*;
import static com.yz.aac.exchange.Constants.MerchantAssertLatestDataCountTypeEnum.DAY;
import static com.yz.aac.exchange.Constants.MerchantAssertLatestDataCountTypeEnum.WEEK;
import static com.yz.aac.exchange.Constants.MerchantAssertStatisticsKey.MINING_MIND;
import static com.yz.aac.exchange.Constants.MerchantAssertStatisticsKey.SELL_SOLD;
import static com.yz.aac.exchange.Constants.MerchantTradeType.BUY;
import static com.yz.aac.exchange.Constants.MerchantTradeType.SELL;
import static com.yz.aac.exchange.Constants.Misc.*;

@Service
@Slf4j
public class CurrencyMallServiceImpl implements CurrencyMallService {

    @Autowired
    private ExchangeService exchangeServiceImpl;

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private CurrencyMallRepository currencyMallRepository;

    @Autowired
    private MerchantAssertLatestDataRepository latestDataRepository;

    @Autowired
    private MerchantAssertIssuanceRepository assertIssuanceRepository;

    @Autowired
    private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;

    @Autowired
    private MerchantAssertTodayTradeRecordRepository merchantAssertTodayTradeRecordRepository;

    @Override
    public List<CurrencyMallIndexInfoResponse> getIndexInfo() throws Exception {
        return this.currencyMallRepository.getIndexInfo();
    }

    @Override
    public List<CurrencyMallIndexInfoResponse> searchIndexInfo(String currencySymbol) throws Exception {
        return this.currencyMallRepository.searchIndexInfo(currencySymbol);
    }

    @Override
    public List<MerchantAssertTodayTradeRecord> exchangeTradInfo(String currencySymbol) {
        List list = new ArrayList<MerchantAssertTodayTradeRecord>();
        List<MerchantAssertTodayTradeRecord> list_1 = this.merchantAssertTodayTradeRecordRepository.selectByTpyeAndSymbol(SELL.code(),currencySymbol);
        list.addAll(list_1);
        for (int i = 0; i < 5 - list_1.size(); i++) {
            list.add(new MerchantAssertLatestDataServiceImpl());
        }
        List<MerchantAssertTodayTradeRecord> list_2 = this.merchantAssertTodayTradeRecordRepository.selectByTpyeAndSymbol(BUY.code(),currencySymbol);
        list.addAll(list_2);
        for (int i = 0; i < 5 - list_2.size(); i++) {
            list.add(new MerchantAssertLatestDataServiceImpl());
        }
        return list;
    }

    @Override
    public List<MerchantAssertTodayTradeRecord> candlestickChartMsgForNow(String currencySymbol) throws Exception {
        // 查询该商户币状态
        Integer status = this.assertIssuanceRepository.queryCurrencyStatus(currencySymbol);
        if (currencySymbol.equals(PLATFORM_CURRENCY_SYMBOL.value()) || status == null || status != IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"未找到相关币的交易信息");
        }
        return this.merchantAssertTodayTradeRecordRepository.candlestickChartMsgForNow(currencySymbol,DateUtil.startOfTodDay(new Date()).getTime());
    }

    @Override
    public List<CandlestickChartMsgResponse> candlestickChartMsg(CandlestickChartMsgRequest request) throws Exception {
        // 查询该商户币状态
        Integer status = this.assertIssuanceRepository.queryCurrencyStatus(request.getCurrencySymbol());
        if (request.getCurrencySymbol().equals(PLATFORM_CURRENCY_SYMBOL.value()) || status == null || status != IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"未找到相关币的交易信息");
        }
        if (request.getType().equals(DAY.code())) {
            // 2月限制
            request.setNum(Integer.valueOf(TWO_MONTHS_FOR_DAY.value()));
        } else if (request.getType().equals(WEEK.code())) {
            // 12月限制
            request.setNum(Integer.valueOf(ONE_YEAR_FOR_WEEK.value()));
        } else {
            request.setNum(null);
        }
        return this.latestDataRepository.candlestickChartMsg(request);
    }

    @Override
    public CandlestickChartBaseMsgResponse candlestickChartBaseMsg(String currencySymbol) throws Exception {
        // 查询该商户币状态
        Long toDayBeginTime = DateUtil.startOfTodDay(new Date()).getTime();
        Integer status = this.assertIssuanceRepository.queryCurrencyStatus(currencySymbol);
        if (status == null || status != IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"未找到相关币的交易信息");
        }
        CandlestickChartBaseMsgResponse chartBaseMsgResponse = this.currencyMallRepository.getAllCurrencyNowMsgForSymbol(toDayBeginTime, currencySymbol, DAY.code());
        if ( chartBaseMsgResponse == null ) {
            BigDecimal latestClosingPrices = this.latestDataRepository.getLatestClosingPrices(currencySymbol, toDayBeginTime, DAY.code());
            chartBaseMsgResponse = new CandlestickChartBaseMsgResponse(
                currencySymbol, null, null, latestClosingPrices,
                null, BigDecimal.ZERO, BigDecimal.ZERO, 0, DAY.code(), System.currentTimeMillis(),
                this.latestDataRepository.getYesterdayClosingPrice(currencySymbol,DAY.code(), toDayBeginTime-DateUtil.MILLIS_PER_DAY, toDayBeginTime),
                this.assertIssuanceRepository.findTotalForCurrencySymbol(currencySymbol));
        }
        MerchantAssertLatestData yesterdayMerchantData =  this.latestDataRepository.getYesterdayMerchantData(currencySymbol, DAY.code(), toDayBeginTime-DateUtil.MILLIS_PER_DAY, toDayBeginTime);
        // 设置昨日总成交量
        chartBaseMsgResponse.setCurrencyNum(yesterdayMerchantData==null ? 0 : yesterdayMerchantData.getCurrencyNum());
        chartBaseMsgResponse.setAppletTradNum(yesterdayMerchantData==null ? BigDecimal.ZERO : yesterdayMerchantData.getAppletTradNum());
        chartBaseMsgResponse.setPlatformTradNum(yesterdayMerchantData==null ? BigDecimal.ZERO : yesterdayMerchantData.getPlatformTradNum());
        chartBaseMsgResponse.setLiquidity(this.merchantAssertStatisticsRepository.queryMarketLiquidity(currencySymbol, MINING_MIND.name(), SELL_SOLD.name()));
        return chartBaseMsgResponse;
    }

    @Override
    public CurrencyIntroductionResponse currencyIntroduction(String currencySymbol) throws Exception {
        // 查询该商户币状态
        Integer status = this.assertIssuanceRepository.queryCurrencyStatus(currencySymbol);
        if (status == null || status != IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"未找到相关币的交易信息");
        }
        CurrencyIntroductionResponse response = this.assertIssuanceRepository.currencyIntroduction(currencySymbol, Arrays.stream(Constants.MerchantAssertStatisticsKey.liquidity).collect(Collectors.toSet()));
        response.setKChartPath(K_CHART_PATH.value() + currencySymbol);
        return response;
    }

    @Override
    public CurrencyTradingInfoResponse buyCurrencyBySymbol(String currencySymbol, Long userId) throws Exception {
        Long maxAmountOrderId = this.userOrderRepository.getMaxAmountOrder(userId, currencySymbol, UserOrderStatus.UPPER_SHELF.code(), SELL.code(),"ASC");
        if (maxAmountOrderId == null) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"我要挂买单");
        }
        return this.exchangeServiceImpl.getCurrencyTradingInfo(currencySymbol,maxAmountOrderId,userId);
    }

    @Override
    public CurrencyTradingInfoResponse sellCurrencyBySymbol(String currencySymbol, Long userId) throws Exception {
        Long maxAmountOrderId = this.userOrderRepository.getMaxAmountOrder(userId, currencySymbol, UserOrderStatus.UPPER_SHELF.code(), MerchantTradeType.BUY.code(),"DESC");
        if (maxAmountOrderId == null) {
            throw new BusinessException(MSG_INTERACTIVE_EXCEPTION.code(),"我要挂卖单");
        }
        return this.exchangeServiceImpl.getCurrencyTradingInfo(currencySymbol,maxAmountOrderId,userId);
    }

}
