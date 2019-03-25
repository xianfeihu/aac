package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QueryRedPacketRequest;
import com.yz.aac.opadmin.model.response.QueryRedPacketDetailResponse;
import com.yz.aac.opadmin.model.response.QueryRedPacketResponse;

public interface RedPacketService {

    /**
     * 查询发布的红包
     */
    QueryRedPacketResponse queryRedPackets(QueryRedPacketRequest request) throws Exception;

    /**
     * 查询红包详情
     */

    QueryRedPacketDetailResponse queryRedPacketDetail(Long id) throws Exception;

}
