package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.request.AppletsMerchantRegisterRequest;

public interface AppletsMerchantService {

    /**
     * 注册商户
     * @param registerRequest
     * @return 商户ID
     */
    Long registerMerchant(AppletsMerchantRegisterRequest registerRequest) throws Exception;

    /**
     * 商户发行货币
     * @param registerRequest
     * @return
     */
    String merchantIssueCurrency(AppletsMerchantRegisterRequest registerRequest) throws Exception;
}
