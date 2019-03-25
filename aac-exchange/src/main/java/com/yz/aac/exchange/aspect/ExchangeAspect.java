package com.yz.aac.exchange.aspect;

import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.exchange.Constants.*;
import com.yz.aac.exchange.model.request.SalesOrderRequest;
import com.yz.aac.exchange.model.request.UserOrderStatusRequest;
import com.yz.aac.exchange.model.request.UserOrderSubRequest;
import com.yz.aac.exchange.model.response.CurrencyTradingInfoResponse;
import com.yz.aac.exchange.model.response.MerchantAssertTradeRecordResponse;
import com.yz.aac.exchange.model.response.PriceFluctuationResponse;
import com.yz.aac.exchange.repository.*;
import com.yz.aac.exchange.repository.domian.*;
import com.yz.aac.exchange.service.ExchangeService;
import com.yz.aac.exchange.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.yz.aac.exchange.Constants.MerchantTradeRecordTransactionModeEnum.BUY_​​ORDER_CLINCH;
import static com.yz.aac.exchange.Constants.MerchantTradeRecordTransactionModeEnum.REPURCHASE;

/**
 * 交易事件
 *
 */

@Slf4j
@Order(1)
@Component
@Aspect
public class ExchangeAspect {
	
	@Autowired
	private LockMoneyTransactionRepository lockMoneyTransactionRepository;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired
	private UserPaymentErrorRecordRepository userPaymentErrorRecordRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserOrderRepository userOrderRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private MerchantAssertLatestDataRepository latestDataRepository;
	
	@Autowired
	private UserAssertRepository userAssertRepository;
	
	@Autowired
	private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;
	
	@Autowired
	private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;

	@Autowired
	private UserBehaviourStatisticsRepository userBehaviourStatisticsRepository;
	
	@Autowired
	private UserIncomeStatisticsRepository userIncomeStatisticsRepository;
	
	@Autowired
	private MerchantAssertTradeRecordRepository merchantAssertTradeRecordRepository;
	
	@Autowired
	private UserAssertFreezeRepository userAssertFreezeRepository;

	@Autowired
	private MerchantService merchantServiceImpl;
	
	/**
	 * 货币交易数据加锁事件
	 * @param pjp
	 * @param currencyTradingInfoResponse
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(returning = "currencyTradingInfoResponse",pointcut = "execution(* com.yz.aac.exchange.service.impl.ExchangeServiceImpl.getCurrencyTradingInfo(..))")
	@Transactional(rollbackFor = Exception.class)
	public void moneyTransactionLock(JoinPoint pjp, CurrencyTradingInfoResponse currencyTradingInfoResponse) throws Throwable{
		Object[] args = pjp.getArgs();
		Long userId = (Long) args[2];
		
		lockMoneyTransactionRepository.saveLockMoneyTransaction(
				new LockMoneyTransaction(userId, currencyTradingInfoResponse.getOrderId(), currencyTradingInfoResponse.getPrice(), System.currentTimeMillis()));
	}
	
	/**
	 * 挂单信息校验
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Before("execution(* com.yz.aac.exchange.service.impl.ExchangeServiceImpl.addSalesOrder(..))")
	public void addSalesOrder(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		SalesOrderRequest salesOrderRequest = (SalesOrderRequest) args[0];
		Long userId = (Long) args[1];
    	
		salesOrderRequest.setType((null == salesOrderRequest.getType()) ? MerchantTradeType.BUY.code() : salesOrderRequest.getType());
		
		PriceFluctuationResponse dbr = exchangeService.getPriceFluctuationInfo(salesOrderRequest.getCurrencySymbol(), salesOrderRequest.getType(), userId, null);
		
		//额度数量检查
		if (MerchantTradeType.SELL.code().equals(salesOrderRequest.getType())) {
			if (dbr.getCurrencyLimit().compareTo(salesOrderRequest.getAvailableTradeAmount()) == -1) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "可售数量不足！");
			}
			//在商户币受限期不能出售该商户币
			Integer isMerchant = merchantAssertIssuanceRepository.queryCurrencyMerchantId(salesOrderRequest.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userId)) 
					? StateType.OK_STATE.code() : StateType.NO_STATE.code();
					
			MerchantAssertStatistics statistic = merchantAssertStatisticsRepository.queryAssertStatic(salesOrderRequest.getCurrencySymbol(), MerchantAssertStatisticsKey.UNRESTRICTED.name());
			if (isMerchant.equals(StateType.NO_STATE.code()) && (null == statistic || statistic.getValue().compareTo(BigDecimal.valueOf(StateType.NO_STATE.code())) == 0)) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "当前商户币未过受限期不给予出售！");
			}
		} else {
			if (dbr.getCurrencyLimit().compareTo(salesOrderRequest.getAvailableTradeAmount().multiply(salesOrderRequest.getPrice())) == -1) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "可用平台币数量不足！");
			}
		}
		
		//波动范围检查
			
		BigDecimal closingPrice = latestDataRepository.getLatelyClosingPrice(salesOrderRequest.getCurrencySymbol());
		UserOrder uOrder = userOrderRepository.getUserOrderByCurrencySymbol(salesOrderRequest.getCurrencySymbol());
		closingPrice = (null == closingPrice) ? (null == uOrder ? new BigDecimal(0) : uOrder.getAabPrice()) : closingPrice;
				
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.PRICE_FLOATING_RATE.maxCode(), ParamConfigSubclassEnum.PRICE_FLOATING_RATE.minCode(), ParamConfigSubclassEnum.PRICE_FLOATING_RATE.name());
		BigDecimal floatingAmount = closingPrice.multiply(BigDecimal.valueOf(Float.valueOf(paramConfig.getValue().toString()))).divide(BigDecimal.valueOf(100.00), 2, BigDecimal.ROUND_DOWN);
		
		if (closingPrice.compareTo(BigDecimal.ZERO) == 1 && (salesOrderRequest.getPrice().compareTo(closingPrice.subtract(floatingAmount)) == -1 || 
				salesOrderRequest.getPrice().compareTo(closingPrice.add(floatingAmount)) == 1 )) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "单价超出波动范围受限！");
		}
		
		//支付密码正确性和失效性
		paymentPasswordChecking(userId, salesOrderRequest.getPayPassword());
    	
	}
	
	/**
	 * 挂单货币额度校验
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Before("execution(* com.yz.aac.exchange.service.impl.ExchangeServiceImpl.updateSalesOrder(..))")
	public void updateSalesOrder(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		SalesOrderRequest salesOrderRequest = (SalesOrderRequest) args[0];
		Long userId = (Long) args[1];
    	
		salesOrderRequest.setType((null == salesOrderRequest.getType()) ? MerchantTradeType.BUY.code() : salesOrderRequest.getType());
		
		UserOrder userOrder = userOrderRepository.getUserOrderInfoById(salesOrderRequest.getOrderId());
		PriceFluctuationResponse dbr = exchangeService.getPriceFluctuationInfo(userOrder.getCurrencySymbol(), salesOrderRequest.getType(), userId, null);
		//冻结相应资产
		if (MerchantTradeType.SELL.code().equals(salesOrderRequest.getType())) {
			if (dbr.getCurrencyLimit().compareTo(salesOrderRequest.getAvailableTradeAmount().subtract(userOrder.getAvailableTradeAmount())) == -1) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "用户可冻结商户币余额不足！");
			}
			
		} else {
			if (dbr.getCurrencyLimit().compareTo((salesOrderRequest.getAvailableTradeAmount().multiply(salesOrderRequest.getPrice())).subtract(userOrder.getAvailableTradeAmount().multiply(userOrder.getAabPrice()))) == -1) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "用户可冻结平台币不足！");
			}
		}
    	
	}
	
	/**
	 * 买卖货币额度校验
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@Before("execution(* com.yz.aac.exchange.service.impl.ExchangeServiceImpl.addUserOrder(..))")
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
	public void addUserOrder(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		UserOrderSubRequest userOrderSubRequest = (UserOrderSubRequest) args[0];
		Long userId = (Long) args[1];
		
		UserOrder userOrder = userOrderRepository.getUserOrderInfoById(userOrderSubRequest.getOrderId());

		//判断买卖数量是否合理
		if (userOrderSubRequest.getTransactionNum().compareTo(BigDecimal.ZERO) <= 0
				|| userOrderSubRequest.getTransactionNum().compareTo(userOrder.getAvailableTradeAmount()) == 1) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "买卖数量库存不足或数量必须大于零！");
		}
		
		//当前单价锁数据
		LockMoneyTransaction lockMoney = lockMoneyTransactionRepository.getLockMoneyTransaction(userId, userOrderSubRequest.getOrderId());

		//单价*数量 金额必须大于0.01
		if (userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice()).compareTo(BigDecimal.valueOf(0.01)) == -1) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "买卖数量最少" + BigDecimal.valueOf(0.01).divide(lockMoney.getPrice(), 2, BigDecimal.ROUND_DOWN) + "个！");
		}

		BigDecimal currencyLimit = BigDecimal.ZERO;
		if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {
			currencyLimit = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		}  else {
 			if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(userOrder.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userId))) {
				currencyLimit = merchantAssertStatisticsRepository.queryAssertStaticSellRest(userOrder.getCurrencySymbol(), userId, MerchantAssertStatisticsKey.SELL_REST.name());
			} else {
				currencyLimit = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, userOrder.getCurrencySymbol());
			}
		}
		
//		currencyLimit = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, (userOrder.getType() == MerchantTradeType.SELL.code()) ? Misc.PLATFORM_CURRENCY_SYMBOL.value() : userOrder.getCurrencySymbol());
		
		//校验最大买卖数量
		if (MerchantTradeType.SELL.code().equals(userOrder.getType()) && currencyLimit.compareTo(userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice())) == -1) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "当前平台币资金不足！");
		} else if (MerchantTradeType.BUY.code().equals(userOrder.getType()) && currencyLimit.compareTo(userOrderSubRequest.getTransactionNum()) == -1) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "当前货币资金不足！");
		}
		
		//支付密码正确性和失效性
		paymentPasswordChecking(userId, userOrderSubRequest.getPayPassword());
		
		//如果是第一次购买商户币则初始化商户币
		if (MerchantTradeType.SELL.code().equals(userOrder.getType()) && 
				null == userAssertRepository.queryUserAssert(userId, userOrder.getCurrencySymbol())) {
			
			boolean condition = true;
			while(condition){
				try{
					userAssertRepository.saveUserAssert(new UserAssert(userId, userOrder.getCurrencySymbol(),
							new BigDecimal(0), new BigDecimal(0), RandomUtil.genUUID()));
					condition = false;
				} catch(DuplicateKeyException du){
					condition = true;
					log.info("钱包地址重复，重新生成！");
				}
			}
		}
    	
	}
	
	/**
	 * 买卖交易后事件
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(returning = "merchantAssertTradeRecordResponse", pointcut = "execution(* com.yz.aac.exchange.service.impl.ExchangeServiceImpl.addUserOrder(..))")
	@Transactional(rollbackFor = Exception.class)
	public MerchantAssertTradeRecordResponse addUserOrderAfterRun(JoinPoint pjp, MerchantAssertTradeRecordResponse merchantAssertTradeRecordResponse) throws Throwable{
		
		Object[] args = pjp.getArgs();
		UserOrderSubRequest userOrderSubRequest = (UserOrderSubRequest) args[0];
		Long userId = (Long)args[1];
		
		//判断挂单额度如果为0则自动下架
		UserOrder userOrder = userOrderRepository.getUserOrderInfoById(userOrderSubRequest.getOrderId());
		if (userOrder.getAvailableTradeAmount().compareTo(BigDecimal.ZERO) == 0) {
			//下架挂单
			userOrderRepository.updateUserOrderStatusById(userOrderSubRequest.getOrderId(), UserOrderStatus.LOWER_SHELF.code());
		}
		
		/*
		 * 如果当前挂单人为该货币商家则记录货币交易统计
		 */
		Long merchantId = merchantAssertIssuanceRepository.queryCurrencyMerchantId(userOrder.getCurrencySymbol());
		if ((merchantId).equals(userRepository.getMerchantIdByUser(userOrder.getUserId()))) {
			merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.TRADED.name(), BigDecimal.valueOf(1));
			if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_SOLD.name(), userOrderSubRequest.getTransactionNum());
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_REST.name(), userOrderSubRequest.getTransactionNum().multiply(new BigDecimal(-1)));
			} else {
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_SOLD.name(), userOrderSubRequest.getTransactionNum().multiply(new BigDecimal(-1)));
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_REST.name(), userOrderSubRequest.getTransactionNum());
				// 添加针对商户的挂买单成交信息
				this.merchantServiceImpl.addMerchantTradeRecord(new MerchantTradeRecord(
						merchantId,
						userId,
						BUY_​​ORDER_CLINCH.code(),
						BUY_​​ORDER_CLINCH.flowDirectionEnum().code(),
						userOrderSubRequest.getTransactionNum(),
						userOrder.getAabPrice(),
						System.currentTimeMillis(),
						BUY_​​ORDER_CLINCH.addSubtractEnum().code())
				);
			}
		}
		
		/*
		 * 如果当前购买方为货币商家则也要记录货币交易统计
		 */
		if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(userOrder.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userId))) {
			merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.TRADED.name(), BigDecimal.valueOf(1));
			if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_SOLD.name(), userOrderSubRequest.getTransactionNum().multiply(new BigDecimal(-1)));
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_REST.name(), userOrderSubRequest.getTransactionNum());
				// 添加针对商户回购成交信息
				this.merchantServiceImpl.addMerchantTradeRecord(new MerchantTradeRecord(
						merchantId,
						userId,
						REPURCHASE.code(),
						REPURCHASE.flowDirectionEnum().code(),
						userOrderSubRequest.getTransactionNum(),
						userOrder.getAabPrice(),
						System.currentTimeMillis(),
						REPURCHASE.addSubtractEnum().code())
				);
			} else {
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_SOLD.name(), userOrderSubRequest.getTransactionNum());
				merchantAssertStatisticsRepository.updateAssertStatic(userOrder.getCurrencySymbol(), MerchantAssertStatisticsKey.SELL_REST.name(), userOrderSubRequest.getTransactionNum().multiply(new BigDecimal(-1)));
			}
		}
		
		//用户交易行为次数+1
		userBehaviourStatisticsRepository.addBehaviourStatistics(userId, UserBehaviourStatisticsKey.TRADE.name(), 1);
		userBehaviourStatisticsRepository.addBehaviourStatistics(userOrder.getUserId(), UserBehaviourStatisticsKey.TRADE.name(), 1);
		
		//用户收入统计
		if (MerchantTradeType.BUY.code().equals(userOrder.getType())) {
			LockMoneyTransaction lockMoney = lockMoneyTransactionRepository.getLockMoneyTransaction(userId, userOrder.getId());
			userIncomeStatisticsRepository.updateUserIncomeStatistices(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserIncomeType.SELL.name(), userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice()));
		}
		
		//用户可用资产，货币修改
		BigDecimal validBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		BigDecimal merchantBalance = userAssertRepository.getUserAssetsByCurrencySymbol(userId, merchantAssertTradeRecordResponse.getCurrencySymbol());
		BigDecimal merchantValidBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, merchantAssertTradeRecordResponse.getCurrencySymbol());
		
		merchantAssertTradeRecordResponse.setValidBalance(validBalance);
		merchantAssertTradeRecordResponse.setMerchantBalance(merchantBalance);
		merchantAssertTradeRecordResponse.setMerchantValidBalance(merchantValidBalance);
		
		merchantAssertTradeRecordRepository.updateOrtherBalance(merchantAssertTradeRecordResponse.getId(), validBalance, merchantBalance, merchantValidBalance);
		
		//清除为零的冻结资产
		userAssertFreezeRepository.deleteAssetFreeze();
		return merchantAssertTradeRecordResponse;
	}
	
	/**
	 * 支付密码正确性和失效性
	 * @param userId
	 * @param payPasword
	 * @throws BusinessException 
	 */
	private void paymentPasswordChecking(Long userId, String payPasword) throws BusinessException{
	    int errorNum = userPaymentErrorRecordRepository.queryPaymentErrorRecord(userId);
		if (errorNum >= 3) {
			if (null != userPaymentErrorRecordRepository.queryPaymentErrorRecordByTime(userId, 30)) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您输入密码错误过多，请一小时后重试！");
			}
				userPaymentErrorRecordRepository.deletePaymentErrorRecordByUserId(userId);
		} 

        User user = userRepository.getUserById(userId);
        try {
            if (!user.getPaymentPassword().equals(payPasword)) {
                userPaymentErrorRecordRepository.savePaymentErrorRecord(new UserPaymentErrorRecord(userId, System.currentTimeMillis()));
                if (++errorNum >= 3) {
                    throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您输入密码错误过多，请一小时后重试！");
                }
                throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您输入的密码错误，请重新输入");
            }
            userPaymentErrorRecordRepository.deletePaymentErrorRecordByUserId(userId);
        } catch(NullPointerException e){
            throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您未设置支付密码！");
        }
	}
	
	/**
	 * 下架订单取消冻结资产
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.exchange.service.impl.ExchangeServiceImpl.updateSalesOrderStatus(..))")
	@Transactional(rollbackFor = Exception.class)
	public void updateSalesOrderStatus(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		UserOrderStatusRequest userOrderStatusRequest = (UserOrderStatusRequest) args[0];
		Long userId = (Long)args[1];
		
		UserOrder userOrder = userOrderRepository.getUserOrderInfoById(userOrderStatusRequest.getOrderId());
		
		if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {
			
			userAssertFreezeRepository.updateFreezeAmount(
					new UserAssertFreeze(userId, userOrder.getCurrencySymbol(),
							userOrder.getAvailableTradeAmount().multiply(new BigDecimal(-1)),
							UserRreezeReason.SALE_OF_BILLS.code().intValue(), null));
			
		} else {
			
			userAssertFreezeRepository.updateFreezeAmount(
					new UserAssertFreeze(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(),
							userOrder.getAvailableTradeAmount().multiply(new BigDecimal(-1)),
							UserRreezeReason.PURCHASE_LIST.code().intValue(), null));
			
		}

	}
	
}
