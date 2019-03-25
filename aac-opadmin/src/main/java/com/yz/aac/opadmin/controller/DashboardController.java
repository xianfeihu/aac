package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryDashboardRecordRequest;
import com.yz.aac.opadmin.model.response.PlatformAssetOverviewResponse;
import com.yz.aac.opadmin.model.response.QueryDashboardRecordResponse;
import com.yz.aac.opadmin.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.DashboardTradeType.BUY;
import static com.yz.aac.opadmin.Constants.DashboardTradeType.CONSUME;
import static com.yz.aac.opadmin.Constants.DashboardTradeType.SELL;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static com.yz.aac.opadmin.Constants.UserRole.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/dashboard")
@Slf4j
@Api(tags = "首页")
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    @ApiOperation("平台资产总览")
    @GetMapping("/overview")
    @ResponseBody
    public RootResponse<PlatformAssetOverviewResponse> queryPlatformAssetOverview() throws Exception {
        return buildSuccess(dashboardService.queryPlatformAssetOverview());
    }

    @ApiOperation("查询平台币交易记录")
    @GetMapping("/platformCurrency/records")
    @ResponseBody
    public RootResponse<QueryDashboardRecordResponse> queryPlatformCurrencyRecords(
            @ApiParam(name = "userRole", value = "用户分类（2-普通用户；3-商户；4-广告主）", required = true) @RequestParam(value = "userRole", required = false) Integer userRole,
            @ApiParam(name = "tradeType", value = "交易类型（1-购买；2-消费）", required = true) @RequestParam(value = "tradeType", required = false) Integer tradeType,
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "name", value = "用户名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "outputStatistics", value = "是否交易额", required = true) @RequestParam(value = "outputStatistics", required = false) Boolean outputStatistics,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(USER_ROLE.value(), userRole);
        validateRequired(TRADE_TYPE.value(), tradeType);
        validateRequired(OUTPUT_STATISTICS.value(), outputStatistics);
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        validateSpecialChar(CODE.value(), code);
        validateSpecialChar(NAME.value(), name);
        validateValueScope(USER_ROLE.value(), userRole, new HashSet<>(Arrays.asList(GENERAL.value(), MERCHANT.value(), ADVERTISER.value())));
        validateValueScope(TRADE_TYPE.value(), tradeType, new HashSet<>(Arrays.asList(BUY.value(), CONSUME.value())));
        return buildSuccess(dashboardService.queryPlatformAssetRecords(new QueryDashboardRecordRequest(
                userRole, tradeType, beginTime, endTime, name, code, outputStatistics, null, new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("查询商家币交易记录")
    @GetMapping("/merchantCurrency/records")
    @ResponseBody
    public RootResponse<QueryDashboardRecordResponse> queryMerchantCurrencyRecords(
            @ApiParam(name = "currencySymbol", value = "币种", required = true) @RequestParam(value = "currencySymbol", required = false) String currencySymbol,
            @ApiParam(name = "tradeType", value = "交易类型（1-购买；3-出售）", required = true) @RequestParam(value = "tradeType", required = false) Integer tradeType,
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "name", value = "用户名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "outputStatistics", value = "是否交易额", required = true) @RequestParam(value = "outputStatistics", required = false) Boolean outputStatistics,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(CURRENCY_SYMBOL.value(), currencySymbol);
        validateRequired(TRADE_TYPE.value(), tradeType);
        validateRequired(OUTPUT_STATISTICS.value(), outputStatistics);
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        validateSpecialChar(CODE.value(), code);
        validateSpecialChar(NAME.value(), name);
        validateValueScope(TRADE_TYPE.value(), tradeType, new HashSet<>(Arrays.asList(BUY.value(), SELL.value())));
        return buildSuccess(dashboardService.queryMerchantAssetRecords(new QueryDashboardRecordRequest(
                null, tradeType, beginTime, endTime, name, code, outputStatistics, currencySymbol, new PagingRequest(pageNumber, pageSize)
        )));
    }

}