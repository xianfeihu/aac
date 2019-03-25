package com.yz.aac.exchange.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.exchange.model.request.MerchantCurrencyExchangeRequest;
import com.yz.aac.exchange.model.response.MerchantAndPlatExchangeRateResponse;
import com.yz.aac.exchange.service.MerchantService;
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
@Api(tags = "商户交易中心--对外接口")
@RequestMapping("/external/merchantTransaction")
public class MerchantTransactionExternalController extends BaseController {

	@Autowired
	private MerchantService merchantServiceImpl;

	@ApiOperation("商家交易 --> 用户转币")
	@PostMapping("/transferToUser")
	public RootResponse<Boolean> merchantTransferUser(@RequestBody MerchantCurrencyExchangeRequest request) throws Exception {
		this.validateRequired("商户ID",request.getMerchantId());
		this.validateBigDecimalRange("交易金额",request.getAmount(), BigDecimal.ZERO ,BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_TOTAL_CIRCULATION.value())));
		this.validateIntRange("交易类型",request.getTransactionMode(),1,7);
		return buildSuccess(this.merchantServiceImpl.merchantTransferUser(request,getUserId()));
	}

	@ApiOperation("商户和平台币汇率(商户币-->人民币：[商户币*商户币汇率*平台币汇率])")
	@GetMapping("/exchangeRate")
	@ApiImplicitParam(paramType = "query", name = "currencySymbol", value = "货币符号", required = true, dataType = "String")
	public RootResponse<MerchantAndPlatExchangeRateResponse> exchangeRate(String currencySymbol) throws Exception {
		return buildSuccess(this.merchantServiceImpl.exchangeRate(currencySymbol));
	}

	@ApiOperation("设置盈利额-（本期未进行分红的）")
	@PutMapping("/setProfitAmount")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "amount", value = "盈利额 (RMB)", required = true, dataType = "BigDecimal")
	})
	public RootResponse<Boolean> setProfitAmount(Long merchantId, BigDecimal amount) throws Exception{
		this.validateRequired("商户ID", merchantId);
		this.validateBigDecimalRange("盈利额", amount, BigDecimal.ZERO ,BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_TOTAL_CIRCULATION.value())));
		return buildSuccess(this.merchantServiceImpl.setProfitAmount(merchantId, amount, getUserId()));
	}


	private Long getUserId() {
		return this.getLoginInfo().getId();
	}

}
