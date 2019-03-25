package com.yz.aac.mining.service;

import com.yz.aac.mining.model.request.MerchantCurrencyExchangeRequest;

public interface MerchantCurrencyExchangeService {

    /**
     * 获取挖矿奖励
     * @param request
     * @return
     * @throws Exception
     */
    Boolean getMiningRewards(MerchantCurrencyExchangeRequest request) throws Exception;
}
