package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryChargeResponse;

import java.util.List;

public interface ChargeService {

    /**
     * 查询配置项
     */
    QueryChargeResponse queryCharges(QueryChargeRequest request, Long loginId) throws Exception;

    /**
     * 创建配置项
     */
    void createCharge(CreateChargeRequest request) throws Exception;

    /**
     * 更新配置项
     */
    void updateCharge(UpdateChargeRequest request) throws Exception;

    /**
     * 删除配置项
     */
    void deleteCharge(DeleteChargeRequest request) throws Exception;

    /**
     * 设置默认配置项
     */
    void applyDefaultCharge(ApplyDefaultChargeRequest request) throws Exception;

}
