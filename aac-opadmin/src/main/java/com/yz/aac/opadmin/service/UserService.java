package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QueryUserFreezingAssertRequest;
import com.yz.aac.opadmin.model.request.QueryUserRequest;
import com.yz.aac.opadmin.model.request.UpdateUserIncreaseStrategyRequest;
import com.yz.aac.opadmin.model.request.UpdateUserStatusRequest;
import com.yz.aac.opadmin.model.response.QueryUserDetailResponse;
import com.yz.aac.opadmin.model.response.QueryUserFreezingAssertResponse;
import com.yz.aac.opadmin.model.response.QueryUserResponse;

public interface UserService {

    /**
     * 查询用户
     */
    QueryUserResponse queryUsers(QueryUserRequest request) throws Exception;

    /**
     * 查询用户冻结资产
     */
    QueryUserFreezingAssertResponse queryUserFreezingAsserts(QueryUserFreezingAssertRequest request) throws Exception;

    /**
     * 启用&停用用户
     */
    void updateUserStatus(UpdateUserStatusRequest request) throws Exception;

    /**
     * 更新用户自然增长策略
     */
    void updateUserIncreaseStrategy(UpdateUserIncreaseStrategyRequest request) throws Exception;

    /**
     * 查询用户详情
     */
    QueryUserDetailResponse queryUserDetail(Long userId) throws Exception;

}
