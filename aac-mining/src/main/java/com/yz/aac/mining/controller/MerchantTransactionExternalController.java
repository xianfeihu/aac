package com.yz.aac.mining.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.mining.model.request.MerchantCurrencyExchangeRequest;
import com.yz.aac.mining.service.MerchantCurrencyExchangeService;
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
@Api(tags = "商户货币交易中心--对外接口")
@RequestMapping("/external/merchantCurrencyMining")
public class MerchantTransactionExternalController extends BaseController {

	@Autowired
	private MerchantCurrencyExchangeService merchantCurrencyExchangeServiceImpl;

	@ApiOperation("获取挖矿奖励")
	@GetMapping("/rewards")
	public RootResponse<Boolean> getMiningRewards(MerchantCurrencyExchangeRequest request) throws Exception {
		this.validateRequired("用户ID",request.getUserId());
		this.validateRequired("商户ID",request.getMerchantId());
		this.validateBigDecimalRange("挖矿获得的商户币金额",request.getAmount(), BigDecimal.ZERO ,BigDecimal.valueOf(Long.valueOf(PLATFORM_CURRENCY_TOTAL_CIRCULATION.value())));
		return buildSuccess(this.merchantCurrencyExchangeServiceImpl.getMiningRewards(request));
	}

}
