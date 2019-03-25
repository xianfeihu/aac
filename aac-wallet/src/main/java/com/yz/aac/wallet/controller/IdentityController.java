package com.yz.aac.wallet.controller;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.service.IdentityService;

@Api(tags = "身份实名认证")
@RestController
@RequestMapping(value = "/idcard")
public class IdentityController  extends BaseController {
	
	@Autowired
	private IdentityService identityService;

	@ApiOperation("身份号码姓名实名匹配")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "idcard", value = "身份证号码", required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "realname", value = "姓名", required = true, dataType = "String"),
    })
	@GetMapping(value = "/query")
	public RootResponse<IdCardResponse> query(String idcard, String realname) throws Exception{
		
		this.validateRequired("身份证号码", idcard);
		this.validateRequired("身份证姓名", realname);
		
		return buildSuccess(identityService.Authentication(idcard, realname));
	}
	
}
 