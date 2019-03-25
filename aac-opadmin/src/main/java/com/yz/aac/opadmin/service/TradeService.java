package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryMerchantRecordResponse;
import com.yz.aac.opadmin.model.response.QueryPlatformOrderResponse;
import com.yz.aac.opadmin.model.response.QueryPlatformRecordResponse;

public interface TradeService {

    /**
     * 查询商家币交易记录
     */
    QueryMerchantRecordResponse queryMerchantRecords(QueryMerchantRecordRequest request) throws Exception;

    /**
     * 查询平台币交易记录
     */
    QueryPlatformRecordResponse queryPlatformRecords(QueryPlatformRecordRequest request) throws Exception;

    /**
     * 转账给平台币购买者
     */
    void transferPlatformRecord(TransferPlatformRecordRequest request) throws Exception;

    /**
     * 创建平台币卖单
     */
    void createPlatformOrder(CreatePlatformOrderRequest request) throws Exception;

    /**
     * 更新平台币卖单
     */
    void updatePlatformOrder(UpdatePlatformOrderRequest request) throws Exception;

    /**
     * 查询平台币卖单
     */
    QueryPlatformOrderResponse queryPlatformOrders(QueryPlatformOrderRequest request) throws Exception;


}
