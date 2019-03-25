package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.DateUtil;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.model.request.AuditIssuanceRequest;
import com.yz.aac.opadmin.model.request.QueryMerchantIssuanceRequest;
import com.yz.aac.opadmin.model.request.QueryMerchantRequest;
import com.yz.aac.opadmin.model.request.UnfreezeAssetRequest;
import com.yz.aac.opadmin.model.response.*;
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
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;
import static com.yz.aac.opadmin.Constants.FeatureModuleAction;
import static com.yz.aac.opadmin.Constants.FeatureModuleAction.MERCHANT_AUDIT_APPROVED;
import static com.yz.aac.opadmin.Constants.FeatureModuleAction.MERCHANT_AUDIT_REJECTED;
import static com.yz.aac.opadmin.Constants.MerchantAssertStatistics.*;
import static com.yz.aac.opadmin.Constants.MerchantAuditStatus;
import static com.yz.aac.opadmin.Constants.MerchantAuditStatus.*;
import static com.yz.aac.opadmin.Constants.MerchantDividendStatus.*;
import static com.yz.aac.opadmin.Constants.UserAssertFreezingReason.DEPOSIT;
import static com.yz.aac.opadmin.Constants.UserIncomeSource.*;
import static com.yz.aac.opadmin.model.response.QueryMerchantResponse.PlatformCurrencyIncomeSource;
import static com.yz.aac.opadmin.model.response.QueryUserDetailResponse.MerchantAttribute;
import static com.yz.aac.opadmin.model.response.QueryUserDetailResponse.MerchantAttribute.*;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;

    @Autowired
    private MerchantAssertIssuanceAuditRepository auditRepository;

    @Autowired
    private PlatformServiceChargeStrategyRepository chargeStrategyRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private OperatorActionLogRepository logRepository;

    @Autowired
    private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;

    @Autowired
    private UserAssertFreezeRepository userAssertFreezeRepository;

    @Autowired
    private MerchantAssertTradeRecordRepository merchantAssertTradeRecordRepository;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private MerchantDividendRecordRepository merchantDividendRecordRepository;

    @Override
    public List<QueryMerchantCurrencyResponse> queryCurrencies() {
        return merchantAssertIssuanceRepository.queryAllSymbols()
                .stream()
                .map(x -> new QueryMerchantCurrencyResponse(x.getId(), x.getCurrencySymbol(), x.getMerchantName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<QueryAuditStatusItemResponse> queryAuditStatus() {
        return Arrays.stream(MerchantAuditStatus.values()).map(x ->
                new QueryAuditStatusItemResponse(x.status(), x.description())
        ).collect(Collectors.toList());
    }

    @Override
    public QueryMerchantResponse queryMerchants(QueryMerchantRequest request) throws Exception {
        Long merchantId;
        try {
            merchantId = StringUtils.isBlank(request.getMerchantCode()) ? null : Long.parseLong(request.getMerchantCode().trim().replaceFirst(MERCHANT_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_MERCHANT_CODE.value());
        }
        MerchantAssertIssuance param = new MerchantAssertIssuance();
        param.setPlatformCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
        param.setCurrencySymbol(request.getCurrencySymbol());
        param.setBeginTime(request.getBeginTime());
        param.setEndTime(request.getEndTime());
        param.setStatus(request.getStatus());
        param.setMerchantName(request.getMerchantName());
        param.setMobileNumber(request.getMobileNumber());
        param.setMerchantId(merchantId);
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        //查询商户
        Page<MerchantAssertIssuance> page = (Page<MerchantAssertIssuance>) merchantRepository.queryForList(param);
        List<QueryMerchantResponse.Item> items = page.getResult().stream()
                .map(x -> {
                    BigDecimal sellingAmount = x.getTotal().multiply(BigDecimal.valueOf(x.getSellRate() / 100D));
                    BigDecimal miningAmount = x.getTotal().subtract(sellingAmount);
                    BigDecimal availableBalance = x.getBalance().subtract(x.getFreezingAmount());
                    Integer canUnfreeze = null == x.getUserId() ? null : (null == x.getIssuanceFreezingAmount() ? NO.value() : YES.value());
                    return new QueryMerchantResponse.Item(
                            x.getUserId(),
                            x.getMerchantId(),
                            MERCHANT_CODE_PREFIX.value() + x.getMerchantId(),
                            x.getStatus(),
                            x.getMerchantName(),
                            x.getCurrencySymbol(),
                            availableBalance,
                            x.getIssuingDate(),
                            x.getTotal(),
                            sellingAmount,
                            miningAmount,
                            x.getRmbPrice(),
                            x.getSellRest(),
                            x.getSellSold(),
                            x.getMiningMind(),
                            x.getTradeCount(),
                            x.getIncreaseStrategyId(),
                            canUnfreeze
                    );
                }).collect(Collectors.toList());
        //统计收入来源
        PlatformCurrencyIncomeSource incomeSource = null;
        if (request.getOutputStatistics()) {
            BigDecimal miningIncome = BigDecimal.valueOf(0), rechargeIncome = BigDecimal.valueOf(0), sellIncome = BigDecimal.valueOf(0), transferIncome = BigDecimal.valueOf(0);
            for (CurrencyKeyValuePair x : merchantRepository.statisticForList(param)) {
                if (RECHARGE.value().equals(x.getKey())) {
                    rechargeIncome = x.getValue();
                } else if (SELL.value().equals(x.getKey())) {
                    sellIncome = x.getValue();
                } else if (TRANSFER.value().equals(x.getKey())) {
                    transferIncome = x.getValue();
                } else if (MINING.value().equals(x.getKey())) {
                    miningIncome = x.getValue();
                }
            }
            incomeSource = new PlatformCurrencyIncomeSource(
                    miningIncome,
                    rechargeIncome.add(sellIncome).add(transferIncome)
            );
        }
        return new QueryMerchantResponse(page.getTotal(), PLATFORM_CURRENCY_SYMBOL.value(), incomeSource, items);
    }

    @Override
    public QueryMerchantIssuanceResponse queryMerchantIssuanceRequests(QueryMerchantIssuanceRequest request) throws Exception {
        //查询
        Long merchantId;
        try {
            merchantId = StringUtils.isBlank(request.getMerchantCode()) ? null : Long.parseLong(request.getMerchantCode().trim().replaceFirst(MERCHANT_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_MERCHANT_CODE.value());
        }
        MerchantAssertIssuance param = new MerchantAssertIssuance();
        param.setPlatformCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
        param.setCurrencySymbol(request.getCurrencySymbol());
        param.setStatus(request.getStatus());
        param.setMerchantName(request.getMerchantName());
        param.setMobileNumber(request.getMobileNumber());
        param.setMerchantId(merchantId);
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<MerchantAssertIssuance> page = (Page<MerchantAssertIssuance>) merchantAssertIssuanceRepository.queryIssuanceRequests(param);
        List<QueryMerchantIssuanceResponse.Item> items = page.getResult().stream()
                .map(x -> {
                    BigDecimal sellingAmount = x.getTotal().multiply(BigDecimal.valueOf(x.getSellRate() / 100D));
                    BigDecimal miningAmount = x.getTotal().subtract(sellingAmount);
                    BigDecimal fixedIncomeAmount = (null == x.getFixedIncomeRate() ? BigDecimal.valueOf(0) : x.getTotal().multiply(BigDecimal.valueOf(x.getFixedIncomeRate() / 100D)));
                    BigDecimal stoDividendAmount = (null == x.getStoDividendRate() ? BigDecimal.valueOf(0) : x.getTotal().multiply(BigDecimal.valueOf(x.getStoDividendRate() / 100D)));
                    BigDecimal availableBalance = x.getBalance().subtract(x.getFreezingAmount());
                    return new QueryMerchantIssuanceResponse.Item(
                            MERCHANT_CODE_PREFIX.value() + x.getMerchantId(),
                            x.getStatus(),
                            x.getCurrencySymbol(),
                            x.getMerchantName(),
                            availableBalance,
                            x.getRequestTime(),
                            x.getTotal(),
                            sellingAmount,
                            miningAmount,
                            fixedIncomeAmount,
                            stoDividendAmount,
                            x.getIncomePeriod(),
                            x.getRestrictionPeriod(),
                            x.getWhitePaperUrl(),
                            x.getAuditComment(),
                            x.getRequestId()
                    );
                }).collect(Collectors.toList());
        return new QueryMerchantIssuanceResponse(page.getTotal(), PLATFORM_CURRENCY_SYMBOL.value(), items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditIssuance(AuditIssuanceRequest request) throws Exception {
        long sysDate = System.currentTimeMillis();
        //验证请求
        MerchantAssertIssuanceAudit audit = new MerchantAssertIssuanceAudit();
        audit.setId(request.getRequestId());
        List<MerchantAssertIssuanceAudit> audits = auditRepository.query(audit);
        if (audits.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        audit = audits.iterator().next();
        //验证审核流程顺序
        if (audit.getStatus() == ISSUED.status() && (request.getStatus() != WAITING_DEPOSIT.status() && request.getStatus() != ISSUED_REJECTED.status())) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_STATUS.value());
        }
        if (audit.getStatus() == WAITING_DEPOSIT_AUDIT.status() && (request.getStatus() != DEPOSIT_REJECTED.status() && request.getStatus() != DEPOSIT_APPROVED.status())) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_STATUS.value());
        }
        //商户资格审核通过时，获取服务费策略信息
        PlatformServiceChargeStrategy charge = null;
        if (request.getStatus() == WAITING_DEPOSIT.status()) {
            charge = new PlatformServiceChargeStrategy();
            charge.setId(request.getServiceChargeId());
            List<PlatformServiceChargeStrategy> charges = chargeStrategyRepository.query(charge);
            if (charges.isEmpty()) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_SERVICE_CHARGE.value());
            }
            charge = charges.iterator().next();
        }
        //更新请求信息
        audit.setStatus(request.getStatus());
        audit.setAuditComment(StringUtils.isBlank(request.getRemark()) ? null : request.getRemark().trim());
        audit.setAuditTime(sysDate);
        auditRepository.update(audit);
        //商户资格审核通过时，更新其发币信息，关联服务费策略ID
        MerchantAssertIssuance issuance = new MerchantAssertIssuance();
        issuance.setId(audit.getIssuanceId());
        List<MerchantAssertIssuance> issuanceList = merchantAssertIssuanceRepository.query(issuance);
        if (issuanceList.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_MERCHANT_ISSUANCE.value());
        }
        issuance = issuanceList.iterator().next();
        if (request.getStatus() == WAITING_DEPOSIT.status()) {
            issuance.setIssuingDate(sysDate);
            issuance.setServiceChargeId(request.getServiceChargeId());
            merchantAssertIssuanceRepository.update(issuance);
        }
        //商户资格审核通过时，初始化商户资产统计数据
        if (request.getStatus() == WAITING_DEPOSIT.status()) {
            BigDecimal sellRest = issuance.getTotal().multiply(BigDecimal.valueOf(issuance.getSellRate() / 100D));
            BigDecimal miningRest = issuance.getTotal().subtract(sellRest);
            CurrencyKeyValuePair[] pairs = new CurrencyKeyValuePair[]{
                    new CurrencyKeyValuePair(UNRESTRICTED.value(), BigDecimal.valueOf(issuance.getRestrictionPeriod() == 0 ? YES.value() : NO.value())),
                    new CurrencyKeyValuePair(TRADED.value(), BigDecimal.valueOf(0)),
                    new CurrencyKeyValuePair(MINING_MIND.value(), BigDecimal.valueOf(0)),
                    new CurrencyKeyValuePair(MINING_REST.value(), miningRest),
                    new CurrencyKeyValuePair(SELL_SOLD.value(), BigDecimal.valueOf(0)),
                    new CurrencyKeyValuePair(SELL_REST.value(), sellRest)
            };
            for (CurrencyKeyValuePair x : pairs) {
                merchantAssertStatisticsRepository.store(new MerchantAssertStatistics(
                        null, issuance.getMerchantId(), issuance.getCurrencySymbol(), x.getKey(), x.getValue()
                ));
            }
        }
        //商户资格审核通过时，更新用户角色
        if (request.getStatus() == WAITING_DEPOSIT.status()) {
            Merchant merchant = new Merchant();
            merchant.setId(issuance.getMerchantId());
            List<Merchant> merchants = merchantRepository.query(merchant);
            if (merchants.isEmpty()) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_MERCHANT.value());
            }
            merchant = merchants.iterator().next();
            User user = new User();
            user.setMobileNumber(merchant.getMobileNumber());
            List<User> users = userRepository.query(user);
            if (!users.isEmpty()) {
                user = users.iterator().next();
                UserRole role = new UserRole();
                role.setUserIds(new HashSet<>(Collections.singletonList(user.getId())));
                List<UserRole> roles = userRoleRepository.query(role);
                if (roles.isEmpty()) {
                    role.setUserId(user.getId());
                    role.setIsMerchant(YES.value());
                    role.setIsAdvertiser(NO.value());
                    userRoleRepository.store(role);
                } else {
                    role = roles.iterator().next();
                    role.setIsMerchant(YES.value());
                    userRoleRepository.update(role);
                }
            }
        }
        //商户资格审核通过/被拒时，写入操作员访问日志
        if (request.getStatus() == WAITING_DEPOSIT.status() || request.getStatus() == ISSUED_REJECTED.status()) {
            FeatureModuleAction action = request.getStatus() == WAITING_DEPOSIT.status() ? MERCHANT_AUDIT_APPROVED : MERCHANT_AUDIT_REJECTED;
            assert charge != null;
            String additionalInfo = request.getStatus() == WAITING_DEPOSIT.status() ? charge.getName() : request.getRemark().trim();
            logRepository.store(new OperatorActionLog(
                    null,
                    request.getLoginId(),
                    action.moduleId(),
                    action.actionId(),
                    action.additionalInfo().replaceFirst(PLACEHOLDER.value(), additionalInfo),
                    sysDate,
                    null,
                    null
            ));
        }
        //商户押金审核通过时，写入其资产信息
        if (request.getStatus() == DEPOSIT_APPROVED.status()) {
            Merchant merchant = new Merchant();
            merchant.setId(issuance.getMerchantId());
            List<Merchant> merchants = merchantRepository.query(merchant);
            if (merchants.isEmpty()) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_MERCHANT.value());
            }
            merchant = merchants.iterator().next();
            User user = new User();
            user.setMobileNumber(merchant.getMobileNumber());
            List<User> users = userRepository.query(user);
            if (users.isEmpty()) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_MERCHANT_USER.value());
            }
            user = users.iterator().next();
            userAssertRepository.store(new UserAssert(
                    null,
                    user.getId(),
                    issuance.getCurrencySymbol(),
                    issuance.getTotal(),
                    issuance.getTotal(),
                    RandomUtil.genUUID(),
                    null,
                    null,
                    null
            ));

            // 初始化商户分红信息
            this.merchantDividendRecordRepository.addMerchantDividendRecord(new MerchantDividendRecord(null, issuance.getMerchantId(),
                    DateUtil.getSomeDay(DateUtil.startOfTodDay(new Date(issuance.getIssuingDate())), issuance.getIncomePeriod()).getTime()
                    , null, null, null, WAIT_DIVIDEND.code()));
        }
    }

    @Override
    public void unfreezeAsset(UnfreezeAssetRequest request) throws Exception {
        UserAssertFreeze param = new UserAssertFreeze();
        param.setUserId(request.getUserId());
        param.setReason(request.getFreezingType());
        if (request.getFreezingType() == DEPOSIT.code()) {
            param.setCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
        }
        List<UserAssertFreeze> assets = userAssertFreezeRepository.query(param);
        if (assets.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_FREEZING_ASSET.value());
        }
        UserAssertFreeze asset = assets.iterator().next();
        userAssertFreezeRepository.delete(asset.getId());
    }

    @Override
    public QueryUserDetailResponse queryMerchantDetail(Long merchantId) throws Exception {
        QueryUserDetailResponse response = null;
        MerchantAssertIssuance param = new MerchantAssertIssuance();
        param.setMerchantId(merchantId);
        MerchantAssertIssuance merchant = merchantRepository.queryDetail(param);
        if (null != merchant) {
            //基础信息
            MerchantBaseInfo baseInfo = new MerchantBaseInfo(
                    MERCHANT_CODE_PREFIX.value() + merchant.getMerchantId(),
                    merchant.getMerchantName(),
                    merchant.getName(),
                    merchant.getMobileNumber(),
                    merchant.getIdNumber(),
                    merchant.getStatus(),
                    merchant.getStatusDescription()
            );
            //发币信息
            MerchantIssuanceInfo issuanceInfo = new MerchantIssuanceInfo(
                    merchant.getCurrencySymbol(),
                    merchant.getTotal(),
                    merchant.getMiningRate(),
                    merchant.getFixedIncomeRate(),
                    merchant.getStoDividendRate(),
                    merchant.getOtherMode(),
                    merchant.getIncomePeriod(),
                    merchant.getRestrictionPeriod(),
                    merchant.getIssuanceDeposit(),
                    PLATFORM_CURRENCY_SYMBOL.value(),
                    merchant.getWhitePaperUrl(),
                    merchant.getMiningRest(),
                    merchant.getMiningMind()
            );
            //出售信息
            List<MerchantAssertTradeRecord> tempRecords = new ArrayList<>(0);
            List<MerchantSellInfo.SoldRecord> records = new ArrayList<>();
            BigDecimal sellAmount = BigDecimal.valueOf(0);
            if (null != merchant.getUserId()) {
                MerchantAssertTradeRecord r = new MerchantAssertTradeRecord();
                r.setCurrencySymbol(issuanceInfo.getCurrencySymbol());
                r.setInitiatorId(merchant.getUserId());
                tempRecords = merchantAssertTradeRecordRepository.querySellRecords(r);
            }
            for (MerchantAssertTradeRecord x : tempRecords) {
                sellAmount = sellAmount.add(x.getTradeAmount());
                records.add(new MerchantSellInfo.SoldRecord(
                        x.getTradeTime(), USER_CODE_PREFIX.value() + x.getPartnerId(), x.getPartnerName(), x.getTradeAmount())
                );
            }
            MerchantSellInfo sellInfo = new MerchantSellInfo(records.size(), sellAmount, records);
            if (null != merchant.getUserId()) {
                response = userService.queryUserDetail(merchant.getUserId());
            }
            if (null == response) {
                response = new QueryUserDetailResponse();
            }
            response.setMerchantAttribute(new MerchantAttribute(baseInfo, issuanceInfo, sellInfo));
        }
        return response;
    }
}
