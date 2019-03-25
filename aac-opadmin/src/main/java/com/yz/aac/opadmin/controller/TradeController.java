package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryMerchantRecordResponse;
import com.yz.aac.opadmin.model.response.QueryPlatformOrderResponse;
import com.yz.aac.opadmin.model.response.QueryPlatformRecordResponse;
import com.yz.aac.opadmin.service.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.Currency.MAX;
import static com.yz.aac.opadmin.Constants.ErrorMessage.AMOUNT_LESS_THAN_MAX_AMOUNT_LIMIT;
import static com.yz.aac.opadmin.Constants.ErrorMessage.MAX_AMOUNT_LIMIT_LESS_THAN_MIN;
import static com.yz.aac.opadmin.Constants.MerchantTradeType.BUY;
import static com.yz.aac.opadmin.Constants.MerchantTradeType.SELL;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/trade")
@Slf4j
@Api(tags = "交易（交易管理）")
public class TradeController extends BaseController {

    @Autowired
    private TradeService tradeService;

    @ApiOperation("查询商家币交易记录")
    @GetMapping("/merchant/records")
    @ResponseBody
    public RootResponse<QueryMerchantRecordResponse> queryMerchantRecords(
            @ApiParam(name = "beginTime", value = "发币开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "发币结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "currencySymbol", value = "币种") @RequestParam(value = "currencySymbol", required = false) String currencySymbol,
            @ApiParam(name = "direction", value = "交易类型（1-买入；2-售出）") @RequestParam(value = "direction", required = false) Integer direction,
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        if (null != direction) {
            validateIntRange(DIRECTION.value(), direction, BUY.value(), SELL.value());
        }
        if (StringUtils.isNotBlank(name)) {
            validateStringLength(NAME.value(), name, 1, 32);
        }
        if (StringUtils.isNotBlank(code)) {
            validateStringLength(CODE.value(), code, 1 + USER_CODE_PREFIX.value().length(), 10 + USER_CODE_PREFIX.value().length());
        }
        validateSpecialChar(CODE.value(), code);
        validateSpecialChar(NAME.value(), name);
        validateSpecialChar(CURRENCY_SYMBOL.value(), currencySymbol);
        return buildSuccess(tradeService.queryMerchantRecords(new QueryMerchantRecordRequest(
                beginTime, endTime, currencySymbol, direction, name, code, new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("查询平台币交易记录")
    @GetMapping("/platform/records")
    @ResponseBody
    public RootResponse<QueryPlatformRecordResponse> queryPlatformRecords(
            @ApiParam(name = "beginTime", value = "开始时间") @RequestParam(value = "beginTime", required = false) Long beginTime,
            @ApiParam(name = "endTime", value = "结束时间") @RequestParam(value = "endTime", required = false) Long endTime,
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "id", value = "交易编号") @RequestParam(value = "id", required = false) Long id,
            @ApiParam(name = "payNumber", value = "付款参考号") @RequestParam(value = "payNumber", required = false) String payNumber,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateTimePeriod(BEGIN_TIME.value(), beginTime, END_TIME.value(), endTime);
        if (StringUtils.isNotBlank(name)) {
            validateStringLength(NAME.value(), name, 1, 32);
        }
        if (StringUtils.isNotBlank(payNumber)) {
            validateStringLength(PAY_NUMBER.value(), payNumber, 1, 16);
        }
        validateSpecialChar(NAME.value(), name);
        return buildSuccess(tradeService.queryPlatformRecords(new QueryPlatformRecordRequest(
                beginTime, endTime, name, id, payNumber, new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("确认转账给平台币购买者")
    @PostMapping("/platform/records/{id}/transfer")
    @ResponseBody
    public RootResponse<?> confirmTransfer(
            @ApiParam(name = "id", value = "平台币交易记录ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        tradeService.transferPlatformRecord(new TransferPlatformRecordRequest(id, getLoginInfo().getId()));
        return buildSuccess(null);
    }

    @ApiOperation("查询平台币卖单")
    @GetMapping("/platform/orders")
    @ResponseBody
    public RootResponse<QueryPlatformOrderResponse> queryPlatformOrders(
            @ApiParam(name = "id", value = "卖单编号") @RequestParam(value = "id", required = false) Long id,
            @ApiParam(name = "name", value = "挂单人姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        if (StringUtils.isNotBlank(name)) {
            validateStringLength(NAME.value(), name, 1, 16);
        }
        validateSpecialChar(NAME.value(), name);
        return buildSuccess(tradeService.queryPlatformOrders(new QueryPlatformOrderRequest(
                id, name, new PagingRequest(pageNumber, pageSize)
        )));
    }

    @ApiOperation("创建平台币卖单")
    @PutMapping("/platform/orders")
    @ResponseBody
    public RootResponse<?> createOrder(@RequestBody CreatePlatformOrderRequest request) throws Exception {
        validateRequired(RMB_PRICE.value(), request.getRmbPrice());
        validateRequired(MIN_AMOUNT_LIMIT.value(), request.getMinAmountLimit());
        validateRequired(MAX_AMOUNT_LIMIT.value(), request.getMaxAmountLimit());
        validateRequired(AVAILABLE_TRADE_AMOUNT.value(), request.getAvailableTradeAmount());
        validateRequired(SELLER_ID.value(), request.getSellerId());
        validateBigDecimalRange(RMB_PRICE.value(), request.getRmbPrice(), BigDecimal.valueOf(0.01), BigDecimal.valueOf(99999.99));
        validateBigDecimalRange(MIN_AMOUNT_LIMIT.value(), request.getMinAmountLimit(), BigDecimal.valueOf(0.01), BigDecimal.valueOf(9999999.99));
        validateBigDecimalRange(MAX_AMOUNT_LIMIT.value(), request.getMaxAmountLimit(), BigDecimal.valueOf(0.01), BigDecimal.valueOf(9999999.99));
        validateBigDecimalRange(AVAILABLE_TRADE_AMOUNT.value(), request.getAvailableTradeAmount(), BigDecimal.valueOf(0.01), MAX.value());
        if (request.getMaxAmountLimit().compareTo(request.getMinAmountLimit()) < 0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MAX_AMOUNT_LIMIT_LESS_THAN_MIN.value());
        }
        if (request.getAvailableTradeAmount().compareTo(request.getMaxAmountLimit()) < 0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), AMOUNT_LESS_THAN_MAX_AMOUNT_LIMIT.value());
        }
        if (StringUtils.isNotBlank(request.getRemark())) {
            validateStringLength(REMARK.value(), request.getRemark(), 1, 128);
        }
        request.setLoginId(getLoginInfo().getId());
        tradeService.createPlatformOrder(request);
        return buildSuccess(null);
    }

    @ApiOperation("更新平台币卖单")
    @PostMapping("/platform/orders/{id}")
    @ResponseBody
    public RootResponse<?> updateOrder(
            @ApiParam(name = "id", value = "卖单ID", required = true) @PathVariable(value = "id") Long id,
            @RequestBody UpdatePlatformOrderRequest request
    ) throws Exception {
        validateRequired(RMB_PRICE.value(), request.getRmbPrice());
        validateRequired(MIN_AMOUNT_LIMIT.value(), request.getMinAmountLimit());
        validateRequired(MAX_AMOUNT_LIMIT.value(), request.getMaxAmountLimit());
        validateRequired(AVAILABLE_TRADE_AMOUNT.value(), request.getAvailableTradeAmount());
        validateRequired(SELLER_ID.value(), request.getSellerId());
        validateBigDecimalRange(RMB_PRICE.value(), request.getRmbPrice(), BigDecimal.valueOf(0.01), BigDecimal.valueOf(99999.99));
        validateBigDecimalRange(MIN_AMOUNT_LIMIT.value(), request.getMinAmountLimit(), BigDecimal.valueOf(0.01), BigDecimal.valueOf(9999999.99));
        validateBigDecimalRange(MAX_AMOUNT_LIMIT.value(), request.getMaxAmountLimit(), BigDecimal.valueOf(0.01), BigDecimal.valueOf(9999999.99));
        validateBigDecimalRange(AVAILABLE_TRADE_AMOUNT.value(), request.getAvailableTradeAmount(), BigDecimal.valueOf(0.01), MAX.value());
        if (request.getMaxAmountLimit().compareTo(request.getMinAmountLimit()) < 0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), MAX_AMOUNT_LIMIT_LESS_THAN_MIN.value());
        }
        if (request.getAvailableTradeAmount().compareTo(request.getMaxAmountLimit()) < 0) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), AMOUNT_LESS_THAN_MAX_AMOUNT_LIMIT.value());
        }
        if (StringUtils.isNotBlank(request.getRemark())) {
            validateStringLength(REMARK.value(), request.getRemark(), 1, 128);
        }
        request.setId(id);
        tradeService.updatePlatformOrder(request);
        return buildSuccess(null);
    }
}