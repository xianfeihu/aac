package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.CreateUserLevelRequest;
import com.yz.aac.opadmin.model.request.QueryUserLevelRequest;
import com.yz.aac.opadmin.model.request.UpdateUserLevelRequest;
import com.yz.aac.opadmin.model.response.QueryUserLevelResponse;

import java.util.List;

public interface UserLevelService {

    /**
     * 查询用户等级
     */
    QueryUserLevelResponse queryUserLevels(QueryUserLevelRequest request) throws Exception;

    /**
     * 创建用户等级
     */
    void createUserLevel(CreateUserLevelRequest request) throws Exception;

    /**
     * 更新用户等级
     */
    void updateUserLevel(UpdateUserLevelRequest request) throws Exception;


}
