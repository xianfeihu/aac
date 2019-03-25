package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.CreateExchangeRequest;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryExchangeRecordRequest;
import com.yz.aac.opadmin.model.request.QueryExchangeRequest;
import com.yz.aac.opadmin.model.response.QueryExchangeRecordResponse;
import com.yz.aac.opadmin.repository.domain.Exchange;
import com.yz.aac.opadmin.service.ExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/exchanges")
@Slf4j
@Api(tags = "兑换（兑换管理）")
public class ExchangeController extends BaseController {

    @Autowired
    private ExchangeService exchangeService;

    @ApiOperation("查询可开通的兑换服务")
    @GetMapping("/deactivated")
    @ResponseBody
    public RootResponse<List<Exchange>> queryDeactivated() throws Exception {
        return buildSuccess(exchangeService.queryDeactivated());
    }

    @ApiOperation("创建兑换服务")
    @PutMapping
    @ResponseBody
    public RootResponse<?> createExchange(@RequestBody CreateExchangeRequest request) throws Exception {
        validate(request);
        exchangeService.createExchange(request);
        return buildSuccess(null);
    }

    @ApiOperation("更新兑换服务")
    @PostMapping
    @ResponseBody
    public RootResponse<?> updateExchange(@RequestBody CreateExchangeRequest request) throws Exception {
        validate(request);
        exchangeService.updateExchange(request);
        return buildSuccess(null);
    }

    @ApiOperation("删除兑换服务")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RootResponse<?> deleteExchange(
            @ApiParam(name = "id", value = "ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        exchangeService.deleteExchange(id);
        return buildSuccess(null);
    }

    @ApiOperation("查询兑换服务")
    @GetMapping
    @ResponseBody
    public RootResponse<List<Exchange>> queryExchanges(
            @ApiParam(name = "id", value = "服务ID") @RequestParam(value = "id", required = false) Long id,
            @ApiParam(name = "includeItem", value = "是否包含兑换项数据") @RequestParam(value = "includeItem", required = false) Boolean includeItem
    ) throws Exception {
        return buildSuccess(exchangeService.queryExchanges(new QueryExchangeRequest(id, includeItem)));
    }

    @ApiOperation("查询兑换记录")
    @GetMapping("/records")
    @ResponseBody
    public RootResponse<QueryExchangeRecordResponse> queryRecords(
            @ApiParam(name = "userName", value = "用户名称") @RequestParam(value = "userName", required = false) String userName,
            @ApiParam(name = "exchangeId", value = "服务ID（充值类型）") @RequestParam(value = "exchangeId", required = false) Long exchangeId,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        if (StringUtils.isNotBlank(userName)) {
            validateStringLength(USER_NAME.value(), userName, 1, 16);
            validateSpecialChar(USER_NAME.value(), userName);
        }
        QueryExchangeRecordRequest request = new QueryExchangeRecordRequest(null, userName, exchangeId, null, null, null, new PagingRequest(pageNumber, pageSize));
        return buildSuccess(exchangeService.queryRecords(request));
    }

    @ApiOperation("确认充值")
    @PostMapping("/records/{id}/charging")
    @ResponseBody
    public RootResponse<?> charging(
            @ApiParam(name = "id", value = "ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        exchangeService.charging(id);
        return buildSuccess(null);
    }

    private void validate(CreateExchangeRequest request) throws Exception {
        validateRequired(ID.value(), request.getId());
        validateRequired(CUSTOMIZED.value(), request.getCustomized());
        validateRequired(LIMIT_IN_MONTH.value(), request.getLimitInMonth());
        validateValueScope(CUSTOMIZED.value(), request.getCustomized(), new HashSet<>(Arrays.asList(YES.value(), NO.value())));
        validateIntRange(LIMIT_IN_MONTH.value(), request.getLimitInMonth(), 1, 10);
        if (CollectionUtils.isEmpty(request.getItems())) {
            validateRequired(ITEMS.value(), null);
        }
        for (int i = 0; i < request.getItems().size(); i++) {
            String fieldNamePrefix = String.format("%s.%d.", ITEMS.value(), i);
            CreateExchangeRequest.Item item = request.getItems().get(i);
            validateRequired(fieldNamePrefix + RMB_AMOUNT.value(), item.getRmbAmount());
            validateRequired(fieldNamePrefix + PLATFORM_AMOUNT.value(), item.getPlatformAmount());
            validateIntRange(fieldNamePrefix + RMB_AMOUNT.value(), item.getRmbAmount(), 1, 10000);
            validateIntRange(fieldNamePrefix + PLATFORM_AMOUNT.value(), item.getPlatformAmount(), 1, 10000);
        }
    }

}