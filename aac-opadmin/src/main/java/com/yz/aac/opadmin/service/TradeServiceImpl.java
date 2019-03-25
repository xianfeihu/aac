package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.Constants.PlatformTradeType;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryMerchantRecordResponse;
import com.yz.aac.opadmin.model.response.QueryPlatformOrderResponse;
import com.yz.aac.opadmin.model.response.QueryPlatformRecordResponse;
import com.yz.aac.opadmin.repository.*;
import com.yz.aac.opadmin.repository.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.*;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;
import static com.yz.aac.opadmin.Constants.FeatureModuleAction.PLATFORM_CURRENCY_TRADE_ADD_SELLING_ORDER;
import static com.yz.aac.opadmin.Constants.FeatureModuleAction.PLATFORM_CURRENCY_TRADE_TRANSFER;
import static com.yz.aac.opadmin.Constants.LedgerInOut.IN;
import static com.yz.aac.opadmin.Constants.LedgerInOut.OUT;
import static com.yz.aac.opadmin.Constants.LedgerType.TRANSFER_PLATFORM_CURRENCY;
import static com.yz.aac.opadmin.Constants.PlatformTradeStatus.TRANSFERRED;
import static com.yz.aac.opadmin.Constants.PlatformTradeStatus.WAITING_TRANSFER;
import static com.yz.aac.opadmin.Constants.PlatformTradeType.BUY;
import static com.yz.aac.opadmin.Constants.PlatformTradeType.ISSUANCE_DEPOSIT;
import static com.yz.aac.opadmin.Constants.UserAssertFreezingReason.DEPOSIT;
import static com.yz.aac.opadmin.Constants.UserBehaviour.TRADE;
import static com.yz.aac.opadmin.Constants.UserIncomeSource.RECHARGE;

@Service
@Slf4j
public class TradeServiceImpl implements TradeService {

    @Autowired
    private MerchantAssertTradeRecordRepository merchantRecordRepository;

    @Autowired
    private PlatformAssertTradeRecordRepository platformRecordRepository;

    @Autowired
    private MerchantAssertIssuanceRepository issuanceRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private PlatformAssertSellingOrderRepository sellingOrderRepository;

    @Autowired
    private PlatformAssertSellerRepository sellerRepository;

    @Autowired
    private PlatformAssertIncomeExpenditureRecordRepository ledgerRepository;

    @Autowired
    private UserBehaviourStatisticsRepository behaviourRepository;

    @Autowired
    private OperatorActionLogRepository logRepository;

    @Autowired
    private UserIncomeStatisticsRepository incomeRepository;

    @Autowired
    private UserAssertFreezeRepository userAssertFreezeRepository;

    @Override
    public QueryMerchantRecordResponse queryMerchantRecords(QueryMerchantRecordRequest request) throws Exception {
        //解析用户编码
        Long userId;
        try {
            userId = StringUtils.isBlank(request.getUserCode()) ? null : Long.parseLong(request.getUserCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        //查询
        MerchantAssertTradeRecord param = new MerchantAssertTradeRecord();
        param.setBeginTradeTime(request.getBeginTime());
        param.setEndTradeTime(request.getEndTime());
        param.setCurrencySymbol(StringUtils.isBlank(request.getCurrencySymbol()) ? null : request.getCurrencySymbol().trim());
        param.setTradeType(request.getDirection());
        param.setInitiatorId(userId);
        param.setInitiatorName(StringUtils.isBlank(request.getUserName()) ? null : request.getUserName().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<MerchantAssertTradeRecord> page = (Page<MerchantAssertTradeRecord>) merchantRecordRepository.query(param);
        List<QueryMerchantRecordResponse.Item> items = new ArrayList<>();
        if (!page.isEmpty()) {
            //获取所有商家币种信息
            Map<String, MerchantAssertIssuance> symbol2Merchant = new HashMap<>();
            issuanceRepository.queryAllSymbols().forEach(x -> symbol2Merchant.put(x.getCurrencySymbol(), x));
            //索取结果中相关用户角色信息
            Map<Long, Integer> user2IsMerchant = new HashMap<>();
            Set<Long> partnerIds = page.getResult().stream().map(MerchantAssertTradeRecord::getPartnerId).collect(Collectors.toSet());
            UserRole roleCondition = new UserRole();
            roleCondition.setUserIds(partnerIds);
            userRoleRepository.query(roleCondition).forEach(x -> user2IsMerchant.put(x.getUserId(), x.getIsMerchant()));
            //组装结果
            items = page.getResult()
                    .stream()
                    .map(x -> new QueryMerchantRecordResponse.Item(
                            USER_CODE_PREFIX.value() + x.getInitiatorId(),
                            x.getInitiatorName(),
                            x.getTradeType(),
                            x.getCurrencySymbol(),
                            symbol2Merchant.containsKey(x.getCurrencySymbol()) ? symbol2Merchant.get(x.getCurrencySymbol()).getMerchantName() : null,
                            x.getTradeAmount(),
                            x.getPlatformPrice(),
                            PLATFORM_CURRENCY_SYMBOL.value(),
                            x.getTradeTime(),
                            x.getPartnerName(),
                            user2IsMerchant.get(x.getPartnerId())
                    )).collect(Collectors.toList());
        }
        return new QueryMerchantRecordResponse(page.getTotal(), items);
    }

    @Override
    public QueryPlatformRecordResponse queryPlatformRecords(QueryPlatformRecordRequest request) {
        //查询
        PlatformAssertTradeRecord param = new PlatformAssertTradeRecord();
        param.setId(request.getId());
        param.setPayNumber(request.getPayNumber());
        param.setTradeTypes(Arrays.asList(ISSUANCE_DEPOSIT.value(), BUY.value()));
        param.setBeginTradeTime(request.getBeginTime());
        param.setEndTradeTime(request.getEndTime());
        param.setInitiatorName(request.getUserName());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<PlatformAssertTradeRecord> page = (Page<PlatformAssertTradeRecord>) platformRecordRepository.query(param);
        List<QueryPlatformRecordResponse.Item> items = new ArrayList<>();
        BigDecimal rmbExchangeRate = new BigDecimal(PLATFORM_CURRENCY_EXCHANGE_RATE.value());
        if (!page.isEmpty()) {
            //组装结果
            items = page.getResult()
                    .stream()
                    .map(x -> new QueryPlatformRecordResponse.Item(
                            x.getId(),
                            x.getPartnerId(),
                            x.getPartnerName(),
                            x.getAvailableTradeAmount(),
                            PLATFORM_CURRENCY_SYMBOL.value(),
                            x.getMinAmountLimit(),
                            x.getMaxAmountLimit(),
                            null == x.getMinAmountLimit() ? new BigDecimal(0) : x.getMinAmountLimit().multiply(rmbExchangeRate),
                            null == x.getMaxAmountLimit() ? new BigDecimal(0) : x.getMaxAmountLimit().multiply(rmbExchangeRate),
                            x.getRmbPrice(),
                            x.getTradeTime(),
                            x.getInitiatorName(),
                            x.getPayNumber(),
                            x.getWalletAddress(),
                            x.getTradeAmount(),
                            x.getStatus()
                    )).collect(Collectors.toList());
        }
        return new QueryPlatformRecordResponse(page.getTotal(), items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferPlatformRecord(TransferPlatformRecordRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        BigDecimal rmbExchangeRate = new BigDecimal(PLATFORM_CURRENCY_EXCHANGE_RATE.value());
        //查询及校验
        PlatformAssertTradeRecord param = new PlatformAssertTradeRecord();
        param.setId(request.getId());
        List<PlatformAssertTradeRecord> records = platformRecordRepository.query(param);
        if (records.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        PlatformAssertTradeRecord record = records.iterator().next();
        if (WAITING_TRANSFER.value() != record.getStatus() || ISSUANCE_DEPOSIT.value() != record.getTradeType() && BUY.value() != record.getTradeType()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_STATUS.value());
        }
        if (null == record.getSellingOrderId()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_PLATFORM_SELLING_ORDER.value());
        }
        //更新交易记录
        record.setStatus(TRANSFERRED.value());
        platformRecordRepository.update(record);
        if (ISSUANCE_DEPOSIT.value() == record.getTradeType()) {
            // 将押金冻结
            userAssertFreezeRepository.store(new UserAssertFreeze(null, record.getInitiatorId(), PLATFORM_CURRENCY_SYMBOL.value(), record.getTradeAmount(), DEPOSIT.code(), sysDate));
        }
        // 增加购买者平台币余额
        UserAssert userAssert = new UserAssert();
        userAssert.setUserId(record.getInitiatorId());
        userAssert.setCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
        List<UserAssert> asserts = userAssertRepository.query(userAssert);
        if (asserts.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_USER_ASSERT.value());
        }
        userAssert = asserts.iterator().next();
        userAssert.setBalance(userAssert.getBalance().add(record.getTradeAmount()));
        userAssert.setHistoryMaxBalance(userAssert.getHistoryMaxBalance().add(record.getTradeAmount()));
        userAssertRepository.update(userAssert);
        // 更新购买者收入统计
        UserIncomeStatistics income = new UserIncomeStatistics(null, record.getInitiatorId(), PLATFORM_CURRENCY_SYMBOL.value(), RECHARGE.value(), null);
        List<UserIncomeStatistics> incomes = incomeRepository.query(income);
        if (incomes.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_USER_INCOME.value());
        }
        income = incomes.iterator().next();
        income.setValue(income.getValue().add(record.getTradeAmount()));
        incomeRepository.update(income);
        // 写入平台币出帐
        ledgerRepository.store(new PlatformAssertIncomeExpenditureRecord(
                null,
                OUT.value(),
                null,
                record.getInitiatorId(),
                record.getInitiatorName(),
                sysDate,
                null,
                null,
                record.getTradeAmount(),
                null,
                TRANSFER_PLATFORM_CURRENCY.value(),
                null,
                null,
                null
        ));
        //更新平台币卖单
        PlatformAssertSellingOrder order = new PlatformAssertSellingOrder();
        order.setId(record.getSellingOrderId());
        List<PlatformAssertSellingOrder> orders = sellingOrderRepository.query(order);
        if (orders.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_PLATFORM_SELLING_ORDER.value());
        }
        order = orders.iterator().next();
        order.setAvailableTradeAmount(order.getAvailableTradeAmount().subtract(record.getTradeAmount()));
        order.setUpdateTime(sysDate);
        sellingOrderRepository.update(order);
        //更新平台币卖家信息
        PlatformAssertSeller seller = new PlatformAssertSeller();
        seller.setId(order.getSellerId());
        List<PlatformAssertSeller> sellers = sellerRepository.query(seller);
        if (sellers.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_PLATFORM_SELLER.value());
        }
        seller = sellers.iterator().next();
        seller.setTotalSoldCurrency(seller.getTotalSoldCurrency().add(record.getTradeAmount()));
        seller.setTotalSoldCount(seller.getTotalSoldCount() + 1);
        seller.setUpdateTime(sysDate);
        sellerRepository.update(seller);
        // 写入法币进帐
        ledgerRepository.store(new PlatformAssertIncomeExpenditureRecord(
                null,
                IN.value(),
                null,
                record.getInitiatorId(),
                record.getInitiatorName(),
                sysDate,
                null,
                null,
                record.getTradeAmount(),
                record.getTradeAmount().multiply(rmbExchangeRate),
                Constants.LedgerType.RECHARGE_PLATFORM_CURRENCY.value(),
                null,
                null,
                null
        ));
        //更新用户行为统计
        UserBehaviourStatistics stat = new UserBehaviourStatistics(null, record.getInitiatorId(), TRADE.value(), null);
        List<UserBehaviourStatistics> statList = behaviourRepository.query(stat);
        if (statList.isEmpty()) {
            stat.setValue(1);
            behaviourRepository.store(stat);
        } else {
            stat = statList.iterator().next();
            stat.setValue(stat.getValue() + 1);
            behaviourRepository.update(stat);
        }
        //写入操作员访问日志
        logRepository.store(new OperatorActionLog(
                null,
                request.getLoginId(),
                PLATFORM_CURRENCY_TRADE_TRANSFER.moduleId(),
                PLATFORM_CURRENCY_TRADE_TRANSFER.actionId(),
                PLATFORM_CURRENCY_TRADE_TRANSFER.additionalInfo()
                        .replaceFirst(PLACEHOLDER.value(), record.getInitiatorName())
                        .replaceFirst(PLACEHOLDER.value(), record.getPartnerName())
                        .replaceFirst(PLACEHOLDER.value(), record.getTradeAmount().toString())
                        .replaceFirst(PLACEHOLDER.value(), PLATFORM_CURRENCY_SYMBOL.value()),
                sysDate,
                null,
                null
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPlatformOrder(CreatePlatformOrderRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        PlatformAssertSeller seller = new PlatformAssertSeller();
        seller.setId(request.getSellerId());
        List<PlatformAssertSeller> sellers = sellerRepository.query(seller);
        if (sellers.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_PLATFORM_SELLER.value());
        }
        PlatformAssertSellingOrder order = new PlatformAssertSellingOrder(
                null,
                request.getSellerId(),
                null,
                request.getAvailableTradeAmount(),
                request.getMinAmountLimit(),
                request.getMaxAmountLimit(),
                request.getRmbPrice(),
                StringUtils.isNotBlank(request.getRemark()) ? request.getRemark().trim() : null,
                sysDate,
                sysDate
        );
        sellingOrderRepository.store(order);
        //写入操作员访问日志
        seller = sellers.iterator().next();
        logRepository.store(new OperatorActionLog(
                null,
                request.getLoginId(),
                PLATFORM_CURRENCY_TRADE_ADD_SELLING_ORDER.moduleId(),
                PLATFORM_CURRENCY_TRADE_ADD_SELLING_ORDER.actionId(),
                PLATFORM_CURRENCY_TRADE_ADD_SELLING_ORDER.additionalInfo()
                        .replaceFirst(PLACEHOLDER.value(), request.getRmbPrice().toPlainString())
                        .replaceFirst(PLACEHOLDER.value(), request.getMinAmountLimit().toPlainString())
                        .replaceFirst(PLACEHOLDER.value(), PLATFORM_CURRENCY_SYMBOL.value())
                        .replaceFirst(PLACEHOLDER.value(), request.getMaxAmountLimit().toPlainString())
                        .replaceFirst(PLACEHOLDER.value(), PLATFORM_CURRENCY_SYMBOL.value())
                        .replaceFirst(PLACEHOLDER.value(), request.getAvailableTradeAmount().toPlainString())
                        .replaceFirst(PLACEHOLDER.value(), StringUtils.isNotBlank(request.getRemark()) ? request.getRemark().trim() : NONE.value())
                        .replaceFirst(PLACEHOLDER.value(), seller.getName()),
                sysDate,
                null,
                null
        ));
    }

    @Override
    public void updatePlatformOrder(UpdatePlatformOrderRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        PlatformAssertSeller seller = new PlatformAssertSeller();
        seller.setId(request.getSellerId());
        List<PlatformAssertSeller> sellers = sellerRepository.query(seller);
        if (sellers.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_PLATFORM_SELLER.value());
        }
        PlatformAssertSellingOrder order = new PlatformAssertSellingOrder();
        order.setId(request.getId());
        List<PlatformAssertSellingOrder> orders = sellingOrderRepository.query(order);
        if (orders.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_PLATFORM_SELLING_ORDER.value());
        }
        seller = sellers.iterator().next();
        sellingOrderRepository.update(new PlatformAssertSellingOrder(
                request.getId(),
                request.getSellerId(),
                null,
                request.getAvailableTradeAmount(),
                request.getMinAmountLimit(),
                request.getMaxAmountLimit(),
                request.getRmbPrice(),
                StringUtils.isNotBlank(request.getRemark()) ? request.getRemark().trim() : null,
                seller.getCreateTime(),
                sysDate
        ));
    }

    @Override
    public QueryPlatformOrderResponse queryPlatformOrders(QueryPlatformOrderRequest request) {
        BigDecimal rmbExchangeRate = new BigDecimal(PLATFORM_CURRENCY_EXCHANGE_RATE.value());
        PlatformAssertSellingOrder param = new PlatformAssertSellingOrder();
        param.setId(request.getId());
        param.setSellerName(request.getName());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<PlatformAssertSellingOrder> page = (Page<PlatformAssertSellingOrder>) sellingOrderRepository.query(param);
        List<QueryPlatformOrderResponse.Item> items = new ArrayList<>();
        if (!page.isEmpty()) {
            items = page.getResult()
                    .stream()
                    .map(x -> new QueryPlatformOrderResponse.Item(
                            x.getId(),
                            x.getSellerId(),
                            x.getSellerName(),
                            x.getAvailableTradeAmount(),
                            PLATFORM_CURRENCY_SYMBOL.value(),
                            x.getMinAmountLimit(),
                            x.getMaxAmountLimit(),
                            x.getMinAmountLimit().multiply(rmbExchangeRate),
                            x.getMaxAmountLimit().multiply(rmbExchangeRate),
                            x.getRmbPrice(),
                            x.getRemark()
                    )).collect(Collectors.toList());
        }
        return new QueryPlatformOrderResponse(page.getTotal(), items);
    }
}
