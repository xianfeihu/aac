package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.CreateExchangeRequest;
import com.yz.aac.opadmin.model.request.QueryExchangeRecordRequest;
import com.yz.aac.opadmin.model.request.QueryExchangeRequest;
import com.yz.aac.opadmin.model.response.QueryExchangeRecordResponse;
import com.yz.aac.opadmin.repository.domain.Exchange;

import java.util.List;

public interface ExchangeService {

    /**
     * 查询可添加的服务
     */
    List<Exchange> queryDeactivated() throws Exception;

    /**
     * 查询服务
     */
    List<Exchange> queryExchanges(QueryExchangeRequest request) throws Exception;

    /**
     * 创建服务
     */
    void createExchange(CreateExchangeRequest request) throws Exception;

    /**
     * 更新服务
     */
    void updateExchange(CreateExchangeRequest request) throws Exception;

    /**
     * 删除服务
     */
    void deleteExchange(Long id) throws Exception;

    /**
     * 查询用户交易记录
     */
    QueryExchangeRecordResponse queryRecords(QueryExchangeRecordRequest request) throws Exception;

    /**
     * 确认充值
     */
    void charging(Long id) throws Exception;

}
