package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QuerySignInParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryQuestionParticipatorResponse;
import com.yz.aac.opadmin.model.response.QuerySignInParticipatorResponse;
import com.yz.aac.opadmin.service.SignInService;
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
@RequestMapping(value = "/signIn")
@Slf4j
@Api(tags = "签到（app内容管理-签到奖励）")
public class SignInController extends BaseController {

    @Autowired
    private SignInService signInService;

    @ApiOperation("查询参与用户")
    @GetMapping("/participators")
    @ResponseBody
    public RootResponse<QuerySignInParticipatorResponse> queryParticipators(
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
        return buildSuccess(signInService.queryParticipators(
                new QuerySignInParticipatorRequest(name, code, null, new PagingRequest(pageNumber, pageSize))
        ));
    }

}