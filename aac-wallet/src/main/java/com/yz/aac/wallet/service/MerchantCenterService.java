package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.request.MerchantAssertIssuanceMessageRequest;
import com.yz.aac.wallet.model.request.MerchantRegisterBaseMessageRequest;
import com.yz.aac.wallet.model.response.MerchantAssetDetailsResponse;
import com.yz.aac.wallet.model.response.MerchantIssueCurrencyResponse;
import com.yz.aac.wallet.repository.domain.MerchantAssertIssuanceAudit;

public interface MerchantCenterService {

    /**
     * 通过手机号校验已注册的商户数据
     * @param mobile
     * @return
     */
    Long checkMerchantByMobile(String mobile) throws Exception;

    /**
     * 注册商户
     * @param registerRequest
     * @return 商户ID
     */
    Long checkMerchantBaseMessage(MerchantRegisterBaseMessageRequest registerRequest) throws Exception;

    /**
     * 商户发行货币
     * @param issuanceMessageRequest
     * @return
     */
    void merchantIssueCurrency(MerchantAssertIssuanceMessageRequest issuanceMessageRequest) throws Exception;

    /**
     * 查询商户当前状态
     * @param merchantId
     * @return
     */
    MerchantAssertIssuanceAudit merchantIssueStatus(Long merchantId) throws Exception;

    /**
     * 获取商户资产信息
     * @param merchantId
     * @return
     */
    MerchantAssetDetailsResponse merchantAssetDetails(Long merchantId) throws Exception;

    /**
     * 获取商户发布等相关信息
     * @param merchantId
     * @return
     */
    MerchantIssueCurrencyResponse getMerchantIssueCurrencyDetails(Long merchantId) throws Exception;
}
