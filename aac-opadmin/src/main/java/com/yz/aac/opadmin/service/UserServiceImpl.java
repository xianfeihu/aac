package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.Constants;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryUserDetailResponse;
import com.yz.aac.opadmin.model.response.QueryUserFreezingAssertResponse;
import com.yz.aac.opadmin.model.response.QueryUserResponse;
import com.yz.aac.opadmin.repository.*;
import com.yz.aac.opadmin.repository.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.AccountStatus.ENABLED;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.ErrorMessage.*;
import static com.yz.aac.opadmin.Constants.UserBehaviour.*;
import static com.yz.aac.opadmin.Constants.UserIncomeSource.*;
import static com.yz.aac.opadmin.model.response.QueryUserDetailResponse.UserAttribute;
import static com.yz.aac.opadmin.model.response.QueryUserDetailResponse.UserAttribute.*;
import static com.yz.aac.opadmin.model.response.QueryUserDetailResponse.UserAttribute.UserAssetInfo.*;
import static com.yz.aac.opadmin.model.response.QueryUserResponse.Item.MerchantCurrencyAssert;
import static com.yz.aac.opadmin.model.response.QueryUserResponse.Item.PlatformCurrencyIncomeSource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private UserAssertFreezeRepository userAssertFreezeRepository;

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private UserBehaviourStatisticsRepository userBehaviourRepository;

    @Autowired
    private MerchantAssertTradeRecordRepository merchantAssertTradeRecordRepository;

    @SuppressWarnings("ConstantConditions")
    @Override
    public QueryUserResponse queryUsers(QueryUserRequest request) throws Exception {
        //参数准备
        Long userId;
        BigDecimal currentLevelIncome = null, nextLevelIncome = null;
        try {
            userId = StringUtils.isBlank(request.getCode()) ? null : Long.parseLong(request.getCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        if (null != request.getLevelId()) {
            for (UserLevel x : userLevelRepository.query(new UserLevel())) {
                if (x.getId().longValue() == request.getLevelId()) {
                    currentLevelIncome = x.getMatchCondition();
                }
                if (null == currentLevelIncome) {
                    nextLevelIncome = x.getMatchCondition();
                }
            }
        }
        RichUser param = new RichUser();
        param.setIsMerchant(NO.value());
        param.setIsAdvertiser(NO.value());
        param.setCurrentLevelIncome(currentLevelIncome);
        param.setNextLevelIncome(nextLevelIncome);
        param.setCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
        param.setTradeCountKey(TRADE.value());
        param.setIncomeRechargeKey(RECHARGE.value());
        param.setIncomeSellKey(SELL.value());
        param.setIncomeTransferKey(TRANSFER.value());
        param.setIncomeMiningKey(MINING.value());
        param.setBeginRegTime(request.getBeginTime());
        param.setEndRegTime(request.getEndTime());
        param.setMinBalance(request.getMinBalance());
        param.setMaxBalance(request.getMaxBalance());
        param.setUserName(request.getName());
        param.setMobileNumber(request.getMobileNumber());
        param.setUserId(userId);
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        //查询
        Page<RichUser> page = (Page<RichUser>) userRepository.queryRichUsers(param);
        List<QueryUserResponse.Item> items = new ArrayList<>();
        if (!page.getResult().isEmpty()) {
            //查询其他资产
            Set<Long> userIds = page.getResult().stream().map(RichUser::getUserId).collect(Collectors.toSet());
            Map<Long, List<MerchantCurrencyAssert>> otherAsserts = queryMerchantAsserts(userIds);
            items = page.getResult()
                    .stream()
                    .map(x -> new QueryUserResponse.Item(
                            x.getUserId(),
                            USER_CODE_PREFIX.value() + x.getUserId(),
                            x.getStatus(),
                            x.getUserName(),
                            x.getWalletAddress(),
                            x.getBalance().subtract(x.getFreezingAmount()),
                            x.getFreezingAmount(),
                            otherAsserts.get(x.getUserId()),
                            x.getLevelName(),
                            x.getPowerPoint(),
                            x.getTradeCount(),
                            x.getIncreaseAlgorithmId(),
                            new PlatformCurrencyIncomeSource(x.getMiningIncome(), x.getRechargeIncome().add(x.getSellIncome()).add(x.getTransferIncome()))
                    )).collect(Collectors.toList());
        }
        return new QueryUserResponse(page.getTotal(), PLATFORM_CURRENCY_SYMBOL.value(), items);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public QueryUserFreezingAssertResponse queryUserFreezingAsserts(QueryUserFreezingAssertRequest request) {
        UserAssertFreeze param = new UserAssertFreeze();
        param.setUserId(request.getUserId());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<UserAssertFreeze> page = (Page<UserAssertFreeze>) userAssertFreezeRepository.querySum(param);
        List<QueryUserFreezingAssertResponse.Item> items = page.getResult()
                .stream()
                .map(x -> new QueryUserFreezingAssertResponse.Item(
                        x.getCurrencySymbol(),
                        x.getAmount(),
                        Arrays.stream(Constants.UserAssertFreezingReason.values()).filter(r -> r.code() == x.getReason()).findFirst().get().desc()
                )).collect(Collectors.toList());
        return new QueryUserFreezingAssertResponse(page.getTotal(), items);
    }

    @Override
    public void updateUserStatus(UpdateUserStatusRequest request) throws Exception {
        UserProperty up = new UserProperty();
        up.setUserId(request.getUserId());
        List<UserProperty> userProperties = userPropertyRepository.query(up);
        if (userProperties.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        up = userProperties.iterator().next();
        up.setStatus(request.getStatus());
        up.setStatusDescription(request.getStatus() == ENABLED.value() ? null : request.getDescription().trim());
        userPropertyRepository.update(up);
    }

    @Override
    public void updateUserIncreaseStrategy(UpdateUserIncreaseStrategyRequest request) throws Exception {
        UserProperty up = new UserProperty();
        up.setUserId(request.getUserId());
        List<UserProperty> userProperties = userPropertyRepository.query(up);
        if (userProperties.isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        IncreaseStrategy is = new IncreaseStrategy();
        is.setId(request.getStrategyId());
        if (algorithmRepository.query(is).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MISSING_INCREASE_STRATEGY.value());
        }
        up = userProperties.iterator().next();
        up.setIncreaseStrategyId(request.getStrategyId());
        userPropertyRepository.update(up);
    }

    @Override
    public QueryUserDetailResponse queryUserDetail(Long userId) throws Exception {
        QueryUserDetailResponse response = null;
        // 查询基础信息
        RichUser param = new RichUser();
        param.setUserId(userId);
        param.setCurrencySymbol(PLATFORM_CURRENCY_SYMBOL.value());
        param.setTradeCountKey(TRADE.value());
        param.setIncomeRechargeKey(RECHARGE.value());
        param.setIncomeSellKey(SELL.value());
        param.setIncomeTransferKey(TRANSFER.value());
        param.setIncomeMiningKey(MINING.value());
        List<RichUser> users = userRepository.queryRichUsers(param);
        if (!users.isEmpty()) {
            //基础信息
            RichUser u = users.iterator().next();
            UserBaseInfo baseInfo = new UserBaseInfo(USER_CODE_PREFIX.value() + u.getUserId(), u.getUserName(), u.getMobileNumber(), u.getIdNumber(), u.getLevelName(), u.getPowerPoint(), u.getGender(), u.getStatus(), u.getStatusDescription());
            //资产信息
            UserAssetInfo assetInfo = queryUserAssetInfo(userId, u);
            //行为信息
            UserBehaviourInfo behaviourInfo = queryUserBehaviour(userId);
            //交易信息
            UserTradeInfo tradeInfo = queryUserTradeInfo(userId, behaviourInfo);
            UserAttribute userAttribute = new UserAttribute(
                    baseInfo,
                    assetInfo,
                    behaviourInfo,
                    tradeInfo
            );
            response = new QueryUserDetailResponse(userAttribute, null, null);
        }
        return response;
    }

    private UserAssetInfo queryUserAssetInfo(Long userId, RichUser u) {
        //查询冻结资产
        List<QueryUserFreezingAssertResponse.Item> freezingAsserts = queryUserFreezingAsserts(
                new QueryUserFreezingAssertRequest(userId, new PagingRequest(1, 1000))
        ).getItems();
        //平台币冻结资产
        List<FreezingAsset> platformFreezingAsserts = freezingAsserts.stream()
                .filter(x -> PLATFORM_CURRENCY_SYMBOL.value().equals(x.getCurrencySymbol()))
                .map(x -> new FreezingAsset(x.getCurrencySymbol(), x.getFreezingAmount(), x.getReason()))
                .collect(Collectors.toList());
        //组装平台币资产
        PlatformCurrencyAsset platformAsset = new PlatformCurrencyAsset(
                u.getWalletAddress(),
                u.getBalance(),
                u.getMiningIncome(),
                u.getRechargeIncome().add(u.getSellIncome()).add(u.getTransferIncome()),
                platformFreezingAsserts
        );
        //商家币冻结资产
        List<FreezingAsset> merchantFreezingAsserts = freezingAsserts.stream()
                .filter(x -> !PLATFORM_CURRENCY_SYMBOL.value().equals(x.getCurrencySymbol()))
                .map(x -> new FreezingAsset(x.getCurrencySymbol(), x.getFreezingAmount(), x.getReason()))
                .collect(Collectors.toList());
        //商家币资产
        UserAssert ua = new UserAssert();
        ua.setUserIds(Arrays.stream(new Long[]{userId}).collect(Collectors.toSet()));
        List<MerchantCurrencyAssetItem> merchantAssets = userAssertRepository.queryForUserList(ua).stream()
                .filter(x -> !PLATFORM_CURRENCY_SYMBOL.value().equals(x.getCurrencySymbol()))
                .map(x -> new MerchantCurrencyAssetItem(x.getCurrencySymbol(), x.getWalletAddress(), x.getBalance(), x.getUnRestricted()))
                .collect(Collectors.toList());
        //组装商家币资产
        MerchantCurrencyAsset merchantAsset = new MerchantCurrencyAsset(merchantAssets, merchantFreezingAsserts);
        return new UserAssetInfo(platformAsset, merchantAsset);
    }

    private UserTradeInfo queryUserTradeInfo(Long userId, UserBehaviourInfo behaviourInfo) {
        List<UserTradeInfo.TradeRecord> tradeRecords = merchantAssertTradeRecordRepository.queryAllRecordsByUserId(
                userId,
                PLATFORM_CURRENCY_SYMBOL.value()
        ).stream().map(x -> new UserTradeInfo.TradeRecord(
                x.getTradeTime(),
                x.getTradeType(),
                x.getCurrencySymbol(),
                x.getTradeAmount(),
                x.getPartnerName())
        ).collect(Collectors.toList());
        return new UserTradeInfo(behaviourInfo.getTradeCount(), tradeRecords);
    }

    private UserBehaviourInfo queryUserBehaviour(Long userId) {
        Map<String, Integer> items = new HashMap<>();
        UserBehaviourStatistics param = new UserBehaviourStatistics();
        param.setUserId(userId);
        userBehaviourRepository.query(param).forEach(x -> items.put(x.getKey(), x.getValue()));
        return new UserBehaviourInfo(
                items.getOrDefault(TRADE.value(), 0),
                items.getOrDefault(SIGN_IN.value(), 0),
                items.getOrDefault(ANSWER.value(), 0),
                items.getOrDefault(ATTENTION_PUBLIC_NUMBER.value(), 0),
                items.getOrDefault(READ_ARTICLE.value(), 0),
                items.getOrDefault(SEND_RED_PACKET.value(), 0),
                items.getOrDefault(GRAB_RED_PACKET.value(), 0),
                items.getOrDefault(PLAY_GAME.value(), 0),
                items.getOrDefault(MINING_EVENT.value(), 0),
                items.getOrDefault(INVITE.value(), 0),
                items.getOrDefault(POST_ARTICLE.value(), 0),
                items.getOrDefault(CLICK_AD.value(), 0),
                items.getOrDefault(SUBMIT_AD_FORM.value(), 0)
        );
    }

    private Map<Long, List<MerchantCurrencyAssert>> queryMerchantAsserts(Set<Long> userIds) {
        Map<Long, List<MerchantCurrencyAssert>> result = new HashMap<>();
        UserAssert ua = new UserAssert();
        ua.setUserIds(userIds);
        List<UserAssert> asserts = userAssertRepository.queryForUserList(ua);
        asserts.forEach(x -> {
            if (!PLATFORM_CURRENCY_SYMBOL.value().equals(x.getCurrencySymbol())) {
                List<MerchantCurrencyAssert> merchantCurrencyAsserts = result.getOrDefault(x.getUserId(), new ArrayList<>());
                merchantCurrencyAsserts.add(new MerchantCurrencyAssert(x.getCurrencySymbol(), x.getBalance().subtract(x.getFreezingAmount()), x.getUnRestricted()));
                result.put(x.getUserId(), merchantCurrencyAsserts);
            }
        });
        return result;
    }
}
