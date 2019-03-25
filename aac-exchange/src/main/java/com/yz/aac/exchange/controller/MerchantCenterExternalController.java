package com.yz.aac.exchange.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@RestController
@Api(tags = "商户中心后台--对外接口")
@RequestMapping("/external/merchantCenter")
public class MerchantCenterExternalController extends BaseController {
	
	@Autowired
	private MerchantService merchantServiceImpl;

	@ApiOperation("商户货币统计信息")
	@GetMapping("/merchantCurrencyStatistics")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "assetType", value = "1、活跃存量 2、冻结存量 3、挖矿存量", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType = "query", name = "countType", value = "1、昨日 2、上周 3、上月", required = true, dataType = "Integer")
	})
	public RootResponse<MerchantCurrencyStatisticsResponse> getMerchantCurrencyStatistics(Long merchantId, Integer assetType, Integer countType) throws Exception{
		this.validateRequired("商户ID",merchantId);
		this.validateIntRange("资产类型", assetType,1,2);
		this.validateIntRange("统计类型", countType,1,3);
		return buildSuccess(this.merchantServiceImpl.getMerchantCurrencyStatistics(merchantId, assetType, countType));
	}

	@ApiOperation("获取账户活跃存量-增长、流失明细")
	@GetMapping("/merchantActiveStockByAddOrSubtract")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageNo", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType = "query", name = "addOrSubtract", value = "1、流失明细 2、增长明细", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType = "query", name = "flag", value = "是否查询该明细总量", required = true, dataType = "Boolean")
	})
	public RootResponse<MerchantActiveStockResponse> getMerchantActiveStockByAddOrSubtract(Long merchantId, Integer pageNo, Integer pageSize, Integer addOrSubtract, Boolean flag) throws Exception{
		this.validateRequired("商户ID",merchantId);
		this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
		this.validateIntRange("每页条数", addOrSubtract,1,2);
		this.validateRequired("是否查询该明细总量", flag);
		return buildSuccess(this.merchantServiceImpl.getMerchantActiveStockByAddOrSubtract(merchantId, pageNo, pageSize, addOrSubtract, flag));
	}

	@ApiOperation("获取冻结卖单明细")
	@GetMapping("/merchantOrderFreezeStock")
	@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long")
	public RootResponse<List<MerchantFreezeStockResponse>> getMerchantOrderFreezeStock(Long merchantId) throws Exception{
		this.validateRequired("商户ID",merchantId);
		return buildSuccess(this.merchantServiceImpl.getMerchantOrderFreezeStock(merchantId));
	}

	@ApiOperation("获取冻结卖单详情")
	@GetMapping("/merchantOrderFreezeStockDetails")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long"),
		@ApiImplicitParam(paramType = "query", name = "id", value = "卖单号", required = true, dataType = "Long")
	})
	public RootResponse<List<MerchantFreezeStockDetailsResponse>> getMerchantOrderFreezeStockDetails(Long merchantId, Long id) throws Exception{
		this.validateRequired("商户ID",merchantId);
		this.validateRequired("卖单号",id);
		return buildSuccess(this.merchantServiceImpl.getMerchantOrderFreezeStockDetails(merchantId, id));
	}

	@ApiOperation("平台币交易明细")
	@GetMapping("/platTradeRecord")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "merchantId", value = "商户ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageNo", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
			@ApiImplicitParam(paramType = "query", name = "flag", value = "true-包含分红信息，false-仅查明细信息", required = true, dataType = "Boolean")
	})
	public RootResponse<PlatStockResponse> getPlatTradeRecord(Long merchantId, Integer pageNo, Integer pageSize, Boolean flag) throws Exception{
		this.validateRequired("商户ID",merchantId);
		this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
		this.validateRequired("返回数据包含分红信息", flag);
		return buildSuccess(this.merchantServiceImpl.getPlatTradeRecord(merchantId, pageNo, pageSize, flag));
	}

}
