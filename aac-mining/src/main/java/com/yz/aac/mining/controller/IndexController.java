package com.yz.aac.mining.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.mining.aspect.targetCustom.PageControllerAspect;
import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.GrowthRequest;
import com.yz.aac.mining.model.request.WechatCheckCodeRequest;
import com.yz.aac.mining.model.response.*;
import com.yz.aac.mining.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@Api(tags = "首页-展示")
@RequestMapping("/index")
@RestController
public class IndexController extends BaseController {
	
	@Autowired
	private AccountService accountService;
	
	@ApiOperation("我的首页数据")
	@GetMapping("/getMyHomeInfo")
	public RootResponse<HomeInfoResponse> getMyHomeInfo() throws Exception{
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();
		
		return buildSuccess(accountService.getMyHomeInfo(userId));
		
	}
	
	@ApiOperation("收集能量")
	@GetMapping("/collectingEnergy")
	public RootResponse<String> collectingEnergy(GrowthRequest growthRequest) throws Exception{
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();

		synchronized (userId){
			return buildSuccess(accountService.collectingEnergy(growthRequest, userId));
		}
		
	}

	@ApiOperation("元力详情")
	@GetMapping("/getPowerPointInfo")
	public RootResponse<PowerPointInfoResponse> getPowerPointInfo() throws Exception{
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();
		
		return buildSuccess(accountService.getPowerPointInfo(userId));
		
	}
	
	@ApiOperation("挖矿记录列表")
	@GetMapping("/getMiningRecordList")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "bonusType", value = "奖励类型（1-平台币 2-元力）", required = false, dataType = "Integer"),
		@ApiImplicitParam(paramType = "query", name = "pageNo", value = "当前页码", required = true, dataType = "Integer"),
		@ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
	})
	@PageControllerAspect
	public RootResponse<PageResult<MiningRecordResponse>> getMiningRecordList(HttpServletRequest request,Integer bonusType, Integer pageNo, Integer pageSize) throws Exception{
		this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
		
		LoginInfo userInfo = getLoginInfo();
		
		PageResult<MiningRecordResponse> pageResult = accountService.getMiningRecordList(pageNo, pageSize, userInfo.getId(), bonusType);
		return buildSuccess(pageResult);
	}
	
	@ApiOperation("平台币详情")
	@GetMapping("/getPlatformCurrencyInfo")
	public RootResponse<PlatformCurrencyInfoResponse> getPlatformCurrencyInfo() throws Exception{
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();
		
		return buildSuccess(accountService.getPlatformCurrencyInfo(userId));
		
	}
	
	@ApiOperation("当前用户等级详情")
	@GetMapping("/getLevelInfo")
	public RootResponse<LevelInfoResponse> getLevelInfo() throws Exception{
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();
		
		return buildSuccess(accountService.getLevelInfo(userId));
		
	}
	
	@ApiOperation("关注微信公众号详情")
	@GetMapping("/getFocusOnWechatInfo")
	public RootResponse<FocusOnWechatInfoResponse> getFocusOnWechatInfo() throws Exception{
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();
		
		return buildSuccess(accountService.getFocusOnWechatInfo(userId));
		
	}
	
	@ApiOperation("校验微信公众号验证码")
	@PostMapping("/wechatCheckCode")
	public RootResponse<Boolean> wechatCheckCode(@RequestBody WechatCheckCodeRequest wechatCheckCodeRequest) throws Exception{
		this.validateRequired("微信验证码", wechatCheckCodeRequest.getCode());
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
		return buildSuccess(accountService.wechatCheckCode(wechatCheckCodeRequest, userId));
		
	}
	
	@ApiOperation("签到")
	@PostMapping("/signInPoint")
	public RootResponse<SignInPointResponse> signInPoint() throws Exception{
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
		return buildSuccess(accountService.signInPoint(userId));
		
	}
	
	@ApiOperation("邀请好友-获取分享参数")
	@PostMapping("/getInviteFriendsParam")
	public RootResponse<InviteFriendsParamResponse> getInviteFriendsParam() throws Exception{
		
		return buildSuccess(accountService.getInviteFriendsParam());
		
	}
	
}
