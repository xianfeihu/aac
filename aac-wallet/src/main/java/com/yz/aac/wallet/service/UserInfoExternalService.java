package com.yz.aac.wallet.service;

import java.math.BigDecimal;

public interface UserInfoExternalService {

    /**
     * 小程序用户与APP用户同步
     * @param mobileNumber
     * @param code
     * @param merchantId
     * @param balance
     * @return
     */
    Long synchronizedUserAssetFromExternal(String mobileNumber, String code, Long merchantId, BigDecimal balance) throws Exception;

    BigDecimal getUserAssetById(Long id, String currencySymbol, Boolean flag) throws Exception;
}
