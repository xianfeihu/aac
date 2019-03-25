package com.yz.aac.wallet.controller;

import com.yz.aac.common.Constants;
import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.request.ExternalRegisterUserRequest;
import com.yz.aac.wallet.service.AccountService;
import com.yz.aac.wallet.service.UserInfoExternalService;
import com.yz.aac.wallet.util.RegularUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_TOTAL_CIRCULATION;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@RestController
@Api(tags = "用户中心后台--对外接口")
@RequestMapping("/external/userInfo")
public class UserInfoExternalController extends BaseController {

	@Autowired
	private AccountService accountServiceImpl;

	@ApiOperation("用户注册（注册-认证-绑定）此接口仅外部服务器可调用->返回userId")
	@PostMapping("/registerUser")
	public RootResponse<Long> registerUser(@RequestBody ExternalRegisterUserRequest registerUserRequest) throws Exception{

		this.validateRequired("商户ID", registerUserRequest.getMerchantId());
		this.validateRequired("姓名", registerUserRequest.getName());
		this.validateRequired("性别", registerUserRequest.getGender());
		this.validateRequired("身份证号码", registerUserRequest.getIdNumber());
		this.validateRequired("手机号码", registerUserRequest.getMobileNumber());
		if (!RegularUtil.phoneVerification(registerUserRequest.getMobileNumber().toString())) {
			throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
		}
		return buildSuccess(this.accountServiceImpl.externalRegisterUser(registerUserRequest, getLoginInfo().getId()));

	}
	
	@Autowired
	private UserInfoExternalService userInfoExternalServiceImpl;

	@ApiOperation("与APP用户同步--返回用户ID")
	@PutMapping("/synchronized")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "mobileNumber", value = "手机号", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "code", value = "验证码", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "balance", value = "同步金额", required = true, dataType = "BigDecimal")
	})
	public RootResponse<Long> synchronizedUserAssetFromExternal(String mobileNumber, String code, Long merchantId, BigDecimal balance) throws Exception{
		if (!RegularUtil.phoneVerification(mobileNumber)) {
			throw new BusinessException(Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "手机号码格式错误！");
		}
		this.validateRequired("验证码",code);
		this.validateRequired("商户ID",merchantId);
		this.validateBigDecimalRange("余额",balance, BigDecimal.ZERO, BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_TOTAL_CIRCULATION.value())));
		return buildSuccess(this.userInfoExternalServiceImpl.synchronizedUserAssetFromExternal(mobileNumber,code,merchantId,balance));
	}

	@ApiOperation("获取用户该商户币的余额")
	@GetMapping("/asset")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "id", value = "用户ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "currencySymbol", value = "货币符号", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "flag", value = "true-总资产（包含冻结资产），false-可用资产", required = true, dataType = "Boolean")
	})
	public RootResponse<BigDecimal> getUserAsset(Long id, String currencySymbol, Boolean flag) throws Exception{
		this.validateRequired("用户ID",id);
		this.validateRequired("货币符号",currencySymbol);
		this.validateRequired("是否包含冻结资产",flag);
		return buildSuccess(this.userInfoExternalServiceImpl.getUserAssetById(id, currencySymbol, flag));
	}

}
