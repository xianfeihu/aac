package com.yz.aac.wallet.controller;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.request.MobileVerifyEntityRequest;
import com.yz.aac.wallet.model.response.SmsResponse;
import com.yz.aac.wallet.service.AccountService;
import com.yz.aac.wallet.service.SmsService;
import com.yz.aac.wallet.util.RegularUtil;


@Api(tags = "短信服务")
@RestController
@RequestMapping(value = "/sms")
public class SmsController  extends BaseController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SmsService smsService;
	
	@ApiOperation("发送验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query", name = "mobile", value = "手机号码", required = true, dataType = "String"),
		@ApiImplicitParam(paramType="query", name = "type", value = "验证码类型<1-登录（默认）  2-修改密码  3-商户实名认证 4-小程序同步>", required = false, dataType = "Integer")
	})
	@GetMapping("/sendCode")
	public RootResponse<Boolean> getMobileCode(String mobile, Integer type) throws Exception{
		this.validateRequired("手机号码", mobile);
		if (!RegularUtil.phoneVerification(mobile)) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
		}
		
		return buildSuccess(accountService.getMobileCode(mobile, type));
	}

	@ApiOperation("校验验证码")
	@PostMapping("/verifycode")
	public RootResponse<SmsResponse> verifycode(@RequestBody MobileVerifyEntityRequest mobileVerifyEntity) throws Exception{
		
		this.validateRequired("手机号码", mobileVerifyEntity.getMobile());
		this.validateRequired("验证码", mobileVerifyEntity.getCode());
		
		return buildSuccess(smsService.verifycode(mobileVerifyEntity.getMobile(), mobileVerifyEntity.getCode(), mobileVerifyEntity.getType()));
	}

}
 