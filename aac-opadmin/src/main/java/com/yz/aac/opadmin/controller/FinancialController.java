package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryFinancialLedgerRequest;
import com.yz.aac.opadmin.model.response.QueryFinancialLedgerResponse;
import com.yz.aac.opadmin.service.FinancialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.LedgerInOut.IN;
import static com.yz.aac.opadmin.Constants.LedgerInOut.OUT;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static com.yz.aac.opadmin.Constants.UserRole.ADVERTISER;
import static com.yz.aac.opadmin.Constants.UserRole.ALL;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/platform/finance")
@Slf4j
@Api(tags = "平台币/法币进出帐（财务管理）")
public class FinancialController extends BaseController {

    @Autowired
    private FinancialService financialService;

    @ApiOperation("查询进出帐")
    @GetMapping("/ledgers")
    @ResponseBody
    public RootResponse<QueryFinancialLedgerResponse> queryLedgers(
            @ApiParam(name = "business", value = "业务领域（1-平台币；2-法币）", required = true) @RequestParam(value = "business", required = false) Integer business,
            @ApiParam(name = "direction", value = "进出帐方向（1-进账；2-出帐）", required = true) @RequestParam(value = "direction", required = false) Integer direction,
            @ApiParam(name = "userRole", value = "用户分类（1-全部；2-普通用户；3-商户；4-广告主）", required = true) @RequestParam(value = "userRole", required = false) Integer userRole,
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "userName", value = "用户姓名") @RequestParam(value = "userName", required = false) String userName,
            @ApiParam(name = "id", value = "编号") @RequestParam(value = "id", required = false) Long id,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(name = "outputStatistics", value = "是否输出相关金额统计", required = true) @RequestParam(value = "outputStatistics", required = false) Boolean outputStatistics
    ) throws Exception {
        validateRequired(BUSINESS.value(), business);
        validateRequired(DIRECTION.value(), direction);
        validateRequired(USER_ROLE.value(), userRole);
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateRequired(OUTPUT_STATISTICS.value(), outputStatistics);
        validateValueScope(BUSINESS.value(), business, new HashSet<>(Arrays.asList(1, 2)));
        validateIntRange(DIRECTION.value(), direction, IN.value(), OUT.value());
        validateIntRange(USER_ROLE.value(), userRole, ALL.value(), ADVERTISER.value());
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        validateSpecialChar(USER_NAME.value(), userName);
        QueryFinancialLedgerRequest request = new QueryFinancialLedgerRequest(
                business,
                direction,
                userRole,
                beginTime,
                endTime,
                userName,
                id,
                new PagingRequest(pageNumber, pageSize),
                outputStatistics
        );
        return buildSuccess(financialService.queryLedgers(request));
    }


}