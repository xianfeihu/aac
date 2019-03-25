package com.yz.aac.exchange.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.DateUtil;
import com.yz.aac.common.util.EnumUtil;
import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.model.request.MerchantCurrencyExchangeRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.repository.*;
import com.yz.aac.exchange.repository.domian.*;
import com.yz.aac.exchange.service.MerchantAssertLatestDataService;
import com.yz.aac.exchange.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE;
import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.exchange.Constants.MerchantAssertLatestDataCountTypeEnum.*;
import static com.yz.aac.exchange.Constants.MerchantAssertStatisticsKey.*;
import static com.yz.aac.exchange.Constants.MerchantCurrencyStatisticsAssetTypeEnum.DYNAMIC_STOCK;
import static com.yz.aac.exchange.Constants.MerchantCurrencyStatisticsAssetTypeEnum.FREEZE_STOCK;
import static com.yz.aac.exchange.Constants.MerchantDividendStatus.*;
import static com.yz.aac.exchange.Constants.MerchantOrderSource.SOURCE_APPLETS;
import static com.yz.aac.exchange.Constants.MerchantTradeType.SELL;
import static com.yz.aac.exchange.Constants.PlatformAssertTradeExamineType.FINISH;
import static com.yz.aac.exchange.Constants.PlatformAssertTradeType.*;
import static com.yz.aac.exchange.Constants.UserOrderStatus.UPPER_SHELF;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantAssertLatestDataService latestDataService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private UserAssertFreezeRepository userAssertFreezeRepository;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private MerchantTradeRecordRepository merchantTradeRecordRepository;

    @Autowired
    private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;

    @Autowired
    private MerchantAssertIssuanceRepository assertIssuanceRepository;

    @Autowired
    private MerchantAssertStatisticsRepository assertStatisticsRepository;

    @Autowired
    private MerchantCurrencyStatisticsRepository currencyStatisticsRepository;

    @Autowired
    private MerchantAssertLatestDataRepository merchantAssertLatestDataRepository;

    @Autowired
    private MerchantDividendRecordRepository merchantDividendRecordRepository;


    @Override
    public MerchantCurrencyStatisticsResponse getMerchantCurrencyStatistics(Long merchantId, Integer assetType, Integer countType) throws Exception  {
        // 查询该商户币状态
        Integer status = this.assertIssuanceRepository.queryCurrencyStatusByMerchantId(merchantId);
        if (status == null || status != Constants.IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"未找到相关商户货币信息");
        }

        MerchantCurrencyStatisticsResponse response = new MerchantCurrencyStatisticsResponse();

        Long time = null;
        if (countType.equals(DAY.code())) {
            time = DateUtil.startOfTodDay(new Date()).getTime();
        } else if (countType.equals(WEEK.code())) {
            time = DateUtil.mondayTime().getTime();
        } else if (countType.equals(MONTH.code())) {
            time = DateUtil.monthBeginTime().getTime();
        }

        // 今日数据量
        if (assetType==DYNAMIC_STOCK.code()) {
            response.setSum(this.assertStatisticsRepository.queryAssertStaticSellRest(
                    this.assertIssuanceRepository.queryCurrencyByMerchantId(merchantId),
                    this.userRepository.getUserIdByMerchantId(merchantId),
                    SELL_REST.name())
            );
            response.setLastSum(this.currencyStatisticsRepository.findMerchantCurrencyLastStatisticsByType(merchantId, DYNAMIC_STOCK.code(),time));
        } else if (assetType==FREEZE_STOCK.code()) {
            response.setSum(this.userAssertFreezeRepository.getOrderAssetFreezeSumForMerchant(
                    this.userRepository.getUserIdByMerchantId(merchantId),
                    SELL.code(), UPPER_SHELF.code()));
            response.setLastSum(this.currencyStatisticsRepository.findMerchantCurrencyLastStatisticsByType(merchantId, FREEZE_STOCK.code(),time));
        }
        return response;
    }

    @Override
    public MerchantActiveStockResponse getMerchantActiveStockByAddOrSubtract(Long merchantId, Integer pageNo, Integer pageSize, Integer addOrSubtract, Boolean flag) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        Page<MerchantTradeRecord> page = (Page<MerchantTradeRecord>)this.merchantTradeRecordRepository.findTradeRecordByMerchantId(merchantId, addOrSubtract);
        if (flag) {
            return new MerchantActiveStockResponse(this.merchantTradeRecordRepository.findTradeRecordSumByMerchantId(merchantId, addOrSubtract),page.getTotal(),page.getResult());
        }
        return new MerchantActiveStockResponse(null,page.getTotal(),page.getResult());
    }

    @Override
    public List<MerchantFreezeStockResponse> getMerchantOrderFreezeStock(Long merchantId) throws Exception {
        Long userId = this.userRepository.getUserIdByMerchantId(merchantId);
        if (userId == null ) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "未找到该商户信息");
        }
        return this.userAssertFreezeRepository.getOrderAssetFreezeForMerchant(userId, SELL.code(), UPPER_SHELF.code());
    }

    @Override
    public List<MerchantFreezeStockDetailsResponse> getMerchantOrderFreezeStockDetails(Long merchantId, Long orderId) throws Exception {
        Long userId = this.userRepository.getUserIdByMerchantId(merchantId);
        if (userId == null ) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "未找到该商户信息");
        }
        return this.userAssertFreezeRepository.getOrderAssetFreezeDetailsForMerchant(userId, orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlatStockResponse getPlatTradeRecord(Long merchantId, Integer pageNo, Integer pageSize, Boolean flag) throws Exception {

        Long userId = this.userRepository.getUserIdByMerchantId(merchantId);
        if (userId==null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "未找到该商户信息");
        }

        // 准备返回数据
        BigDecimal platCurrencySum = null;
        Long dividendDate = null;
        Integer dividendStatus = null;
        BigDecimal fixedIncome = null;
        BigDecimal stoDividend = null;
        BigDecimal incomeSum = null;

        // 获取平台币交易记录相关信息
        PageHelper.startPage(pageNo, pageSize);
        Page<PlatStockResponse.Item> page = (Page<PlatStockResponse.Item>)this.merchantTradeRecordRepository.getMerchantPlatTradeRecord(merchantId, userId,
                new Constants.PlatformAssertTradeType[]{DEPOSIT, BUY},
                new Constants.MerchantTradeType[]{Constants.MerchantTradeType.BUY, Constants.MerchantTradeType.SELL},
                SUCCESS_DIVIDEND.code());

        if (flag) {
            platCurrencySum = this.userAssertRepository.getUserAssetsByCurrencySymbol(userId, PLATFORM_CURRENCY_SYMBOL.value());
            MerchantIssueCurrencyResponse issueResponse = this.assertIssuanceRepository.getMerchantIssueCurrencyDetails(merchantId);
            if (issueResponse != null && ( issueResponse.getStoDividendRate()!=null && issueResponse.getStoDividendRate().compareTo(BigDecimal.ZERO)==1 )
                    || (issueResponse.getFixedIncomeRate()!=null && issueResponse.getFixedIncomeRate().compareTo(BigDecimal.ZERO)==1)) {
                MerchantDividendRecord dividendRecord = this.merchantDividendRecordRepository.findRecordByStatus(merchantId, Constants.MerchantDividendStatus.dividendStatus);

                dividendDate = dividendRecord.getDividendDate();
                dividendStatus = dividendRecord.getStatus();
                // 市场流通量
                BigDecimal liquidity = this.currencyStatisticsRepository.getLiquidity(merchantId, Constants.MerchantAssertStatisticsKey.liquidity );
                // 商户币汇率
                String currencySymbol = this.assertIssuanceRepository.queryCurrencyByMerchantId(merchantId);
                BigDecimal closingPrice = merchantAssertLatestDataRepository.getLatelyClosingPrice(currencySymbol);
                if (closingPrice == null){
                    UserOrder uOrder = userOrderRepository.getUserOrderByCurrencySymbol(currencySymbol);
                    closingPrice = null == uOrder ? BigDecimal.ZERO : uOrder.getAabPrice();
                }

                if (issueResponse.getFixedIncomeRate() != null && issueResponse.getFixedIncomeRate().compareTo(BigDecimal.ZERO)!=0) {
                    fixedIncome = liquidity.multiply((issueResponse.getFixedIncomeRate().divide(BigDecimal.valueOf(100)))).multiply(closingPrice).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                if (issueResponse.getStoDividendRate() !=null && issueResponse.getStoDividendRate().compareTo(BigDecimal.ZERO)!=0) {
                    if (dividendRecord.getProfitAmount()==null) {
                        stoDividend =  BigDecimal.valueOf(-1);
                        incomeSum =  BigDecimal.valueOf(-1);
                    } else {
                        stoDividend = dividendRecord.getProfitAmount().multiply(liquidity.divide(issueResponse.getTotal())).multiply(BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value()))).setScale(2,BigDecimal.ROUND_HALF_UP);
                        incomeSum = stoDividend.add(fixedIncome==null ? BigDecimal.ZERO : fixedIncome);
                    }
                }
            }
        }

        return new PlatStockResponse(platCurrencySum, dividendStatus, dividendDate, fixedIncome, stoDividend, incomeSum, page.getTotal(), page.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMerchantTradeRecord(MerchantTradeRecord tradeRecord) throws Exception {
        this.merchantTradeRecordRepository.addMerchantTradeRecord(tradeRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean merchantTransferUser(MerchantCurrencyExchangeRequest request, Long serviceId) throws Exception {
        String currencySymbol = this.assertIssuanceRepository.queryCurrencyByMerchantId(request.getMerchantId());
        if (currencySymbol==null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该商户不存在");
        }
        // 查询该商户币状态
        Integer status = this.assertIssuanceRepository.queryCurrencyStatus(currencySymbol);
        if (status == null || status != Constants.IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该商户币不允许交易");
        }
        Long merchantUserId = this.userRepository.getUserIdByMerchantId(request.getMerchantId());
        if (merchantUserId.equals(request.getUserId())) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"不允许对自己发行的币进行交易！！！");
        }
        Constants.MerchantTradeRecordTransactionModeEnum enums = EnumUtil.getByCode(Constants.MerchantTradeRecordTransactionModeEnum.class,request.getTransactionMode());

        if (request.getUserId()!=null) {
            User user = this.userRepository.getUserById(request.getUserId());
            if (user==null) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"交易失败，该用户不存在");
            }
            UserAssert userAssert = this.userAssertRepository.queryUserAssert(request.getUserId(), currencySymbol);
            if (userAssert==null) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"交易失败，请该用户绑定后重试");
            }

            int i = 0;
            switch (enums) {
                case APPLET_RECHARGE :
                    // 小程序充值
                    i += this.userAssertRepository.subtractUserEffectiveAssets(merchantUserId, currencySymbol, request.getAmount());
                    // 市场流通量改变
                    i += this.assertStatisticsRepository.updateAssertStatic(currencySymbol, SELL_SOLD.name(),request.getAmount());
                    i += this.assertStatisticsRepository.subtractMerchantAssertStatic(currencySymbol, SELL_REST.name(),request.getAmount());
                    if (i != 3) {
                        throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"商户余额不足，交易失败！");
                    }
                    if (userAssert.getSynchronizedTime()!=null) {
                        this.userAssertRepository.addUserAssertBalance(request.getUserId(), currencySymbol, request.getAmount(), request.getAmount());
                    }
                    // 更新当前商户币数据
                    MerchantAssertTradeRecord tradeRecord = new MerchantAssertTradeRecord();
                    tradeRecord.setTradeTime(System.currentTimeMillis());
                    tradeRecord.setCurrencySymbol(currencySymbol);
                    BigDecimal closingPrice = this.merchantAssertLatestDataRepository.getLatestPrices(currencySymbol, DAY.code());
                    if (null == closingPrice) {
                        UserOrder uOrder = userOrderRepository.getUserOrderByCurrencySymbol(currencySymbol);
                        closingPrice = null == uOrder ? BigDecimal.ZERO : uOrder.getAabPrice();
                    }
                    if (closingPrice.compareTo(BigDecimal.ZERO)==0) {
                        throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该商户未定开盘价，暂未能预估市值");
                    }
                    tradeRecord.setPlatformPrice(closingPrice);
                    tradeRecord.setTradeAmount(request.getAmount());
                    this.latestDataService.updateCurrencyMailMsg(tradeRecord, SOURCE_APPLETS);

                    log.info("服务器："+serviceId+"  发来请求--->"+" 用户ID: "+request.getUserId()+" 进行 "+enums.des()+"："+request.getAmount()+" 个  "+currencySymbol);
                    break;
                case PERSONAL_TRANSFER :
                    // 个人转币
                    i += this.userAssertRepository.subtractUserEffectiveAssets(merchantUserId, currencySymbol, request.getAmount());
                    // 市场流通量改变
                    i += this.assertStatisticsRepository.updateAssertStatic(currencySymbol, SELL_SOLD.name(),request.getAmount());
                    i += this.assertStatisticsRepository.subtractMerchantAssertStatic(currencySymbol, SELL_REST.name(),request.getAmount());
                    if (i != 3) {
                        throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"您的可交易余额不足，交易失败！");
                    }
                    if (userAssert.getSynchronizedTime()!=null) {
                        this.userAssertRepository.addUserAssertBalance(request.getUserId(), currencySymbol, request.getAmount(), request.getAmount());
                    }
                    log.info("服务器："+serviceId+"  发来请求--->"+" 商户ID: "+request.getMerchantId()+" 为用户（"+request.getUserId()+"）进行 "+enums.des()+"："+request.getAmount()+" 个  "+currencySymbol);
                    break;
                case STORE_CONSUME :
                    // 店内消费
                    i += this.userAssertRepository.addUserAssertBalance(merchantUserId, currencySymbol, request.getAmount(), request.getAmount());
                    // 市场流通量改变
                    i += this.assertStatisticsRepository.updateAssertStatic(currencySymbol, SELL_REST.name(),request.getAmount());
                    i += this.assertStatisticsRepository.subtractMerchantAssertStatic(currencySymbol, SELL_SOLD.name(),request.getAmount());
                    if (i != 3) {
                        throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"交易失败，请稍后重试！");
                    }
                    if (userAssert.getSynchronizedTime()!=null) {
                        if (userAssert.getBalance().compareTo(request.getAmount())==-1 || this.userAssertRepository.subtractUserEffectiveAssets(request.getUserId(), currencySymbol, request.getAmount())!=1) {
                            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"您的可交易余额不足，交易失败！");
                        }
                    }
                    log.info("服务器："+serviceId+"  发来请求--->"+" 用户ID: "+request.getUserId()+" 进行 "+enums.des()+"："+request.getAmount()+" 个  "+currencySymbol);
                    break;
            }
        }

        // 添加针对商户的挂买单成交信息记录
        this.addMerchantTradeRecord(new MerchantTradeRecord(
                request.getMerchantId(),
                request.getUserId(),
                enums.code(),
                enums.flowDirectionEnum().code(),
                request.getAmount(),
                this.merchantAssertLatestDataRepository.getLatestClosingPrices(currencySymbol, DateUtil.startOfTodDay(new Date()).getTime(), DAY.code()),
                System.currentTimeMillis(),
                enums.addSubtractEnum().code())
        );
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setProfitAmount(Long merchantId, BigDecimal amount, Long serviceId) throws Exception {
        // 获取商户分红信息
        MerchantDividendRecord dividendRecord = this.merchantDividendRecordRepository.findRecordByStatus(merchantId, Constants.MerchantDividendStatus.dividendStatus);
        if (dividendRecord == null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"未找到该商户的分红信息");
        }
        if (this.merchantDividendRecordRepository.updateRecordById(
                new MerchantDividendRecord(dividendRecord.getId(),
                        null,
                        null,
                        null,
                        amount.divide(BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value()))),
                        null,
                        null)) !=1 ){
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"设置失败 请稍后重试");
        }
        log.info("服务器："+serviceId+"  发来请求--->"+" 商户ID："+merchantId + "修改盈利额为：" + amount.divide(BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value()))));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMerchantDividendRecord() {
        Long toDayBeginTime = DateUtil.startOfTodDay(new Date()).getTime();
        List<MerchantDividendRecord> list = this.merchantDividendRecordRepository.getToDayAllMerchantDividendRecord(toDayBeginTime, new Constants.MerchantDividendStatus[]{WAIT_DIVIDEND, ISSUE_DIVIDEND, OVERDUE});
        list.stream().forEach(x -> {
            if (x.getStatus().equals(WAIT_DIVIDEND.code()) && x.getDividendDate().equals(toDayBeginTime)) {
                this.merchantDividendRecordRepository.updateRecordById(new MerchantDividendRecord(x.getId(), null, null, null, null, null, ISSUE_DIVIDEND.code()));
            } else {
                Long day = (toDayBeginTime-x.getDividendDate())/DateUtil.MILLIS_PER_DAY;
                if ( day >= Calendar.DAY_OF_WEEK ) {
                    try {
                        ((MerchantServiceImpl) AopContext.currentProxy()).userDividendProcess(x);
                    } catch (Exception e) {
                        log.info("定时任务（分红）--  执行失败  --->" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 进入用户分红流程
     * @param x
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class, propagation = REQUIRES_NEW)
    public void userDividendProcess(MerchantDividendRecord x) {
        Long nowTime = System.currentTimeMillis();
        /**
         * 进入分红流程
         */
        // 总分红金额
        BigDecimal dividendAmount = BigDecimal.ZERO;
        // 扣除部分金额
        BigDecimal deductionAmount = BigDecimal.ZERO;
        // 获取商户下平台币的存量
        Long merchantUserId = this.userRepository.getUserIdByMerchantId(x.getMerchantId());
        // 商户发行货币符号
        String currencySymbol = this.assertIssuanceRepository.queryCurrencyByMerchantId(x.getMerchantId());
        // 商户的平台币有效余额
        BigDecimal availableFunds = this.userAssertRepository.getUserAvailableFundsByCurrencySymbol(merchantUserId, PLATFORM_CURRENCY_SYMBOL.value());
        // 该商户币的市场流通量
        BigDecimal marketLiquidity = this.assertStatisticsRepository.queryMarketLiquidity(currencySymbol,MINING_MIND.name(), SELL_SOLD.name());
        // 获取商户发币的分红设置
        MerchantIssueCurrencyResponse merchantIssueDetails = this.assertIssuanceRepository.getMerchantIssueCurrencyDetails(x.getMerchantId());
        // 最近一笔交易单价
        BigDecimal closingPrice = merchantAssertLatestDataRepository.getLatestPrices(currencySymbol ,DAY.code());
        // 防止上次分红数据没有清除
        List<MerchantDividendTempData> dividendTempDataList = this.merchantDividendRecordRepository.getAllDividendMessage(merchantUserId);
        // 是否存在分红
        boolean flag = true;

        // 存在STO分红但未设置盈利额
        if (!dividendTempDataList.isEmpty() || (merchantIssueDetails.getStoDividendRate()!=null && x.getProfitAmount()==null)) {
            this.merchantDividendRecordRepository.updateRecordById(new MerchantDividendRecord(x.getId(),  null,  null,  null,  null,  null,  OVERDUE.code()));
            return;
        }

        if (marketLiquidity.compareTo(BigDecimal.ZERO) > 0 && closingPrice != null) {
            if (merchantIssueDetails.getStoDividendRate()!=null && merchantIssueDetails.getStoDividendRate().compareTo(BigDecimal.ZERO)==1) {
                // 计算STO分红数额
                dividendAmount = dividendAmount.add(x.getProfitAmount().multiply(marketLiquidity).divide(merchantIssueDetails.getTotal()).multiply(BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value()))).setScale(2,BigDecimal.ROUND_HALF_UP));
            }
            if (merchantIssueDetails.getFixedIncomeRate()!=null && merchantIssueDetails.getFixedIncomeRate().compareTo(BigDecimal.ZERO)==1) {
                // 计算固定分红数额
                dividendAmount = dividendAmount.add(marketLiquidity.multiply((merchantIssueDetails.getFixedIncomeRate().divide(BigDecimal.valueOf(100)))).multiply(closingPrice).setScale(2,BigDecimal.ROUND_HALF_UP));
            }
            if (dividendAmount.compareTo(BigDecimal.ZERO)==0) {
                // 无分红
                flag = false;
            } else {
                // 余额不足
                if (availableFunds.compareTo(dividendAmount)==-1) {
                    this.merchantDividendRecordRepository.updateRecordById(new MerchantDividendRecord(x.getId(),  null,  null,  null,  null,  null,  OVERDUE.code()));
                    return;
                }
                // 计算 一个商户币可以 分得平台币数量
                BigDecimal unit = dividendAmount.divide(marketLiquidity, 10 ,BigDecimal.ROUND_FLOOR);
                // 统计用户分红信息
                if (this.merchantDividendRecordRepository.statisticalUserDividendInfo(merchantUserId, unit, currencySymbol)>0) {
                    // 扣除商户余额
                    if (this.userAssertRepository.subtractUserEffectiveAssets(merchantUserId, PLATFORM_CURRENCY_SYMBOL.value(), deductionAmount)!=1) {
                        // 回滚事务，记录分红失败日志
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        log.info("定时任务（分红）--扣除商户余额执行失败  ---> 商户ID：" + x.getMerchantId());
                        this.merchantDividendRecordRepository.updateRecordById(new MerchantDividendRecord(x.getId(), null, null, null, null, null, OVERDUE.code()));
                        return;
                    }
                    // 为用户按比例分红
                    this.merchantDividendRecordRepository.dividendToUserAsset(merchantUserId, PLATFORM_CURRENCY_SYMBOL.value());
                    // 记录用户分红信息
                    List<PlatformAssertTradeRecord> tradeRecords = new ArrayList<>();
                    dividendTempDataList = this.merchantDividendRecordRepository.getAllDividendMessage(merchantUserId);
                    UserAssert merchantUserAssert = this.userAssertRepository.queryUserAssert(merchantUserId, PLATFORM_CURRENCY_SYMBOL.value());
                    User merchantUser = this.userRepository.getUserById(merchantUserId);
                    for (MerchantDividendTempData y : dividendTempDataList) {

                        deductionAmount = deductionAmount.add(y.getAmount());
                        merchantUserAssert.setBalance(merchantUserAssert.getBalance().subtract(y.getAmount()));
                        availableFunds = availableFunds.subtract(y.getAmount());

                        tradeRecords.add(new PlatformAssertTradeRecord(merchantUserId,
                                merchantUser.getName(),
                                DIVIDEND.code(),
                                nowTime,
                                BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value())),
                                y.getAmount(),
                                merchantUserAssert.getWalletAddress(),
                                merchantUserAssert.getBalance(),
                                availableFunds,
                                y.getUserId(),
                                null,
                                null,
                                FINISH.code(),
                                null));
                    }
                    tradeRecords.stream().forEach(z -> this.platformAssertTradeRecordRepository.addUserDividendRecordInfo(z));
                }
            }
        }

        // 修改商户分红记录
        this.merchantDividendRecordRepository.updateRecordById(new MerchantDividendRecord(x.getId(), null,  null, nowTime, null, deductionAmount, SUCCESS_DIVIDEND.code()));

        // 创建下一周期的分红信息
        if (flag) {
            this.addMerchantDividendRecord(x.getMerchantId(),this.assertIssuanceRepository.getMerchantIssueCurrencyDetails(x.getMerchantId()).getIncomePeriod());
        }
    }

    @Override
    public void addMerchantDividendRecord(Long merchantId, Integer incomePeriod) {
        // 获取最近的分红信息
        MerchantDividendRecord dividendRecord = this.merchantDividendRecordRepository.findLatelyDividendRecordByStatus(merchantId);

        Long toDayBeginTime = DateUtil.startOfTodDay(new Date()).getTime();
        Long nextDividendDate = DateUtil.getSomeDay(new Date(dividendRecord.getDividendDate()), incomePeriod).getTime();
        Integer code = WAIT_DIVIDEND.code();
        if (toDayBeginTime >= nextDividendDate + Calendar.DAY_OF_WEEK * DateUtil.MILLIS_PER_DAY) {
            code = OVERDUE.code();
        } else if (toDayBeginTime > nextDividendDate){
            code = ISSUE_DIVIDEND.code();
        }
        if (dividendRecord.getStatus().equals(SUCCESS_DIVIDEND.code()) || dividendRecord.getStatus().equals(OVERDUE.code())) {
            this.merchantDividendRecordRepository.addMerchantDividendRecord(new MerchantDividendRecord(null,merchantId,nextDividendDate,
                    null, null, null, code));
        }
    }

    @Override
    public MerchantAndPlatExchangeRateResponse exchangeRate(String currencySymbol) throws Exception {
        // 查询该商户币状态
        Integer status = this.assertIssuanceRepository.queryCurrencyStatus(currencySymbol);
        if (status == null || status != Constants.IssuanceAuditStatus.DEPOSIT_YES.code()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该商户币不允许交易");
        }
        BigDecimal closingPrice = merchantAssertLatestDataRepository.getLatestPrices(currencySymbol, DAY.code());
        if (null == closingPrice) {
            UserOrder uOrder = userOrderRepository.getUserOrderByCurrencySymbol(currencySymbol);
            closingPrice = null == uOrder ? BigDecimal.ZERO : uOrder.getAabPrice();
        }
        if (closingPrice.compareTo(BigDecimal.ZERO)==0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该商户未定开盘价，暂未能预估市值");
        }
        return new MerchantAndPlatExchangeRateResponse(BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value())),closingPrice);
    }

}
