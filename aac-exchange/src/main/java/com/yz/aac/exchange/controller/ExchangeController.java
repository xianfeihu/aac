package com.yz.aac.exchange.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.exchange.aspect.targetCustom.PageControllerAspect;
import com.yz.aac.exchange.model.PageResult;
import com.yz.aac.exchange.model.request.PlatformCurrencyOrderSubRequest;
import com.yz.aac.exchange.model.request.SalesOrderRequest;
import com.yz.aac.exchange.model.request.UserOrderStatusRequest;
import com.yz.aac.exchange.model.request.UserOrderSubRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.service.ExchangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@Api(tags = "交易所-货币买卖")
@RestController
public class ExchangeController extends BaseController {
	
	@Autowired
	private ExchangeService exchangeService;

	@ApiOperation("获取系统货币符号列表")
	@GetMapping("/getCurrencyList")
	@ApiImplicitParam(paramType = "query", name = "isContainAAB", value = "是否包含AAB货币（1-是<默认包含> 2-否）", required = false, dataType = "Integer")
	public RootResponse<List<String>> getCurrencyList(Integer isContainAAB) throws Exception{
		LoginInfo userInfo = getLoginInfo();
		Long userId = (null == userInfo) ? null : userInfo.getId();
		List<String> currencyList = exchangeService.getCurrencyList(userId, isContainAAB);
		
		return buildSuccess(currencyList);
		
	}
	
	@ApiOperation("货币买卖挂单列表")
	@GetMapping("/getCurrencyTradingList")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "currencySymbol", value = "货币符号<默认-AAB>", required = false, dataType = "String"),
		@ApiImplicitParam(paramType = "query", name = "tradingType", value = "买卖数据类型（1-买 2-卖<默认>）", required = false, dataType = "Integer"),
		@ApiImplicitParam(paramType = "query", name = "pageNo", value = "当前页码", required = true, dataType = "Integer"),
		@ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
	})
	@PageControllerAspect
	public RootResponse<PageResult<CurrencyTradingListResponse>> getCurrencyTradingList(HttpServletRequest request, String currencySymbol, Integer tradingType, Integer pageNo, Integer pageSize) throws Exception{
		this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
		
		PageResult<CurrencyTradingListResponse> pageResult = exchangeService.getCurrencyTradingList(currencySymbol, tradingType, pageNo, pageSize);
		return buildSuccess(pageResult);
		
	}
	
	@ApiOperation("获取买卖挂单详情")
	@GetMapping("/getCurrencyTradingInfo")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "currencySymbol", value = "货币符号<默认AAB>", required = false, dataType = "String"),
		@ApiImplicitParam(paramType = "query", name = "orderId", value = "挂单ID", required = true, dataType = "Long")
	})
	public RootResponse<CurrencyTradingInfoResponse> getCurrencyTradingInfo(String currencySymbol, Long orderId) throws Exception{
		this.validateRequired("挂单ID", orderId);
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.getCurrencyTradingInfo(currencySymbol, orderId, userInfo.getId()));
	}
	
	@ApiOperation("获取平台币挂单支付方式详情")
	@GetMapping("/getPlatformCurrencyTradingInfo")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "transactionNum", value = "交易数量", required = true, dataType = "BigDecimal"),
		@ApiImplicitParam(paramType = "query", name = "orderId", value = "挂单ID", required = true, dataType = "Long")
	})
	public RootResponse<PlatformCurrencyOrderInfoResponse> getPlatformCurrencyTradingInfo(BigDecimal transactionNum, Long orderId) throws Exception{
		this.validateRequired("挂单ID", orderId);
		this.validateRequired("交易数量", transactionNum);
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.getPlatformCurrencyTradingInfo(orderId, userInfo.getId(), transactionNum));
	}
	
	@ApiOperation("提交购买平台币订单")
	@PutMapping("/submitPlatformCurrencyOrder")
	public RootResponse<Boolean> submitAABOrder(@RequestBody PlatformCurrencyOrderSubRequest aabOrderSubRequest) throws Exception{
		this.validateRequired("挂单ID", aabOrderSubRequest.getOrderId());
		this.validateRequired("参考号", aabOrderSubRequest.getReferenceNumber());
		this.validateRequired("购买数量", aabOrderSubRequest.getTransactionNum());
		this.validateRequired("订单时间", aabOrderSubRequest.getOrderTime());
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.submitPlatformCurrencyOrder(aabOrderSubRequest, userInfo.getId()));
	}
	
	@ApiOperation("添加买卖单")
	@PutMapping("/addSalesOrder")
	public RootResponse<Boolean> addSalesOrder(@RequestBody SalesOrderRequest salesOrderRequest) throws Exception{
		this.validateRequired("出售额度", salesOrderRequest.getAvailableTradeAmount());
		this.validateRequired("货币符号", salesOrderRequest.getCurrencySymbol());
		this.validateRequired("单价", salesOrderRequest.getPrice());
		this.validateRequired("支付密码", salesOrderRequest.getPayPassword());
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.addSalesOrder(salesOrderRequest, userInfo.getId()));
	}
	
	@ApiOperation("买卖挂单价格波动信息<只针对商户币>")
	@GetMapping("/getPriceFluctuationInfo")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "currencySymbol", value = "货币符号", required = true, dataType = "String"),
		@ApiImplicitParam(paramType = "query", name = "type", value = "发布类型(1-买单<默认> 2-卖单)", required = false, dataType = "Integer"),
		@ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = false, dataType = "Long")
	})
	public RootResponse<PriceFluctuationResponse> getPriceFluctuationInfo(String currencySymbol, Integer type, Long orderId) throws Exception{
		this.validateRequired("货币符号", currencySymbol);
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.getPriceFluctuationInfo(currencySymbol, type, userInfo.getId(), orderId));
	}
	
	@ApiOperation("提交用户交易订单")
	@PutMapping("/submitUserOrder")
	public RootResponse<MerchantAssertTradeRecordResponse> submitUserOrder(@RequestBody UserOrderSubRequest userOrderSubRequest) throws Exception{
		this.validateRequired("挂单ID", userOrderSubRequest.getOrderId());
		this.validateRequired("交易数量", userOrderSubRequest.getTransactionNum());
		this.validateRequired("支付密码", userOrderSubRequest.getPayPassword());
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.addUserOrder(userOrderSubRequest,userInfo.getId()));
	}
	
	@ApiOperation("当前用户买卖挂单列表")
	@GetMapping("/getMyCurrencyTradingList")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "pageNo", value = "当前页码", required = true, dataType = "Integer"),
		@ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
	})
	@PageControllerAspect
	public RootResponse<PageResult<CurrencyTradingListResponse>> getMyCurrencyTradingList(HttpServletRequest request, Integer pageNo, Integer pageSize) throws Exception{
		this.validateRequired("当前页码", pageNo);
		this.validateRequired("每页条数", pageSize);
		
		LoginInfo userInfo = getLoginInfo();
		
		PageResult<CurrencyTradingListResponse> pageResult = exchangeService.getMyCurrencyTradingList(pageNo, pageSize, userInfo.getId());
		return buildSuccess(pageResult);
		
	}
	
	@ApiOperation("修改买卖单")
	@PostMapping("/updateSalesOrder")
	public RootResponse<Boolean> updateSalesOrder(@RequestBody SalesOrderRequest salesOrderRequest) throws Exception{
		this.validateRequired("挂单ID", salesOrderRequest.getOrderId());
		this.validateRequired("出售额度", salesOrderRequest.getAvailableTradeAmount());
		this.validateRequired("货币符号", salesOrderRequest.getCurrencySymbol());
		this.validateRequired("单价", salesOrderRequest.getPrice());
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.updateSalesOrder(salesOrderRequest, userInfo.getId()));
	}
	
	@ApiOperation("修改买卖单状态")
	@PostMapping("/updateSalesOrderStatus")
	public RootResponse<Boolean> updateSalesOrderStatus(@RequestBody UserOrderStatusRequest userOrderStatusRequest) throws Exception{
		this.validateRequired("挂单ID", userOrderStatusRequest.getOrderId());
		
		LoginInfo userInfo = getLoginInfo();
		
		return buildSuccess(exchangeService.updateSalesOrderStatus(userOrderStatusRequest, userInfo.getId()));
	}
}
