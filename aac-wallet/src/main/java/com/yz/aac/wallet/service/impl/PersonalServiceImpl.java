package com.yz.aac.wallet.service.impl;

import com.yz.aac.common.Constants;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.DateUtil;
import com.yz.aac.wallet.aspect.PersonalAspect;
import com.yz.aac.wallet.model.request.ExchangeRequest;
import com.yz.aac.wallet.model.request.MobileEntityRequest;
import com.yz.aac.wallet.model.request.TransferToWalletAddrRequest;
import com.yz.aac.wallet.model.response.*;
import com.yz.aac.wallet.repository.*;
import com.yz.aac.wallet.repository.domain.*;
import com.yz.aac.wallet.service.MerchantAssertStatisticsService;
import com.yz.aac.wallet.service.PersonalService;
import com.yz.aac.wallet.service.SmsService;
import com.yz.aac.wallet.service.UserBehaviourStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE;
import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.wallet.Constants.*;
import static com.yz.aac.wallet.Constants.IssuanceAuditStatus.DEPOSIT_YES;
import static com.yz.aac.wallet.Constants.PlatformAssertTradeExamineType.FINISH;
import static com.yz.aac.wallet.Constants.PlatformAssertTradeType.DIVIDEND;
import static com.yz.aac.wallet.Constants.PlatformAssertTradeType.TRANSFER;

@Service
@Slf4j
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantAssertTradeRecordRepository merchantAssertTradeRecordRepository;

    @Autowired
    private UserAssetsRepository userAssertRepository;

    @Autowired
    private UserAssertFreezeRepository userAssertFreezeRepository;

    @Autowired
    private UserAddressBookRepository userAddressBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;

    @Autowired
    private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;

    @Autowired
    private UserBehaviourStatisticsService userBehaviourStatisticsServiceImpl;

    @Autowired
    private MerchantAssertStatisticsService merchantAssertStatisticsServiceImpl;

    @Autowired
    private SmsService smsServiceImpl;

    @Autowired
    private ExchangeRecordRepository exchangeRecordRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeItemRepository exchangeItemRepository;

    @Autowired
    private PersonalAspect personalAspect;

    @Override
    public PersonalIndexMsgResponse indexMsg(Long userId) throws Exception {
        PersonalIndexMsgResponse indexMsg = this.userRepository.getIndexMsg(PLATFORM_CURRENCY_SYMBOL.value() ,UserBehaviourStatisticsKey.TRADE.name(), userId);
        // 获取级别
        indexMsg.setLevel(this.userRepository.getUserLevel(PLATFORM_CURRENCY_SYMBOL.value(), userId));
        // 统计次数
        indexMsg.setTransferCount(this.merchantAssertTradeRecordRepository.getTradeCount(userId, MerchantTradeType.TRANSFER.code()));
        indexMsg.setReceiptCount(this.merchantAssertTradeRecordRepository.getReceiptCount(userId, MerchantTradeType.BUY.code(), MerchantTradeType.SELL.code(), MerchantTradeType.TRANSFER.code()));
        return indexMsg;
    }

    @Override
    public PersonalAssetsMsgResponse getPersonalBaseMsg(Long userId, String currencySymbol) throws Exception {
        // 获取用户资产相关信息
        UserAssets userAssert = this.userAssertRepository.queryUserAssert(userId, currencySymbol);
        if (userAssert==null){
            return new PersonalAssetsMsgResponse(BigDecimal.ZERO,null,BigDecimal.ZERO,null);
        }
        PersonalAssetsMsgResponse assetsMsgResponse = new PersonalAssetsMsgResponse(userAssert.getBalance(), userAssert.getWalletAddress(), null,null);
        // 换算币种
        if (PLATFORM_CURRENCY_SYMBOL.value().equals(currencySymbol)){
            assetsMsgResponse.setAboutOtherCoin(new BigDecimal(PLATFORM_CURRENCY_EXCHANGE_RATE.value()).multiply(userAssert.getBalance()).setScale(2,BigDecimal.ROUND_HALF_UP));
            // 转账记录
            assetsMsgResponse.setPersonalTransferRecord(this.platformAssertTradeRecordRepository.getTransactionRecordHistory(userId, currencySymbol, TRANSFER.code(), DIVIDEND.code(),true));
        } else {
            // 获取当前商户与AAB的汇率
            if (currencySymbol.equals(PLATFORM_CURRENCY_SYMBOL.value())) {
                assetsMsgResponse.setAboutOtherCoin(userAssert.getBalance().multiply(BigDecimal.valueOf(Long.parseLong(PLATFORM_CURRENCY_EXCHANGE_RATE.value()))).setScale(2,BigDecimal.ROUND_HALF_UP));
            } else {
                BigDecimal platformPrice = this.merchantAssertTradeRecordRepository.getMchLastPlatformPrice(currencySymbol);
                assetsMsgResponse.setAboutOtherCoin(platformPrice == null ? BigDecimal.ZERO : userAssert.getBalance().multiply(platformPrice).setScale(2,BigDecimal.ROUND_HALF_UP));
            }
            // 转账记录
            assetsMsgResponse.setPersonalTransferRecord(this.merchantAssertTradeRecordRepository.getTransactionRecordHistory(userId, currencySymbol, MerchantTradeType.TRANSFER.code()));
        }
        return assetsMsgResponse;
    }

    @Override
    public List<TradeRecordResponse> getTransactionRecord(Long userId, String currencySymbol) throws Exception {
        if (PLATFORM_CURRENCY_SYMBOL.value().equals(currencySymbol)) {
            return this.merchantAssertTradeRecordRepository.getAllTradeRecordHistory(userId, MerchantTradeType.getTransactionRecord(), PlatformAssertTradeType.getTransactionRecord());
        }
        return this.merchantAssertTradeRecordRepository.getTradeRecordHistory(userId, currencySymbol, MerchantTradeType.BUY.code()+","+MerchantTradeType.SELL.code());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferToWalletAddr(TransferToWalletAddrRequest transferMsg) throws Exception {
        if(this.userAssertRepository.checkForWalletAddress(transferMsg.getSendAddr())==0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"请实名认证后继续操作");
        }
        if(this.userAssertRepository.checkForWalletAddress(transferMsg.getReceiveAddr())==0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"对方未实名认证，转账失败！");
        }

        // 校验支付密码
        User user= this.userRepository.getUserById(transferMsg.getUserId());
        if (!user.getPaymentPassword().equals(transferMsg.getPayPass().toString()))
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"支付密码错误");
        // 开始转账
        if (this.userAssertRepository.subtractUserEffectiveAssets(transferMsg.getUserId(), transferMsg.getCoinType(), transferMsg.getAmount())!=1)
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"您的账户余额不足");
        if (this.userAssertRepository.addAssets(transferMsg)!=1)
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"转账失败");
        // 收录转账信息
        MerchantAssertTradeRecord tradeRecord = new MerchantAssertTradeRecord(transferMsg.getUserId(),transferMsg.getCoinType(),transferMsg.getAmount());
        User initiatorUser = this.userRepository.getUserByWalletAddress(transferMsg.getSendAddr());
        User partnerUser = this.userRepository.getUserByWalletAddress(transferMsg.getReceiveAddr());
        tradeRecord.setInitiatorId(initiatorUser.getId());
        tradeRecord.setInitiatorName(initiatorUser.getName());
        tradeRecord.setTradeType(Long.valueOf(MerchantTradeType.TRANSFER.code()));

        BigDecimal platBalance = this.userAssertRepository.getUserAssetsByCurrencySymbol(transferMsg.getUserId(), PLATFORM_CURRENCY_SYMBOL.value());
        BigDecimal platValidBalance = this.userAssertRepository.getUserAvailableFundsByCurrencySymbol(transferMsg.getUserId(), PLATFORM_CURRENCY_SYMBOL.value());
        BigDecimal merchantBalance = this.userAssertRepository.getUserAssetsByCurrencySymbol(transferMsg.getUserId(), transferMsg.getCoinType());
        BigDecimal merchantValidBalance = this.userAssertRepository.getUserAvailableFundsByCurrencySymbol(transferMsg.getUserId(), transferMsg.getCoinType());

        if (transferMsg.getCoinType().equals(PLATFORM_CURRENCY_SYMBOL.value())) {
            tradeRecord.setPlatformPrice(BigDecimal.valueOf(Long.parseLong(PLATFORM_CURRENCY_EXCHANGE_RATE.value())));
            // 记录到平台转账记录
            this.platformAssertTradeRecordRepository.addUserTransferRecordInfo(new PlatformAssertTradeRecord(initiatorUser.getId(),
                    initiatorUser.getName(),
                    TRANSFER.code(),
                    System.currentTimeMillis(),
                    BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_EXCHANGE_RATE.value())),
                    transferMsg.getAmount(),
                    transferMsg.getSendAddr(),
                    platBalance,
                    platValidBalance,
                    partnerUser.getId(),
                    partnerUser.getName(),
                    null,
                    FINISH.code(),
                    null,null,null,null));

        } else {
            tradeRecord.setPlatformPrice(this.merchantAssertTradeRecordRepository.getMchLastPlatformPrice(transferMsg.getCoinType()));
            // 商户资产统计
            this.merchantAssertStatisticsServiceImpl.addBehaviourStatistics(transferMsg.getCoinType(), MerchantAssertStatisticsKey.TRADED.name() ,1);
            tradeRecord.setBalance(platBalance);
            tradeRecord.setValidBalance(platValidBalance);
            tradeRecord.setMerchantBalance(merchantBalance);
            tradeRecord.setMerchantValidBalance(merchantValidBalance);
            tradeRecord.setPartnerId(partnerUser.getId());
            tradeRecord.setPartnerName(partnerUser.getName());
            tradeRecord.setTradeResult(transferMsg.getCoinType() + "-" + transferMsg.getAmount());
            tradeRecord.setTradeTime(System.currentTimeMillis());

            if (this.merchantAssertTradeRecordRepository.addTradeRecord(tradeRecord)!=1)
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"转账失败");
        }


        // 用户转账行为统计
        this.userBehaviourStatisticsServiceImpl.addBehaviourStatistics(initiatorUser.getId(), UserBehaviourStatisticsKey.TRADE.name(), 1);
        this.userBehaviourStatisticsServiceImpl.addBehaviourStatistics(partnerUser.getId(), UserBehaviourStatisticsKey.TRADE.name(), 1);

        log.info("转账信息： 用户ID: "+initiatorUser.getId()+"("+ initiatorUser.getName() +")转账到 用户ID："+partnerUser.getId()+ "("+ partnerUser.getName() +") -> "+ transferMsg.getCoinType() +" --" + transferMsg.getAmount() + "个");
    }


    @Override
    public String getNameByWalletAddress(String walletAddress) throws Exception {
        String name = this.userRepository.getNameByWalletAddress(walletAddress);
        if (name == null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该地址的相关信息不存在");
        }
        return name;
    }

    @Override
    public BigDecimal AabBalance(Long userId) throws Exception {
        return this.userAssertRepository.getUserAssetsByCurrencySymbol(userId, PLATFORM_CURRENCY_SYMBOL.value());
    }

    @Override
    public OtherCoinMsgResponse getOtherAssets(Long userId) throws Exception {
        BigDecimal userOtherAssetsSum = this.userAssertRepository.getUserOtherAssetsSum(userId, PLATFORM_CURRENCY_SYMBOL.value());
        return new OtherCoinMsgResponse(
                userOtherAssetsSum==null ? BigDecimal.ZERO : userOtherAssetsSum ,
                this.userAssertRepository.getUserOtherAssets(userId, PLATFORM_CURRENCY_SYMBOL.value()));
    }

    @Override
    public String getMchQrCode(String currencySymbol) throws Exception {
        return this.merchantRepository.getMchQrCode(currencySymbol);
    }

    @Override
    public List<CoinDetailsMsgResponse> getFreezeAssets(Long userId) throws Exception {
        List<CoinDetailsMsgResponse> ls = this.userAssertFreezeRepository.getFreezeRepositoryByUserId(userId, DateUtil.startOfTodDay(new Date()).getTime());
        if (ls==null || ls.isEmpty()) return ls;
        List<CoinDetailsMsgResponse> list = new ArrayList();
        ls.stream().forEach(x -> {
            List<CoinDetailsMsgResponse> collect = list.stream().filter(y -> y.getCurrencySymbol().equals(x.getCurrencySymbol())).collect(Collectors.toList());
            if (collect == null || collect.isEmpty()) {
                x.setFreezeReason(x.getReason(x.getFreezeReasonBuilder()==null ? new StringBuilder() : x.getFreezeReasonBuilder()));
                list.add(x);
            } else {
                CoinDetailsMsgResponse response = collect.get(0);
                response.setAmount(x.getAmount().add(response.getAmount()));
                response.setFreezeReason(x.getReason(response.getFreezeReasonBuilder()));
            }
        });
        return list;
    }

    @Override
    public List<UserAddressBook> getWalletAddressForCoinType(Long userId, String currencySymbol) throws Exception {
        return this.userAddressBookRepository.getWalletAddress(userId, currencySymbol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addWalletAddress(UserAddressBook walletAddress) throws Exception {
        // 判断地址格式
        if( 1 > this.userAssertRepository.checkForWalletAddress(walletAddress.getWalletAddress()) ){
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该地址不存在");
        }
        this.userAddressBookRepository.addWalletAddress(walletAddress);
        return walletAddress.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyWalletAddress(UserAddressBook walletAddress) throws Exception  {
        // 判断地址格式
        if( 1 > this.userAssertRepository.checkForWalletAddress(walletAddress.getWalletAddress()) ){
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(),"该地址不存在");
        }
        this.userAddressBookRepository.modifyWalletAddress(walletAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delWalletAddress(Integer id, Long userId) throws Exception {
        this.userAddressBookRepository.delWalletAddressById(id,userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(MobileEntityRequest mobileEntityRequest) throws Exception {
        // 校验验证码数据
        SmsResponse verifycode = this.smsServiceImpl.verifycode(mobileEntityRequest.getMobile(), mobileEntityRequest.getCode(), SmsCodeType.UP_PAS.code());
        if (YunXinErrorCodeEnum.CODE_OK.errorCode() != verifycode.getCode()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "验证码错误");
        }

        // 修改密码
        if (this.userRepository.updatePassWord(mobileEntityRequest)!=1) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "密码修改失败");
        }
        // 日志记录

    }

    @Override
    public List<String> getAllCurrencySymbol() throws Exception {
        List<String> list = this.merchantAssertIssuanceRepository.getAllCurrencySymbol(DEPOSIT_YES.code());
        list.add(0, PLATFORM_CURRENCY_SYMBOL.value());
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String exchange(ExchangeRequest exchangeRequest, Long userId) throws Exception {

        ExchangeRecord exchangeRecord = personalAspect.calculationRechargeAmount(exchangeRequest);

        exchangeRecordRepository.saveExchangeRecord(
                new ExchangeRecord(null, userId, exchangeRequest.getExchangeId(), exchangeRequest.getChargingNumber()
                        , exchangeRecord.getRmbAmount(), exchangeRecord.getPlatformAmount(), System.currentTimeMillis(), ExchangeRecordStatusEnum.RECHARGE_IN.code()));

        return "操作成功！";
    }

    @Override
    public Set<ActivityListResponse> activityList(Long userId) throws Exception {

        Set<ActivityListResponse> activitySet = new HashSet<>();
        List<Exchange> exchangeList = exchangeRepository.getExchangeByActivated(ExchangeActivatedStatusEnum.OPENING.code());

        if (null != exchangeList && exchangeList.size() > 0) {
            for (Exchange exchange : exchangeList) {
                //排除当前用户兑换次数上限服务
                List<ExchangeRecord> recordList = exchangeRecordRepository.getExchangeRecord(exchange.getId(), userId);
                if (((null == recordList || recordList.size() ==0) ? 0 : recordList.size()) < exchange.getLimitInMonth()) {
                    activitySet.add(new ActivityListResponse(exchange.getCategory(),
                            (exchange.getCategory() == ExchangeMaxCategoryEnum.TELEPHONE.code()) ? "充话费" : "充油卡"
                    ));
                }
            }
        }

       return activitySet;
    }

    @Override
    public ExchangeServiceInfoResponse serviceInfo(Integer category, Long userId) throws Exception {

        //基本信息
        User user = userRepository.getUserById(userId);

        ExchangeServiceInfoResponse esir = new ExchangeServiceInfoResponse(category, user.getMobileNumber(), BigDecimal.valueOf(Integer.valueOf(Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE.value())));
        List<Exchange> exchangeList = exchangeRepository.getExchangeBycategory(category);
        if (null != exchangeList && exchangeList.size() > 0) {
            List<ExchangeActivityInfoResponse> eairList = new ArrayList<>();
            for (Exchange exchange : exchangeList) {
                List<ExchangeRecord> recordList = exchangeRecordRepository.getExchangeRecord(exchange.getId(), userId);
                ExchangeActivityInfoResponse eair = new ExchangeActivityInfoResponse(
                        exchange.getId(), exchange.getSubCategory(),
                        exchange.getLimitInMonth() - (null == recordList ? 0 : recordList.size()),
                        exchange.getCustomized()
                );

                if (eair.getExchangeCount() <= 0) {
                    continue;
                }

                List<ExchangeItem> itemList = exchangeItemRepository.getExchangeItemByExchangeId(exchange.getId());
                if (null != itemList && itemList.size() > 0) {
                    eair.setItemList(
                            itemList.stream().map(e -> new ExchangeActivityItemResponse(e.getId(), e.getRmbAmount(), e.getPlatformAmount())).collect(Collectors.toList())
                    );
                }

                eairList.add(eair);
            }
            esir.setExchangeActivityInfoResponses(eairList);
        }

        return esir;
    }


}
