package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.Constants.Config;
import com.yz.aac.opadmin.model.request.QueryConfigRequest;
import com.yz.aac.opadmin.model.request.UpdateConfigRequest;
import com.yz.aac.opadmin.model.response.QueryConfigResponse;
import com.yz.aac.opadmin.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.ErrorMessage.INVALID_CONFIG_PARAM;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/configs")
@Slf4j
@Api(tags = "配置")
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;

    @ApiOperation("查询配置")
    @GetMapping
    @ResponseBody
    public RootResponse<List<QueryConfigResponse>> queryConfigs(
            @ApiParam(name = "category", value = "大类（1-挖矿；2-交易）") @RequestParam(value = "category", required = false) Integer category,
            @ApiParam(name = "subCategory", value = "小类（1.1-阅读；1.2-实名认证；1.3-签到；1.4-公众号；1.5-邀请；1.6-红信；1.7-答题；2.1-挂单买卖；2.2-汇率）") @RequestParam(value = "subCategory", required = false) Integer subCategory,
            @ApiParam(name = "key", value = "键（内容较多，参阅DB初始化脚本）") @RequestParam(value = "key", required = false) String key
    ) throws Exception {
        return buildSuccess(configService.queryConfigs(new QueryConfigRequest(category, subCategory, key)));
    }

    @ApiOperation("更新配置")
    @PostMapping
    @ResponseBody
    public RootResponse<?> updateConfig(@RequestBody UpdateConfigRequest request) throws Exception {
        validateRequired(CATEGORY.value(), request.getCategory());
        validateRequired(SUB_CATEGORY.value(), request.getSubCategory());
        validateRequired(KEY.value(), request.getKey());
        validateRequired(VALUE.value(), request.getValue());
        boolean match = Arrays.stream(Config.values()).anyMatch(x -> x.category() == request.getCategory() && x.subCategory() == request.getSubCategory() && x.key().equals(request.getKey()));
        if (!match) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_CONFIG_PARAM.value());
        }
        validateStringLength(KEY.value(), request.getKey(), 1, 32);
        validateStringLength(VALUE.value(), request.getValue(), 1, 32);
        configService.updateConfig(request);
        return buildSuccess(null);
    }

}