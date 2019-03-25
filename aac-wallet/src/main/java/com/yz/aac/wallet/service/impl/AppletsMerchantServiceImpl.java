package com.yz.aac.wallet.service.impl;

import com.yz.aac.common.Constants;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.wallet.model.request.AppletsMerchantRegisterRequest;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.service.AppletsMerchantService;
import com.yz.aac.wallet.service.IdentityService;
import com.yz.aac.wallet.util.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppletsMerchantServiceImpl implements AppletsMerchantService {

    @Autowired
    private IdentityService identityServiceImpl;

    @Override
    public Long registerMerchant(AppletsMerchantRegisterRequest registerRequest) throws Exception {
        // 手机号校验
        if (!RegularUtil.phoneVerification(registerRequest.getMobileNumber())) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
        }

        // 身份证校验
        // 验证码校验
        // 身份信息校验
        IdCardResponse authentication = this.identityServiceImpl.Authentication(registerRequest.getIdNumber(), registerRequest.getName());
        if (authentication.getRes()==2) {
            throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code() , "身份认证失败");
        }
        // 注册商户
        return null;
    }

    @Override
    public String merchantIssueCurrency(AppletsMerchantRegisterRequest registerRequest) throws Exception {
        return null;
    }
}
