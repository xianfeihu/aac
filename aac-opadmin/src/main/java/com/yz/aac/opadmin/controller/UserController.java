package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryUserDetailResponse;
import com.yz.aac.opadmin.model.response.QueryUserFreezingAssertResponse;
import com.yz.aac.opadmin.model.response.QueryUserResponse;
import com.yz.aac.opadmin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INVALID_TIME_PERIOD;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.AccountStatus.DISABLED;
import static com.yz.aac.opadmin.Constants.AccountStatus.ENABLED;
import static com.yz.aac.opadmin.Constants.Currency.MAX;
import static com.yz.aac.opadmin.Constants.Currency.MIN;
import static com.yz.aac.opadmin.Constants.ErrorMessage.INVALID_CURRENCY_PERIOD;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/users")
@Slf4j
@Api(tags = "普通用户（用户管理-普通用户管理）")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation("查询普通用户")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryUserResponse> queryUsers(
            @ApiParam(name = "levelId", value = "等级ID") @RequestParam(value = "levelId", required = false) Long levelId,
            @ApiParam(name = "beginTime", value = "注册开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "minBalance", value = "资产金额下限") @RequestParam(value = "minBalance", required = false) BigDecimal minBalance,
            @ApiParam(name = "maxBalance", value = "资产金额上限") @RequestParam(value = "maxBalance", required = false) BigDecimal maxBalance,
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "mobileNumber", value = "手机号") @RequestParam(value = "mobileNumber", required = false) Long mobileNumber,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        if (null != minBalance && minBalance.compareTo(BigDecimal.valueOf(0)) < 0) {
            validateBigDecimalRange(MIN_BALANCE.value(), minBalance, MIN.value(), MAX.value());
        }
        if (null != maxBalance && maxBalance.compareTo(BigDecimal.valueOf(0)) < 0) {
            validateBigDecimalRange(MAX_BALANCE.value(), maxBalance, MIN.value(), MAX.value());
        }
        if (null != minBalance && null != maxBalance && minBalance.compareTo(maxBalance) > 0) {
            throw new BusinessException(MSG_INVALID_TIME_PERIOD.code(), INVALID_CURRENCY_PERIOD.value());
        }
        validateSpecialChar(CODE.value(), code);
        validateSpecialChar(NAME.value(), name);
        return buildSuccess(userService.queryUsers(new QueryUserRequest(
                levelId,
                beginTime,
                endTime,
                minBalance,
                maxBalance,
                name,
                mobileNumber,
                code,
                new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("查询用户冻结资产")
    @GetMapping("/{id}/freezingAsserts")
    @ResponseBody
    public RootResponse<QueryUserFreezingAssertResponse> queryUserFreezingAsserts(
            @ApiParam(name = "id", value = "用户ID", required = true) @PathVariable(value = "id", required = false) Long userId,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        return buildSuccess(userService.queryUserFreezingAsserts(new QueryUserFreezingAssertRequest(
                userId,
                new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("启用&停用")
    @PostMapping("/{id}/status")
    @ResponseBody
    public RootResponse<?> updateUserStatus(
            @ApiParam(name = "id", value = "用户ID", required = true) @PathVariable(value = "id", required = false) Long userId,
            @RequestBody UpdateUserStatusRequest request
    ) throws Exception {
        validateRequired(STATUS.value(), request.getStatus());
        validateIntRange(STATUS.value(), request.getStatus(), ENABLED.value(), DISABLED.value());
        if (DISABLED.value() == request.getStatus()) {
            validateRequired(DESCRIPTION.value(), request.getDescription());
            validateStringLength(DESCRIPTION.value(), request.getDescription(), 1, 50);
        }
        request.setUserId(userId);
        userService.updateUserStatus(request);
        return buildSuccess(null);
    }

    @ApiOperation("更新自然增长策略")
    @PostMapping("/{id}/increaseStrategy")
    @ResponseBody
    public RootResponse<?> updateUserIncreaseStrategy(
            @ApiParam(name = "id", value = "用户ID", required = true) @PathVariable(value = "id", required = false) Long userId,
            @RequestBody UpdateUserIncreaseStrategyRequest request
    ) throws Exception {
        validateRequired(STRATEGY_ID.value(), request.getStrategyId());
        request.setUserId(userId);
        userService.updateUserIncreaseStrategy(request);
        return buildSuccess(null);
    }

    @ApiOperation("查询普通用户详细信息")
    @GetMapping("/{id}")
    @ResponseBody
    public RootResponse<QueryUserDetailResponse> queryUserDetail(
            @ApiParam(name = "id", value = "用户ID", required = true) @PathVariable(value = "id") Long userId
    ) throws Exception {
        return buildSuccess(userService.queryUserDetail(userId));
    }

}