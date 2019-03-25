package com.yz.aac.opadmin.controller;

import com.yz.aac.common.config.SecurityConfig;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.EncryptionUtil;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.LoginResponse;
import com.yz.aac.opadmin.model.response.QueryAccessLogResponse;
import com.yz.aac.opadmin.model.response.QueryAccountResponse;
import com.yz.aac.opadmin.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.yz.aac.common.Constants.Token.*;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/accounts")
@Slf4j
@Api(tags = "账号（系统管理-账号设置）")
public class AccountController extends BaseController {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private AccountService accountService;

    @ApiOperation("登录")
    @PostMapping("/login")
    @ResponseBody
    public RootResponse<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {
        validateRequired(LOGIN_NAME.value(), request.getLoginName());
        validateRequired(PASSWORD.value(), request.getPassword());
        validateSpecialChar(LOGIN_NAME.value(), request.getLoginName());
        validateStringLength(LOGIN_NAME.value(), request.getLoginName(), 1, 20);
        validateStringLength(PASSWORD.value(), request.getPassword(), 1, 20);
        LoginResponse response = accountService.login(request);
        genTokenInHeader(response);
        return buildSuccess(response);
    }

    @ApiOperation("创建账号")
    @PutMapping
    @ResponseBody
    public RootResponse<?> createAccount(@RequestBody CreateAccountRequest request) throws Exception {
        validateRequired(LOGIN_NAME.value(), request.getLoginName());
        validateRequired(PASSWORD.value(), request.getPassword());
        validateRequired(NAME.value(), request.getName());
        validateRequired(DEPARTMENT.value(), request.getDepartment());
        validateStringLength(LOGIN_NAME.value(), request.getLoginName(), 1, 20);
        validateStringLength(PASSWORD.value(), request.getPassword(), 1, 20);
        validateStringLength(NAME.value(), request.getName(), 1, 10);
        validateStringLength(DEPARTMENT.value(), request.getDepartment(), 1, 10);
        validateSpecialChar(LOGIN_NAME.value(), request.getLoginName());
        validateSpecialChar(NAME.value(), request.getName());
        accountService.createAccount(request);
        return buildSuccess(null);
    }

    @ApiOperation("停用账号")
    @PostMapping("/{id}/disablement")
    @ResponseBody
    public RootResponse<?> disableAccount(
            @ApiParam(name = "id", value = "账号ID", required = true) @PathVariable(value = "id") Long operatorId
    ) throws Exception {
        accountService.disableAccount(new DisableAccountRequest(operatorId), getLoginInfo().getId());
        return buildSuccess(null);
    }

    @ApiOperation("启用账号")
    @PostMapping("/{id}/enablement")
    @ResponseBody
    public RootResponse<?> enableAccount(
            @ApiParam(name = "id", value = "账号ID", required = true) @PathVariable(value = "id") Long operatorId
    ) throws Exception {
        accountService.enableAccount(new EnableAccountRequest(operatorId), getLoginInfo().getId());
        return buildSuccess(null);
    }

    @ApiOperation("查询账号")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryAccountResponse> queryAccounts(
            @ApiParam(name = "loginName", value = "账号") @RequestParam(value = "loginName", required = false) String loginName,
            @ApiParam(name = "name", value = "姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateSpecialChar(LOGIN_NAME.value(), loginName);
        validateSpecialChar(NAME.value(), name);
        QueryAccountRequest request = new QueryAccountRequest(
                loginName,
                name,
                new PagingRequest(pageNumber, pageSize)
        );
        return buildSuccess(accountService.queryAccounts(request));
    }

    @ApiOperation("查询指定账号访问日志")
    @GetMapping("/{id}/accessLogs")
    @ResponseBody
    public RootResponse<QueryAccessLogResponse> queryAccessLogs(
            @ApiParam(name = "id", value = "账号ID", required = true) @PathVariable(value = "id") Long operatorId,
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        QueryAccessLogRequest request = new QueryAccessLogRequest(
                operatorId,
                beginTime,
                endTime,
                new PagingRequest(pageNumber, pageSize)
        );
        return buildSuccess(accountService.queryAccessLogs(request, getLoginInfo().getId()));
    }

    private void genTokenInHeader(LoginResponse response) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_CLAIM_LOGIN_ID.value(), response.getId());
        claims.put(TOKEN_CLAIM_ROLE_IDS.value(), response.getRoles());
        String token = EncryptionUtil.createToken(claims, Long.parseLong(TTL_MILLIS.value()), securityConfig.getTokenSecurityKey());
        getResponse().addHeader(TOKEN_KEY.value(), token);
    }

}