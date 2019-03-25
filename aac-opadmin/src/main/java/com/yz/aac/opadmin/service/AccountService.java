package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.LoginResponse;
import com.yz.aac.opadmin.model.response.QueryAccessLogResponse;
import com.yz.aac.opadmin.model.response.QueryAccountResponse;

public interface AccountService {

    /**
     * 登录
     */
    LoginResponse login(LoginRequest request) throws Exception;

    /**
     * 创建账号
     */
    void createAccount(CreateAccountRequest request) throws Exception;

    /**
     * 停用账号
     */
    void disableAccount(DisableAccountRequest request, Long loginId) throws Exception;

    /**
     * 启用账号
     */
    void enableAccount(EnableAccountRequest request, Long loginId) throws Exception;

    /**
     * 查询账号
     */
    QueryAccountResponse queryAccounts(QueryAccountRequest request) throws Exception;

    /**
     * 查询操作日志
     */
    QueryAccessLogResponse queryAccessLogs(QueryAccessLogRequest request, Long loginId) throws Exception;

}
