package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryInvitationParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryInvitationParticipatorResponse;
import com.yz.aac.opadmin.service.InvitationService;
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
@RequestMapping(value = "/invitation")
@Slf4j
@Api(tags = "邀请（app内容管理-邀请好友）")
public class InvitationController extends BaseController {

    @Autowired
    private InvitationService invitationService;

    @ApiOperation("查询参与用户")
    @GetMapping("/participators")
    @ResponseBody
    public RootResponse<QueryInvitationParticipatorResponse> queryParticipators(
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "code", value = "用户编号") @RequestParam(value = "code", required = false) String code,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        if (StringUtils.isNotBlank(name)) {
            validateSpecialChar(NAME.value(), name);
            validateStringLength(NAME.value(), name, 1, 32);
        }
        if (StringUtils.isNotBlank(code)) {
            validateSpecialChar(CODE.value(), code);
            validateStringLength(CODE.value(), code, 1, 16);
        }
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        return buildSuccess(invitationService.queryParticipators(new QueryInvitationParticipatorRequest(
                name, code, null, new PagingRequest(pageNumber, pageSize)
        )));
    }

}