package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.AuditIssuanceRequest;
import com.yz.aac.opadmin.model.request.QueryMerchantIssuanceRequest;
import com.yz.aac.opadmin.model.request.QueryMerchantRequest;
import com.yz.aac.opadmin.model.request.UnfreezeAssetRequest;
import com.yz.aac.opadmin.model.response.*;

import java.util.List;

public interface MerchantService {


    /**
     * 查询所有商户已发行的币种
     */
    List<QueryMerchantCurrencyResponse> queryCurrencies() throws Exception;

    /**
     * 查询所有审核状态
     */
    List<QueryAuditStatusItemResponse> queryAuditStatus() throws Exception;

    /**
     * 查询商户
     */
    QueryMerchantResponse queryMerchants(QueryMerchantRequest request) throws Exception;

    /**
     * 查询商户发币申请
     */
    QueryMerchantIssuanceResponse queryMerchantIssuanceRequests(QueryMerchantIssuanceRequest request) throws Exception;

    /**
     * 审核商家发币申请
     */
    void auditIssuance(AuditIssuanceRequest request) throws Exception;

    /**
     * 用户资产解冻
     */
    void unfreezeAsset(UnfreezeAssetRequest request) throws Exception;

    /**
     * 查询商户详情
     */
    QueryUserDetailResponse queryMerchantDetail(Long merchant) throws Exception;
}