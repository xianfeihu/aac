package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QueryConfigRequest;
import com.yz.aac.opadmin.model.request.UpdateConfigRequest;
import com.yz.aac.opadmin.model.response.QueryConfigResponse;

import java.util.List;

public interface ConfigService {

    /**
     * 查询配置项
     */
    List<QueryConfigResponse> queryConfigs(QueryConfigRequest request) throws Exception;

    /**
     * 更新配置项
     */
    void updateConfig(UpdateConfigRequest request) throws Exception;


}
