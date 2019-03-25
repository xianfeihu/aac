package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryAlgorithmResponse;
import com.yz.aac.opadmin.service.AlgorithmService;
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
@RequestMapping(value = "/algorithms")
@Slf4j
@Api(tags = "自然增长算法（系统管理-自然增长设置）")
public class AlgorithmController extends BaseController {

    @Autowired
    private AlgorithmService algorithmService;

    @ApiOperation("查询算法")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryAlgorithmResponse> queryAlgorithms(
            @ApiParam(name = "name", value = "算法名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateSpecialChar(NAME.value(), name);
        QueryAlgorithmRequest request = new QueryAlgorithmRequest(name, new PagingRequest(pageNumber, pageSize));
        return buildSuccess(algorithmService.queryAlgorithms(request));
    }

    @ApiOperation("创建算法")
    @PutMapping
    @ResponseBody
    public RootResponse<?> createAlgorithm(@RequestBody CreateAlgorithmRequest request) throws Exception {
        validateRequired(NAME.value(), request.getName());
        validateRequired(INCREASED_POWER_POINT.value(), request.getIncreasedPowerPoint());
        validateRequired(CONSUMED_AD.value(), request.getConsumedAd());
        validateRequired(PLATFORM_CURRENCY.value(), request.getPlatformCurrency());
        validateStringLength(NAME.value(), request.getName(), 1, 10);
        validateIntRange(INCREASED_POWER_POINT.value(), request.getIncreasedPowerPoint(), 1, 99);
        validateIntRange(CONSUMED_AD.value(), request.getConsumedAd(), 1, 99);
        validateDoubleRange(PLATFORM_CURRENCY.value(), request.getPlatformCurrency(), 1.0D, 99.99D);
        validateSpecialChar(NAME.value(), request.getName());
        algorithmService.createAlgorithm(request);
        return buildSuccess(null);
    }

    @ApiOperation("更新算法")
    @PostMapping("/{id}")
    @ResponseBody
    public RootResponse<?> updateAlgorithm(
            @ApiParam(name = "id", value = "算法ID", required = true) @PathVariable(value = "id") Long id,
            @RequestBody UpdateAlgorithmRequest request
    ) throws Exception {
        validateRequired(NAME.value(), request.getName());
        validateRequired(INCREASED_POWER_POINT.value(), request.getIncreasedPowerPoint());
        validateRequired(CONSUMED_AD.value(), request.getConsumedAd());
        validateRequired(PLATFORM_CURRENCY.value(), request.getPlatformCurrency());
        validateStringLength(NAME.value(), request.getName(), 1, 10);
        validateIntRange(INCREASED_POWER_POINT.value(), request.getIncreasedPowerPoint(), 1, 99);
        validateIntRange(CONSUMED_AD.value(), request.getConsumedAd(), 1, 99);
        validateDoubleRange(PLATFORM_CURRENCY.value(), request.getPlatformCurrency(), 1.0D, 99.99D);
        validateSpecialChar(NAME.value(), request.getName());
        request.setId(id);
        algorithmService.updateAlgorithm(request);
        return buildSuccess(null);
    }

    @ApiOperation("删除算法")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RootResponse<?> deleteAlgorithm(
            @ApiParam(name = "id", value = "算法ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        algorithmService.deleteAlgorithm(new DeleteAlgorithmRequest(id));
        return buildSuccess(null);
    }

    @ApiOperation("设置默认算法")
    @PostMapping("/{id}/default")
    @ResponseBody
    public RootResponse<?> applyDefaultAlgorithm(
            @ApiParam(name = "id", value = "算法ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        algorithmService.applyDefaultAlgorithm(new ApplyDefaultAlgorithmRequest(id));
        return buildSuccess(null);
    }

}