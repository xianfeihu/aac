package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryChargeResponse;
import com.yz.aac.opadmin.service.ChargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/charges")
@Slf4j
@Api(tags = "服务费（系统管理-手续费/押金设置）")
public class ChargeController extends BaseController {

    @Autowired
    private ChargeService chargeService;

    @ApiOperation("查询配置项")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryChargeResponse> queryCharges(
            @ApiParam(name = "name", value = "配置名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateSpecialChar(NAME.value(), name);
        QueryChargeRequest request = new QueryChargeRequest(name, new PagingRequest(pageNumber, pageSize));
        return buildSuccess(chargeService.queryCharges(request, getLoginInfo().getId()));
    }

    @ApiOperation("创建配置项")
    @PutMapping
    @ResponseBody
    public RootResponse<?> createCharge(@RequestBody CreateChargeRequest request) throws Exception {
        validateRequired(NAME.value(), request.getName());
        validateRequired(TRADE_CHARGE_RATE.value(), request.getTradeChargeRate());
        validateRequired(ISSUANCE_DEPOSIT.value(), request.getIssuanceDeposit());
        validateStringLength(NAME.value(), request.getName(), 1, 10);
        validateDoubleRange(TRADE_CHARGE_RATE.value(), request.getTradeChargeRate(), 1, 100);
        validateDoubleRange(ISSUANCE_DEPOSIT.value(), request.getIssuanceDeposit(), 0, 9999999);
        validateSpecialChar(NAME.value(), request.getName());
        chargeService.createCharge(request);
        return buildSuccess(null);
    }

    @ApiOperation("更新配置项")
    @PostMapping("/{id}")
    @ResponseBody
    public RootResponse<?> updateCharge(
            @ApiParam(name = "id", value = "配置项ID", required = true) @PathVariable(value = "id") Long id,
            @RequestBody UpdateChargeRequest request
    ) throws Exception {
        validateRequired(NAME.value(), request.getName());
        validateRequired(TRADE_CHARGE_RATE.value(), request.getTradeChargeRate());
        validateRequired(ISSUANCE_DEPOSIT.value(), request.getIssuanceDeposit());
        validateStringLength(NAME.value(), request.getName(), 1, 10);
        validateDoubleRange(TRADE_CHARGE_RATE.value(), request.getTradeChargeRate(), 1, 100);
        validateDoubleRange(ISSUANCE_DEPOSIT.value(), request.getIssuanceDeposit(), 0, 9999999);
        validateSpecialChar(NAME.value(), request.getName());
        request.setId(id);
        chargeService.updateCharge(request);
        return buildSuccess(null);
    }

    @ApiOperation("删除配置项")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RootResponse<?> deleteCharge(
            @ApiParam(name = "id", value = "配置项ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        chargeService.deleteCharge(new DeleteChargeRequest(id));
        return buildSuccess(null);
    }

    @ApiOperation("设置默认配置项")
    @PostMapping("/{id}/default")
    @ResponseBody
    public RootResponse<?> applyDefaultCharge(
            @ApiParam(name = "id", value = "配置项ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        chargeService.applyDefaultCharge(new ApplyDefaultChargeRequest(id));
        return buildSuccess(null);
    }

}