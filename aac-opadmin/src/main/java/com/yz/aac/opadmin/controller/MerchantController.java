package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.*;
import com.yz.aac.opadmin.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.MerchantAuditStatus;
import static com.yz.aac.opadmin.Constants.MerchantAuditStatus.*;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static com.yz.aac.opadmin.Constants.UserAssertFreezingReason.DEPOSIT;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/merchants")
@Slf4j
@Api(tags = "商家（用户管理-商户管理）")
public class MerchantController extends BaseController {

    @Autowired
    private MerchantService merchantService;

    @ApiOperation("查询所有已发行的商家币种")
    @GetMapping("/currency/symbols")
    @ResponseBody
    public RootResponse<List<QueryMerchantCurrencyResponse>> queryMerchantCurrencies() throws Exception {
        return buildSuccess(merchantService.queryCurrencies());
    }

    @ApiOperation("查询商家所有审核状态")
    @GetMapping("/audit/status")
    @ResponseBody
    public RootResponse<List<QueryAuditStatusItemResponse>> queryAuditStatus() throws Exception {
        return buildSuccess(merchantService.queryAuditStatus());
    }

    @ApiOperation("查询商家")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryMerchantResponse> queryMerchants(
            @ApiParam(name = "currencySymbol", value = "币种") @RequestParam(value = "currencySymbol", required = false) String currencySymbol,
            @ApiParam(name = "beginTime", value = "发币开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "发币结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "status", value = "状态（1-启用；2-停用）") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam(name = "name", value = "商户名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "mobileNumber", value = "手机号") @RequestParam(value = "mobileNumber", required = false) Long mobileNumber,
            @ApiParam(name = "code", value = "商户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "outputStatistics", value = "是否统计收入来源", required = true) @RequestParam(value = "outputStatistics", required = false) Boolean outputStatistics,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(OUTPUT_STATISTICS.value(), outputStatistics);
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        validateSpecialChar(CODE.value(), code);
        validateSpecialChar(NAME.value(), name);
        validateSpecialChar(CURRENCY_SYMBOL.value(), currencySymbol);
        return buildSuccess(merchantService.queryMerchants(new QueryMerchantRequest(
                currencySymbol,
                beginTime,
                endTime,
                status,
                name,
                mobileNumber,
                code,
                outputStatistics,
                new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("查询商家发币申请")
    @GetMapping("/issuance/requests")
    @ResponseBody
    public RootResponse<QueryMerchantIssuanceResponse> queryIssuanceRequests(
            @ApiParam(name = "currencySymbol", value = "币种") @RequestParam(value = "currencySymbol", required = false) String currencySymbol,
            @ApiParam(name = "status", value = "状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam(name = "name", value = "商户名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "mobileNumber", value = "手机号") @RequestParam(value = "mobileNumber", required = false) Long mobileNumber,
            @ApiParam(name = "code", value = "商户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateSpecialChar(CODE.value(), code);
        validateSpecialChar(NAME.value(), name);
        validateSpecialChar(CURRENCY_SYMBOL.value(), currencySymbol);
        if (null != status) {
            validateValueScope(STATUS.value(), status, Arrays.stream(MerchantAuditStatus.values()).map(MerchantAuditStatus::status).collect(Collectors.toSet()));
        }
        return buildSuccess(merchantService.queryMerchantIssuanceRequests(new QueryMerchantIssuanceRequest(
                currencySymbol, status, name, mobileNumber, code, new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("审核发币申请")
    @PostMapping("/audit/{id}")
    @ResponseBody
    public RootResponse<?> updateUserIncreaseStrategy(
            @ApiParam(name = "requestId", value = "申请ID", required = true) @PathVariable(value = "id", required = false) Long requestId,
            @RequestBody AuditIssuanceRequest request
    ) throws Exception {
        validateRequired(STATUS.value(), request.getStatus());
        Set<Integer> statusValues = new HashSet<>(Arrays.asList(
                WAITING_DEPOSIT.status(),
                ISSUED_REJECTED.status(),
                DEPOSIT_APPROVED.status(),
                DEPOSIT_REJECTED.status()
        ));
        validateValueScope(STATUS.value(), request.getStatus(), statusValues);
        if (request.getStatus() == WAITING_DEPOSIT.status()) {
            validateRequired(SERVICE_CHARGE_ID.value(), request.getServiceChargeId());
        }
        if (request.getStatus() == ISSUED_REJECTED.status() || request.getStatus() == DEPOSIT_REJECTED.status()) {
            validateRequired(REMARK.value(), request.getRemark());
            validateStringLength(REMARK.value(), request.getRemark(), 1, 100);
        }
        request.setLoginId(getLoginInfo().getId());
        request.setRequestId(requestId);
        merchantService.auditIssuance(request);
        return buildSuccess(null);
    }

    @ApiOperation("资产解冻")
    @PostMapping("/asset/unfreeze")
    @ResponseBody
    public RootResponse<?> unfreezeAsset(@RequestBody UnfreezeAssetRequest request) throws Exception {
        validateRequired(USER_ID.value(), request.getUserId());
        validateRequired(FREEZING_TYPE.value(), request.getFreezingType());
        validateIntRange(FREEZING_TYPE.value(), request.getFreezingType(), DEPOSIT.code(), DEPOSIT.code());
        merchantService.unfreezeAsset(request);
        return buildSuccess(null);
    }

    @ApiOperation("查询商户详细信息")
    @GetMapping("/{id}")
    @ResponseBody
    public RootResponse<QueryUserDetailResponse> queryMerchantDetail(
            @ApiParam(name = "id", value = "商户ID", required = true) @PathVariable(value = "id") Long merchantId
    ) throws Exception {
        return buildSuccess(merchantService.queryMerchantDetail(merchantId));
    }
}