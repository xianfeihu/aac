package com.yz.aac.exchange.service;

import java.math.BigDecimal;
import java.util.List;

import com.yz.aac.exchange.model.PageResult;
import com.yz.aac.exchange.model.request.PlatformCurrencyOrderSubRequest;
import com.yz.aac.exchange.model.request.SalesOrderRequest;
import com.yz.aac.exchange.model.request.UserOrderStatusRequest;
import com.yz.aac.exchange.model.request.UserOrderSubRequest;
import com.yz.aac.exchange.model.response.CurrencyTradingInfoResponse;
import com.yz.aac.exchange.model.response.CurrencyTradingListResponse;
import com.yz.aac.exchange.model.response.MerchantAssertTradeRecordResponse;
import com.yz.aac.exchange.model.response.PlatformCurrencyOrderInfoResponse;
import com.yz.aac.exchange.model.response.PriceFluctuationResponse;


/**
 * APP场外交易所服务
 *
 */
public interface ExchangeService {

	/**
	 * 获取系统所有商户币列表
	 * @param userId 用户ID
	 * @param isContainAAB 是否包含系统AAB货币（1-是<默认> 2-否）
	 * @return
	 * @throws Exception
	 */
	List<String> getCurrencyList(Long userId, Integer isContainAAB) throws Exception;
	
	/**
	 * 获取货币买卖挂单列表
	 * @param currencySymbol 货币符号
	 * @param tradingType 挂单类型
	 * @param pageNo 当前页面
	 * @param pageSize 每页条数
	 * @return
	 * @throws Exception
	 */
	PageResult<CurrencyTradingListResponse> getCurrencyTradingList(String currencySymbol, Integer tradingType, Integer pageNo, Integer pageSize) throws Exception;

	/**
	 * 添加买卖挂单
	 * @param salesOrderRequest 挂单信息
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	boolean addSalesOrder(SalesOrderRequest salesOrderRequest, Long userId) throws Exception;
	
	/**
	 * 获取买卖挂单价格波动信息
	 * @param currencySymbol 货币符号
	 * @param type 挂单类型
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	PriceFluctuationResponse getPriceFluctuationInfo(String currencySymbol, Integer type, Long userId, Long orderId) throws Exception;
	
	/**
	 * 挂单详情
	 * @param currencySymbol 货币符号
	 * @param orderId 挂单ID
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	CurrencyTradingInfoResponse getCurrencyTradingInfo(String currencySymbol, Long orderId, Long userId) throws Exception;

	/**
	 * 平台币支付方式
	 * @param orderId 挂单ID
	 * @param userId 用户ID
	 * @param transactionNum 交易数量
	 * @return
	 * @throws Exception
	 */
	PlatformCurrencyOrderInfoResponse getPlatformCurrencyTradingInfo(Long orderId,Long userId, BigDecimal transactionNum) throws Exception;
	
	/**
	 * 购买平台币
	 * @param orderSubRequest 购买订单信息
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	Boolean submitPlatformCurrencyOrder(PlatformCurrencyOrderSubRequest orderSubRequest, Long userId) throws Exception;
	
	/**
	 * 用户购买商户币
	 * @param userOrderSubRequest 购买订单信息
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	MerchantAssertTradeRecordResponse addUserOrder(UserOrderSubRequest userOrderSubRequest, Long userId) throws Exception;
	
	/**
	 * 获取当前用户买卖挂单列表
	 * @param pageNo 当前页码
	 * @param pageSize 每页条数
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	PageResult<CurrencyTradingListResponse> getMyCurrencyTradingList(Integer pageNo, Integer pageSize, Long userId) throws Exception;
	
	/**
	 * 修改挂单信息
	 * @param salesOrderRequest 挂单信息
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	boolean updateSalesOrder(SalesOrderRequest salesOrderRequest, Long userId) throws Exception;
	
	/**
	 * 修改挂单状态
	 * @param userOrderStatusRequest 挂单信息
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	boolean updateSalesOrderStatus(UserOrderStatusRequest userOrderStatusRequest, Long userId) throws Exception;
}
