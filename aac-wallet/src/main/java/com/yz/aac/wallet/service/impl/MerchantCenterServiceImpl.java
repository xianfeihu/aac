package com.yz.aac.wallet.service.impl;

import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.wallet.Constants;
import com.yz.aac.wallet.Constants.SmsCodeType;
import com.yz.aac.wallet.model.request.IdCardMesRequest;
import com.yz.aac.wallet.model.request.MerchantAssertIssuanceMessageRequest;
import com.yz.aac.wallet.model.request.MerchantRegisterBaseMessageRequest;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.model.response.MerchantAssetDetailsResponse;
import com.yz.aac.wallet.model.response.MerchantIssueCurrencyResponse;
import com.yz.aac.wallet.model.response.SmsResponse;
import com.yz.aac.wallet.repository.*;
import com.yz.aac.wallet.repository.domain.Merchant;
import com.yz.aac.wallet.repository.domain.MerchantAssertIssuance;
import com.yz.aac.wallet.repository.domain.MerchantAssertIssuanceAudit;
import com.yz.aac.wallet.repository.domain.User;
import com.yz.aac.wallet.service.AccountService;
import com.yz.aac.wallet.service.MerchantCenterService;
import com.yz.aac.wallet.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.wallet.Constants.IssuanceAuditStatus.DEPOSIT_YES;
import static com.yz.aac.wallet.Constants.IssuanceAuditStatus.QUAL_NO;
import static com.yz.aac.wallet.Constants.MerchantAssertStatisticsKey.*;
import static com.yz.aac.wallet.Constants.Misc.APP_DOWNLOAD_URI;

@Service
@Slf4j
public class MerchantCenterServiceImpl implements MerchantCenterService {

    @Autowired
    private AccountService accountServiceImpl;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private MerchantAssertIssuanceRepository issuanceRepository;

    @Autowired
    private MerchantAssertIssuanceAuditRepository assertIssuanceAuditRepository;

    @Autowired
    private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;

    @Autowired
    private SmsService smsServiceImpl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long checkMerchantByMobile(String mobile) throws Exception {
        Merchant merchant = null;
        // 获取已注册的商户信息
        merchant = this.merchantRepository.getMerchantByMobile(Long.parseLong(mobile));
        if ( merchant==null ) {
            // 获取已注册的用户信息
            User user = this.userRepository.getUserByMobile(Long.parseLong(mobile));
            if (user == null || user.getName()==null || user.getIdNumber()==null) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "该手机号未被注册使用过！");
            }

            // 注册商户
            merchant = new Merchant(null, user.getName(), null, null, user.getGender(), user.getIdNumber(), Long.parseLong(mobile), System.currentTimeMillis());
            this.merchantRepository.addMerchant(merchant);
        }
        return merchant.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long checkMerchantBaseMessage(MerchantRegisterBaseMessageRequest registerRequest) throws Exception {

        // 验证码校验
        SmsResponse verifycode = this.smsServiceImpl.verifycode(registerRequest.getMobileNumber().toString(), registerRequest.getCode(), SmsCodeType.CERTIFICATION.code());
        if (com.yz.aac.wallet.Constants.YunXinErrorCodeEnum.CODE_OK.errorCode() != verifycode.getCode()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "验证码错误");
        }

        Merchant merchant = this.merchantRepository.getMerchantByMobileOrIdNumber(registerRequest.getMobileNumber(), registerRequest.getIdNumber());
        // 更新商户信息
        if (registerRequest.getMerchantId()!=null) {
            if (merchant==null || !merchant.getId().equals(registerRequest.getMerchantId())) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "商户ID有误");
            }
            if (this.merchantRepository.updateMerchantById(registerRequest.getMerchantName(), registerRequest.getMerchantVisitUrl(), registerRequest.getMerchantId())!=1) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "商户注册失败");
            }
            return merchant.getId();
        }


        // 手机号校验
        if (merchant != null) {
            if (merchant.getMobileNumber().equals(registerRequest.getMobileNumber())) {
                if (!merchant.getIdNumber().equals(registerRequest.getIdNumber())
                        || !merchant.getName().equals(registerRequest.getName())) {
                    throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "该手机号已被用户实名注册");
                }
                // 判断是否需要更新信息
                if (!merchant.getMerchantName().equals(registerRequest.getMerchantName()))  {
                    this.merchantRepository.updateMerchantById(registerRequest.getMerchantName(), null, merchant.getId());
                }
                return merchant.getId();
            }

            // 身份证校验
            if (merchant.getIdNumber().equals(registerRequest.getIdNumber())) {
                throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "该身份已被实名注册");
            }
        }

        // 获取已注册的用户信息
        Long userId = null;
        User user = this.userRepository.getUserByMobile(registerRequest.getMobileNumber());
        if (user != null && user.getName()!=null && user.getIdNumber()!=null) {
            userId = user.getId();
        }

        // 身份证校验
        IdCardResponse authentication = this.accountServiceImpl.idCardAuth(new IdCardMesRequest(registerRequest.getIdNumber(), registerRequest.getName(), registerRequest.getGender(), null), userId);
        if (authentication.getRes()==2) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code() , "身份认证失败");
        }

        // 注册商户
        merchant = new Merchant();
        merchant.setCreateTime(System.currentTimeMillis());
        BeanUtils.copyProperties(registerRequest,merchant);
        this.merchantRepository.addMerchant(merchant);
        return merchant.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void merchantIssueCurrency(MerchantAssertIssuanceMessageRequest issuanceMessageRequest) throws Exception {

        // 商户校验
        Merchant merchant = this.merchantRepository.getMerchantById(issuanceMessageRequest.getMerchantId());
        if (merchant == null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code() , "该商家不存在");
        }

        // 商户发币状态校验
        MerchantAssertIssuanceAudit merchantAssertIssuanceAudit = this.assertIssuanceAuditRepository.findMerchantIssueByMerchantId(issuanceMessageRequest.getMerchantId());
        if (null != merchantAssertIssuanceAudit && merchantAssertIssuanceAudit.getStatus().equals(DEPOSIT_YES.code())) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code() , "发币成功后不允许修改");
        }

        // 币代号校验
        Long merchantId = this.issuanceRepository.getMerchantAssertIssuanceByCurrencySymbol(issuanceMessageRequest.getCurrencySymbol());
        if (merchantId != null && !issuanceMessageRequest.getMerchantId().equals(merchantId)) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code() , "该币代码已被其他商家注册，请重新输入");
        }
        MerchantAssertIssuance assertIssuance = new MerchantAssertIssuance();
        BeanUtils.copyProperties(issuanceMessageRequest, assertIssuance);
        assertIssuance.setIssuingDate(System.currentTimeMillis());

        // 更新操作
        if ( this.issuanceRepository.getAllByMerchantId(issuanceMessageRequest.getMerchantId() ) != null) {
            this.issuanceRepository.deleteByMerchantId(issuanceMessageRequest.getMerchantId());
        }
        this.issuanceRepository.addMerchantAssertIssuance(assertIssuance);
        if ( assertIssuance.getId() == null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code() , "发币失败，请重试");
        }

        // 商户货币发行审核初始化数据
        this.assertIssuanceAuditRepository.addMerchantAssertIssuanceAudit(assertIssuance.getId(),QUAL_NO.code(),System.currentTimeMillis());
    }

    @Override
    public MerchantAssetDetailsResponse merchantAssetDetails(Long merchantId) throws Exception {
        MerchantAssetDetailsResponse response = new MerchantAssetDetailsResponse();
        String currencySymbol = this.issuanceRepository.queryCurrencyByMerchantId(merchantId);
        Long userId = this.userRepository.getUserIdByMerchantId(merchantId);

        if (currencySymbol==null || userId == null ) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "未找到该商户信息");
        }

        BigDecimal sellRest = this.merchantAssertStatisticsRepository.queryAssertStaticSellRest(currencySymbol, userId, Constants.MerchantAssertStatisticsKey.SELL_REST.name());
        BigDecimal miningMind = this.merchantAssertStatisticsRepository.queryAssertStatisticsByKey(currencySymbol, MINING_MIND.name());
        BigDecimal miningRest = this.merchantAssertStatisticsRepository.queryAssertStatisticsByKey(currencySymbol, MINING_REST.name());

        response.setSellRest(sellRest);
        response.setMiningMind(miningMind);
        response.setMiningRest(miningRest);
        return response;
    }

    @Override
    public MerchantIssueCurrencyResponse getMerchantIssueCurrencyDetails(Long merchantId) throws Exception {
        MerchantIssueCurrencyResponse response = this.issuanceRepository.getMerchantIssueCurrencyDetails(merchantId);
        if (response!=null) {
            response.setAppUri(APP_DOWNLOAD_URI.value());
            response.setLiquidity(this.merchantAssertStatisticsRepository.queryMarketLiquidity(this.issuanceRepository.queryCurrencyByMerchantId(merchantId),
                    MINING_MIND.name(), SELL_SOLD.name()));
        }
        return response;
    }

    @Override
    public MerchantAssertIssuanceAudit merchantIssueStatus(Long merchantId) throws Exception {
        MerchantAssertIssuanceAudit issuanceAudit = this.assertIssuanceAuditRepository.findMerchantIssueByMerchantId(merchantId);
        if (issuanceAudit==null) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "未找到该商户的发币信息");
        }
        return issuanceAudit;
    }
}
