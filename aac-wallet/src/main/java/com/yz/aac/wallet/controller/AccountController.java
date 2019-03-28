package com.yz.aac.wallet.controller;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.config.SecurityConfig;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.EncryptionUtil;
import com.yz.aac.wallet.model.request.IdCardMesRequest;
import com.yz.aac.wallet.model.request.LoginMesRequest;
import com.yz.aac.wallet.model.response.IdCardLoginResponse;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.model.response.UserInfoResponse;
import com.yz.aac.wallet.service.AccountService;
import com.yz.aac.wallet.util.RegularUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.yz.aac.common.Constants.Token.TOKEN_CLAIM_LOGIN_ID;
import static com.yz.aac.common.Constants.Token.TTL_MILLIS;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@Api(tags = "APP端-用户登录，验证流程1232")
@RequestMapping("/account")
@RestController
public class AccountController extends BaseController {
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private SecurityConfig securityConfig;

	@ApiOperation("手机短信登录")
	@PostMapping("/SMSLogin")
	public RootResponse<UserInfoResponse> SMSLogin(@RequestBody LoginMesRequest loginMesRequest) throws Exception{
		
		this.validateRequired("手机号码", loginMesRequest.getMobile());
		this.validateRequired("验证码", loginMesRequest.getCode());
		if (!RegularUtil.phoneVerification(loginMesRequest.getMobile())) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
		}
		UserInfoResponse userInfoResponse = accountService.SMSLogin(loginMesRequest);
		userInfoResponse.setToken(genTokenInHeader(userInfoResponse));
		userInfoResponse.setId(null);
		
		return buildSuccess(userInfoResponse);
		
	}
	
	@ApiOperation("出生日期登录")
	@ApiImplicitParam(paramType="query", name = "idCard", value = "出生年月（19900101）", required = true, dataType = "String")
	@GetMapping("/idCardLogin")
	public RootResponse<IdCardLoginResponse> idCardLogin(String idCard) throws Exception{
		
		this.validateRequired("出生日期", idCard);
		
		LoginInfo loginInfo = getLoginInfo();
		return buildSuccess(accountService.idCardLogin(idCard, loginInfo.getId()));
		
	}
	
	@ApiOperation("获取手机验证码")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query", name = "mobile", value = "手机号码", required = true, dataType = "String"),
		@ApiImplicitParam(paramType="query", name = "type", value = "验证码类型<1-登录（默认）  2-修改密码  3-商户实名认证 4-小程序>", required = false, dataType = "String")
	})
	@GetMapping("/getMobileCode")
	public RootResponse<Boolean> getMobileCode(String mobile, Integer type) throws Exception{
		this.validateRequired("手机号码", mobile);
		if (!RegularUtil.phoneVerification(mobile)) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
		}
		
		return buildSuccess(accountService.getMobileCode(mobile, type));
	}
	
	@ApiOperation("实名认证")
	@PostMapping("/certification/idcard")
	public RootResponse<IdCardResponse> queryCertification(@RequestBody IdCardMesRequest idCardMesRequest) throws Exception{
		
		this.validateRequired("身份证号码", idCardMesRequest.getIdcard());
		this.validateStringLength("身份证号码", idCardMesRequest.getIdcard(), 1, 18);
		this.validateRequired("真实姓名", idCardMesRequest.getRealname());
		this.validateRequired("性别", idCardMesRequest.getGender());
		
		LoginInfo loginInfo = getLoginInfo();
		
		return buildSuccess(accountService.idCardAuth(idCardMesRequest, loginInfo.getId()));
	}
	
	private String genTokenInHeader(UserInfoResponse response) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_CLAIM_LOGIN_ID.value(), response.getId());
        return EncryptionUtil.createToken(claims, Long.parseLong(TTL_MILLIS.value()), securityConfig.getTokenSecurityKey());
//        getResponse().addHeader(TOKEN_KEY.value(), token);
    }
	
}
