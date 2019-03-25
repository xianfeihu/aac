package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryRedPacketRequest;
import com.yz.aac.opadmin.model.response.QueryRedPacketDetailResponse;
import com.yz.aac.opadmin.model.response.QueryRedPacketResponse;
import com.yz.aac.opadmin.service.RedPacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/redPackets")
@Slf4j
@Api(tags = "红包（app内容管理-红信红包）")
public class RedPacketController extends BaseController {

    @Autowired
    private RedPacketService redPacketService;

    @ApiOperation("查询发布的红包")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryRedPacketResponse> queryRedPackets(
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        if (StringUtils.isNotBlank(name)) {
            validateSpecialChar(NAME.value(), name);
            validateStringLength(NAME.value(), name, 1, 32);
        }
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        return buildSuccess(redPacketService.queryRedPackets(new QueryRedPacketRequest(name, new PagingRequest(pageNumber, pageSize))));
    }

    @ApiOperation("查询红包详情")
    @GetMapping("/{id}")
    @ResponseBody
    public RootResponse<QueryRedPacketDetailResponse> queryRedPacketDetail(
            @ApiParam(name = "id", value = "红包ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        return buildSuccess(redPacketService.queryRedPacketDetail(id));
    }

}