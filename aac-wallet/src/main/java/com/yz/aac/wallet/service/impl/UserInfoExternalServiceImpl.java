package com.yz.aac.wallet.service.impl;

import com.yz.aac.common.Constants;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.wallet.model.response.SmsResponse;
import com.yz.aac.wallet.repository.MerchantAssertIssuanceRepository;
import com.yz.aac.wallet.repository.UserAssertRepository;
import com.yz.aac.wallet.repository.UserRepository;
import com.yz.aac.wallet.repository.domain.User;
import com.yz.aac.wallet.repository.domain.UserAssert;
import com.yz.aac.wallet.service.SmsService;
import com.yz.aac.wallet.service.UserInfoExternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;

@Service
@Slf4j
public class UserInfoExternalServiceImpl implements UserInfoExternalService {

    @Autowired
    private SmsService smsServiceImpl;

    @Autowired
    private UserAssertRepository userAssertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long synchronizedUserAssetFromExternal(String mobileNumber, String code, Long merchantId, BigDecimal balance) throws Exception {
        String currencySymbol = this.merchantAssertIssuanceRepository.queryCurrencyByMerchantId(merchantId);
        if (currencySymbol==null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "该商户不存在！");
        }
        User user = this.userRepository.getUserByMobile(Long.parseLong(mobileNumber));
        if (user==null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "请前往App端注册并实名认证后再同步");
        }
        if (user.getName()==null || user.getIdNumber()==null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "请前往App端实名认证再同步");
        }

        // 验证码校验
        SmsResponse verifycode = this.smsServiceImpl.verifycode(mobileNumber, code, com.yz.aac.wallet.Constants.SmsCodeType.MINI_PROGRAM_SYN.code());
        if (com.yz.aac.wallet.Constants.YunXinErrorCodeEnum.CODE_OK.errorCode() != verifycode.getCode()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), "验证码错误");
        }

        UserAssert userAssert = this.userAssertRepository.queryUserAssert(user.getId(), currencySymbol);
        if (userAssert==null) {
            // 新建该商家下用户资产信息
            this.userAssertRepository.saveUserAssert(new UserAssert(null,
                    user.getId(),
                    currencySymbol,
                    balance,
                    balance,
                    RandomUtil.genUUID(),
                    System.currentTimeMillis()));
        } else if (userAssert.getSynchronizedTime()==null) {
            // 修改当前用户资产同步信息
            if (this.userAssertRepository.updateUserAssetSynchronizedTime(userAssert.getId(), balance, System.currentTimeMillis())!=1) {
                throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "同步失败，请稍后重试！");
            }
        }
        return user.getId();
    }

    @Override
    public BigDecimal getUserAssetById(Long id, String currencySymbol, Boolean flag) throws Exception {
        BigDecimal asset = null;
        if (flag) {
            asset = this.userAssertRepository.getUserAssetsByCurrencySymbol(id, currencySymbol);
        } else {
           asset = this.userAssertRepository.getUserAvailableFundsByCurrencySymbol(id, currencySymbol);
        }
        if (asset == null) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "该用户未同步资产");
        }
        return asset;
    }
}
