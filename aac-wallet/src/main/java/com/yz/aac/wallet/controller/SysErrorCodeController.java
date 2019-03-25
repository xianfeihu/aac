package com.yz.aac.wallet.controller;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.response.ErrorCodeListResponse;

@Api(tags = "系统返回错误参考码")
@RestController
public class SysErrorCodeController extends BaseController {
	
	@ApiOperation("获取错误参考码列表")
	@GetMapping("/getSysErrorCodeList")
	public RootResponse<ErrorCodeListResponse> getSysErrorCodeList() throws Exception{
		return buildSuccess(new ErrorCodeListResponse());
	}
}
