package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.CreateMiningActivityRequest;
import com.yz.aac.opadmin.model.request.QueryMiningActivityParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryMiningActivityParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryMiningActivityResponse;

public interface MiningService {

    /**
     * 创建活动
     */
    void createActivity(CreateMiningActivityRequest request) throws Exception;

    /**
     * 取消活动
     */
    void cancelActivity() throws Exception;

    /**
     * 查询活动
     */
    QueryMiningActivityResponse queryActivities() throws Exception;

    /**
     * 查询活动参与者
     */
    QueryMiningActivityParticipatorResponse queryMiningActivityParticipators(QueryMiningActivityParticipatorRequest request) throws Exception;

}
