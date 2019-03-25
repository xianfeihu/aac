package com.yz.aac.wallet.controller;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.wallet.model.request.AABOrderInfoRequest;
import com.yz.aac.wallet.model.response.AABOrderInfoResponse;
import com.yz.aac.wallet.model.response.MerchantDepositStatusResponse;
import com.yz.aac.wallet.service.MerchantService;

@Api(tags = "APP端-商户押金流程")
@RestController
public class MerchantController extends BaseController {
	
	@Autowired
	private MerchantService merchantService;

	@ApiOperation("获取押金信息")
	@GetMapping("/getdDepositMes")
	public RootResponse<AABOrderInfoResponse> getDepositMes() throws Exception{
		LoginInfo loginInfo = getLoginInfo();
		return buildSuccess(merchantService.getDepositMes(loginInfo.getId()));
	}
	
	@ApiOperation("提交押金订单")
	@PutMapping("/addDepositOrder")
	public RootResponse<AABOrderInfoResponse> addDepositOrder(@RequestBody AABOrderInfoRequest aabOrderInfoRequest) throws Exception{
		return buildSuccess(merchantService.addDepositOrder(aabOrderInfoRequest));
	}
	
	@ApiOperation("查看商户押金状态")
	@GetMapping("/getDepositStatus")
	public RootResponse<MerchantDepositStatusResponse> getDepositStatus() throws Exception{
		LoginInfo loginInfo = getLoginInfo();
		return buildSuccess(merchantService.getDepositStatus(loginInfo.getId()));
	}
	
}
