package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QueryDashboardRecordRequest;
import com.yz.aac.opadmin.model.response.PlatformAssetOverviewResponse;
import com.yz.aac.opadmin.model.response.QueryDashboardRecordResponse;

public interface DashboardService {


    /**
     * 查询平台资产概要
     */
    PlatformAssetOverviewResponse queryPlatformAssetOverview() throws Exception;

    /**
     * 查询平台币交易记录
     */
    QueryDashboardRecordResponse queryPlatformAssetRecords(QueryDashboardRecordRequest request) throws Exception;

    /**
     * 查询商家币交易记录
     */
    QueryDashboardRecordResponse queryMerchantAssetRecords(QueryDashboardRecordRequest request) throws Exception;

}
