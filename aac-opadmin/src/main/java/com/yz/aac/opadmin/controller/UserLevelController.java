package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.ImageUtil;
import com.yz.aac.opadmin.model.request.*;
import com.yz.aac.opadmin.model.response.QueryUserLevelResponse;
import com.yz.aac.opadmin.service.UserLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_INVALID_IMAGE;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/user/levels")
@Slf4j
@Api(tags = "用户等级（系统管理-等级设置）")
public class UserLevelController extends BaseController {

    @Autowired
    private UserLevelService userLevelService;

    @ApiOperation("查询等级")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryUserLevelResponse> queryLevels(
            @ApiParam(name = "name", value = "等级名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        validateSpecialChar(NAME.value(), name);
        QueryUserLevelRequest request = new QueryUserLevelRequest(name, new PagingRequest(pageNumber, pageSize));
        return buildSuccess(userLevelService.queryUserLevels(request));
    }

    @ApiOperation(value = "创建等级")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RootResponse<?> createLevel(
            @ApiParam(name = "name", value = "等级名称", required = true) @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "matchCondition", value = "满足该等级所须持有的平台币数量", required = true) @RequestParam(value = "matchCondition", required = false) Double matchCondition,
            @ApiParam(name = "icon", value = "图标", required = true) @RequestParam(value = "icon", required = false) MultipartFile icon
    ) throws Exception {
        validateRequired(NAME.value(), name);
        validateRequired(MATCH_CONDITION.value(), matchCondition);
        validateRequired(ICON.value(), icon);
        validateStringLength(NAME.value(), name, 1, 32);
        validateDoubleRange(MATCH_CONDITION.value(), matchCondition, 0D, 9999999.99);
        validateSpecialChar(NAME.value(), name);
        if (!ImageUtil.isImage(icon.getInputStream())) {
            throw new BusinessException(MSG_INVALID_IMAGE.code(), MSG_INVALID_IMAGE.message().replaceFirst(PLACEHOLDER.value(), ICON.value()));
        }
        userLevelService.createUserLevel(new CreateUserLevelRequest(name, matchCondition, UploadRequest.createOrNull(icon)));
        return buildSuccess(null);
    }

    @ApiOperation("更新等级")
    @PostMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RootResponse<?> updateLevel(
            @ApiParam(name = "id", value = "等级ID", required = true) @PathVariable(value = "id") Long id,
            @ApiParam(name = "name", value = "等级名称", required = true) @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "matchCondition", value = "满足该等级所须持有的平台币数量", required = true) @RequestParam(value = "matchCondition", required = false) Double matchCondition,
            @ApiParam(name = "updateIcon", value = "是否更新图标", required = true) @RequestParam(value = "updateIcon", required = false) Boolean updateIcon,
            @ApiParam(name = "icon", value = "图标（updateIcon=true时，必须输入）") @RequestParam(value = "icon", required = false) MultipartFile icon
    ) throws Exception {
        validateRequired(NAME.value(), name);
        validateRequired(MATCH_CONDITION.value(), matchCondition);
        validateRequired(UPDATE_ICON.value(), updateIcon);
        validateStringLength(NAME.value(), name, 1, 32);
        validateDoubleRange(MATCH_CONDITION.value(), matchCondition, 0D, 9999999.99);
        if (updateIcon) {
            validateRequired(ICON.value(), icon);
            if (!ImageUtil.isImage(icon.getInputStream())) {
                throw new BusinessException(MSG_INVALID_IMAGE.code(), MSG_INVALID_IMAGE.message().replaceFirst(PLACEHOLDER.value(), ICON.value()));
            }
        }
        validateSpecialChar(NAME.value(), name);
        userLevelService.updateUserLevel(new UpdateUserLevelRequest(id, name, matchCondition, updateIcon, UploadRequest.createOrNull(icon)));
        return buildSuccess(null);
    }

}