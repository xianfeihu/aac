package com.yz.aac.exchange.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.exchange.Constants.*;
import com.yz.aac.exchange.model.PageResult;
import com.yz.aac.exchange.model.request.PlatformCurrencyOrderSubRequest;
import com.yz.aac.exchange.model.request.SalesOrderRequest;
import com.yz.aac.exchange.model.request.UserOrderStatusRequest;
import com.yz.aac.exchange.model.request.UserOrderSubRequest;
import com.yz.aac.exchange.model.response.*;
import com.yz.aac.exchange.repository.*;
import com.yz.aac.exchange.repository.domian.*;
import com.yz.aac.exchange.service.ExchangeService;
import com.yz.aac.exchange.service.MerchantAssertLatestDataService;
import com.yz.aac.exchange.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.exchange.Constants.MerchantTradeRecordTransactionModeEnum.CANCEL_SELL_​​ORDER;
import static com.yz.aac.exchange.Constants.MerchantTradeRecordTransactionModeEnum.RELEASE_SELL_​​ORDER;
import static com.yz.aac.exchange.Constants.UserOrderStatus.LOWER_SHELF;

@Slf4j
@Service
public class ExchangeServiceImpl implements ExchangeService{

	@Autowired
	private MerchantService merchantServiceImpl;
	
	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private PlatformAssertSellingOrderRepository platformAssertSellingOrderRepository;
	
	@Autowired
	private UserOrderRepository userOrderRepository;
	
	@Autowired
	private MerchantAssertLatestDataRepository latestDataRepository;
	
	@Autowired
	private UserAssertRepository userAssertRepository;
	
	@Autowired
	private MerchantAssertTradeRecordRepository merchantAssertTradeRecordRepository;
	
	@Autowired
	private UserAssertFreezeRepository userAssertFreezeRepository;
	
	@Autowired
	private PlatformAssertSellerRepository platformAssertSellerRepository;
	
	@Autowired
	private OrderReferenceNumberRepository orderReferenceNumberRepository;
	
	@Autowired
    private FileStorageHandler fileStorageHandler;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;
	
	@Autowired
	private MerchantAssertLatestDataService latestDataService;
	
	@Autowired
	private PlatformServiceChargeStrategyRepository platformServiceChargeStrategyRepository;
	
	@Autowired
	private PlatformAssertIncomeExpenditureRecordRepository platformAssertIncomeExpenditureRecordRepository;
	
	@Autowired
	private LockMoneyTransactionRepository lockMoneyTransactionRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private MerchantAssertIssuanceRepository merchantAssertIssuanceRepository;
	
	@Autowired
	private MerchantAssertStatisticsRepository merchantAssertStatisticsRepository;
	
	@Override
	public List<String> getCurrencyList(Long userId, Integer isContainAAB) throws Exception {
		//系统所有商币列表
		List<String> currencyList = merchantRepository.getCurrencyList(userId,
				new Integer[]{IssuanceAuditStatus.DEPOSIT_YES.code()},
				new Integer[]{MerchantTradeType.BUY.code(), MerchantTradeType.SELL.code()});
		if (null == isContainAAB || isContainAAB == StateType.OK_STATE.code()) {
			if (null != currencyList && !currencyList.isEmpty()) {
				LinkedList<String> cList = new LinkedList<String>(currencyList);
				cList.addFirst(Misc.PLATFORM_CURRENCY_SYMBOL.value());
				currencyList = cList;
			} else {
				currencyList = new ArrayList<String>();
				currencyList.add(Misc.PLATFORM_CURRENCY_SYMBOL.value());
			}
		}
		return currencyList;
	}
	
	@Override
	public PageResult<CurrencyTradingListResponse> getCurrencyTradingList(String currencySymbol, Integer tradingType, Integer pageNo, Integer pageSize) throws Exception {
		List<CurrencyTradingListResponse> ctList = new ArrayList<CurrencyTradingListResponse>();
		
		currencySymbol = StringUtil.isEmpty(currencySymbol) ? Misc.PLATFORM_CURRENCY_SYMBOL.value() : currencySymbol;
		tradingType = null== tradingType ? MerchantTradeType.SELL.code() : tradingType;
		
		Page<CurrencyTradingListResponse> page = PageHelper.startPage(pageNo, pageSize, true);
        
		//出售列表分为商户币和系统币数据
		if (Misc.PLATFORM_CURRENCY_SYMBOL.value().equals(currencySymbol)) {
			ctList = platformAssertSellingOrderRepository.getPlatformAssertSellerOrderList().stream()
	                .map(x -> new CurrencyTradingListResponse(x.getPersonName(), x.getNumber(), x.getPrice(), x.getOrderId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(), MerchantTradeType.SELL.code()))
	                .collect(Collectors.toList());
		} else {
			ctList = userOrderRepository.getUserOrderList(currencySymbol, tradingType, UserOrderStatus.UPPER_SHELF.code(), tradingType == MerchantTradeType.SELL.code() ? "ASC" : "DESC");
		}
		
		return new PageResult<CurrencyTradingListResponse>(pageNo, pageSize, page.getTotal(), page.getPages(), ctList);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
	public boolean addSalesOrder(SalesOrderRequest salesOrderRequest, Long userId) throws Exception {
		salesOrderRequest.setType((null == salesOrderRequest.getType()) ? MerchantTradeType.BUY.code() : salesOrderRequest.getType());
		Long nowTime = System.currentTimeMillis();
		UserAssertFreeze assetFreeze = null;
		//冻结相应资产
		if (MerchantTradeType.SELL.code().equals(salesOrderRequest.getType())) {
//			userAssertRepository.addUserAssertBalance(userId, salesOrderRequest.getCurrencySymbol(), salesOrderRequest.getAvailableTradeAmount().divide(new BigDecimal(-1)));
			assetFreeze = new UserAssertFreeze(userId, salesOrderRequest.getCurrencySymbol(), salesOrderRequest.getAvailableTradeAmount(),
					(UserRreezeReason.SALE_OF_BILLS.code()).intValue(), nowTime);
			
			if (null != userAssertFreezeRepository.getAssetFreezeByUserId(userId, salesOrderRequest.getCurrencySymbol(), UserRreezeReason.SALE_OF_BILLS.code())) {
				userAssertFreezeRepository.updateFreezeAmount(assetFreeze);
			} else {
				userAssertFreezeRepository.saveUserAssertFreeze(assetFreeze);
			}

			// 添加针对商户的挂卖单信息
			this.merchantServiceImpl.addMerchantTradeRecord(new MerchantTradeRecord(
					this.merchantAssertIssuanceRepository.queryCurrencyMerchantId(salesOrderRequest.getCurrencySymbol()),
					userId,
					RELEASE_SELL_​​ORDER.code(),
					RELEASE_SELL_​​ORDER.flowDirectionEnum().code(),
					salesOrderRequest.getAvailableTradeAmount(),
					salesOrderRequest.getPrice(),
					nowTime,
					RELEASE_SELL_​​ORDER.addSubtractEnum().code())

			);

			if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(salesOrderRequest.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userId))) {

			}
		} else {
			
//			userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), salesOrderRequest.getAvailableTradeAmount().divide(new BigDecimal(-1)));
			assetFreeze = new UserAssertFreeze(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), salesOrderRequest.getAvailableTradeAmount().multiply(salesOrderRequest.getPrice()),
					(UserRreezeReason.PURCHASE_LIST.code()).intValue(), nowTime);
			
			if (null != userAssertFreezeRepository.getAssetFreezeByUserId(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), UserRreezeReason.PURCHASE_LIST.code())) {
				userAssertFreezeRepository.updateFreezeAmount(assetFreeze);
			} else {
				userAssertFreezeRepository.saveUserAssertFreeze(assetFreeze);
			}
		}
		
		//生成挂单
		userOrderRepository.saveUserOrder(
				new UserOrder(userId, salesOrderRequest.getAvailableTradeAmount(), salesOrderRequest.getAvailableTradeAmount(), 
						salesOrderRequest.getCurrencySymbol(), salesOrderRequest.getPrice(), 
						salesOrderRequest.getRemark(), salesOrderRequest.getType(), 
								UserOrderStatus.UPPER_SHELF.code(), nowTime));
		return true;
	}
	
	@Override
	public PriceFluctuationResponse getPriceFluctuationInfo(String currencySymbol, Integer type, Long userId, Long orderId) throws Exception {
		
		//判断商家是否有挂单（商家资金挂卖单不用判断）
		if (MerchantTradeType.BUY.code().equals(type) || (MerchantTradeType.SELL.code().equals(type) && !merchantAssertIssuanceRepository.queryCurrencyMerchantId(currencySymbol).equals(userRepository.getMerchantIdByUser(userId)))) {
			Merchant merchant = merchantRepository.getMerchantByCurrencySymbol(currencySymbol);
			User muser = userRepository.getUserByMobile(merchant.getMobileNumber());
			List<CurrencyTradingListResponse> ctList = userOrderRepository.getMyOrderList(muser.getId(), new Integer[]{UserOrderStatus.UPPER_SHELF.code(), LOWER_SHELF.code()});
			if (null == ctList || ctList.size() == 0) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "商家暂未进行出售，敬请期待！");	
			}
		}
		
		type = (null == type) ? MerchantTradeType.BUY.code() : type;
		//昨日收盘价->历史收盘价->商户第一次挂单单价
		BigDecimal closingPrice = latestDataRepository.getLatelyClosingPrice(currencySymbol);
		UserOrder uOrder = userOrderRepository.getUserOrderByCurrencySymbol(currencySymbol);
		closingPrice = (null == closingPrice) ? (null == uOrder ? new BigDecimal(0) : uOrder.getAabPrice()) : closingPrice;
		
		//浮动率
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.PRICE_FLOATING_RATE.maxCode(), ParamConfigSubclassEnum.PRICE_FLOATING_RATE.minCode(), ParamConfigSubclassEnum.PRICE_FLOATING_RATE.name());
		
		BigDecimal currencyLimit = BigDecimal.ZERO;
		Integer isMerchant = StateType.NO_STATE.code();
		BigDecimal tradeChargeRate = BigDecimal.ZERO;
		
		if (MerchantTradeType.SELL.code().equals(type)) {
			if (null == orderId) {//修改挂单不用判断
				if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(currencySymbol).equals(userRepository.getMerchantIdByUser(userId))) {
					currencyLimit = merchantAssertStatisticsRepository.queryAssertStaticSellRest(currencySymbol, userId, MerchantAssertStatisticsKey.SELL_REST.name());
				} else {
					currencyLimit = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, currencySymbol);
				}
				if (null == currencyLimit || currencyLimit.compareTo(BigDecimal.ZERO) == 0) {
					throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您无该商家币，无法出售！");
				}
			}
			
			//判断是否可挂卖单
			BigDecimal price = merchantAssertTradeRecordRepository.getMchLastPlatformPrice(currencySymbol);
			isMerchant = merchantAssertIssuanceRepository.queryCurrencyMerchantId(currencySymbol).equals(userRepository.getMerchantIdByUser(userId)) 
							? StateType.OK_STATE.code() : StateType.NO_STATE.code();
			if (isMerchant == StateType.NO_STATE.code() && null == price) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "当前货币暂时不支持用户挂单！");
			}
			
			//卖单判断当前账号是否当前货币商家
			if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(currencySymbol).equals(userRepository.getMerchantIdByUser(userId))) {
				BigDecimal serviceCharge = platformServiceChargeStrategyRepository.getByMerchantMobile(userRepository.getUserById(userId).getMobileNumber()).getTradeChargeRate();
				tradeChargeRate = (null == serviceCharge) ? BigDecimal.ZERO : serviceCharge;
			}
			
		} else {
			
			currencyLimit = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		}
		
		PriceFluctuationResponse pfr = new PriceFluctuationResponse(closingPrice, Float.valueOf(paramConfig.getValue().toString()),
				(null == currencyLimit) ? BigDecimal.valueOf(0.00) : currencyLimit, 
						closingPrice.subtract(closingPrice.multiply(BigDecimal.valueOf(Float.valueOf(paramConfig.getValue().toString()))).divide(BigDecimal.valueOf(100.00), 2, BigDecimal.ROUND_DOWN)), 
						closingPrice.add(closingPrice.multiply(BigDecimal.valueOf(Float.valueOf(paramConfig.getValue().toString()))).divide(BigDecimal.valueOf(100.00), 2, BigDecimal.ROUND_DOWN)),
						isMerchant, tradeChargeRate);
		if (null != orderId) {
			UserOrder userOrder = userOrderRepository.getUserOrderInfoById(orderId);
			pfr.setOrderId(userOrder.getId());
			pfr.setPrice(userOrder.getAabPrice());
			pfr.setAmount(userOrder.getAvailableTradeAmount());
			pfr.setRemark(userOrder.getRemark());
		}
		
		return pfr;
	}
	
	@Override
	public CurrencyTradingInfoResponse getCurrencyTradingInfo(String currencySymbol, Long orderId, Long userId) throws Exception {
		CurrencyTradingInfoResponse ctir = null;
		currencySymbol = StringUtil.isEmpty(currencySymbol) ? Misc.PLATFORM_CURRENCY_SYMBOL.value() : currencySymbol;
		
		if (Misc.PLATFORM_CURRENCY_SYMBOL.value().equals(currencySymbol)) {
			PlatformAssertSellingOrder paso = platformAssertSellingOrderRepository.getSellerOrderInfoById(orderId);
			if (null == paso) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "该挂单已经下架，请刷新！");
			}
			
			if (paso.getAvailableTradeAmount().compareTo(paso.getMinAmountLimit()) == -1) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "该挂单已经下架，请刷新！");
			}
			
			PlatformAssertSeller assetSeller = platformAssertSellerRepository.getSellerById(paso.getSellerId());
			ctir = new CurrencyTradingInfoResponse(paso.getId(), paso.getSellerId(), assetSeller.getName(), paso.getAvailableTradeAmount(),
					paso.getMinAmountLimit(), paso.getMaxAmountLimit(), paso.getRmbPrice(), paso.getRemark(),
					Misc.PLATFORM_CURRENCY_SYMBOL.value(), paso.getMaxAmountLimit(), MerchantTradeType.BUY.code(),
					paso.getAvailableTradeAmount(), null, null);
		} else {
			UserOrder userOrder = userOrderRepository.getUserOrderInfoById(orderId);
			if (null == userOrder) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "该挂单已经下架，请刷新！");
			}
			
			//自己不能购买自己发售得货币
			if (userOrder.getUserId().equals(userId)) {
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "不支持对自己的挂单进行交易！");
			}
			
			User user = userRepository.getUserById(userOrder.getUserId());
			ctir = new CurrencyTradingInfoResponse(userOrder.getId(), userOrder.getUserId(), user.getName(), userOrder.getAvailableTradeAmount(),
					 userOrder.getAabPrice(), userOrder.getRemark(), currencySymbol, userOrder.getType());
			ctir.setIsMerchant(StateType.NO_STATE.code());
			
			//设置最大买卖数量
			BigDecimal maxBalance = new BigDecimal(0);
			if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {//买
				maxBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
				
				ctir.setMaxPlatformCurrencyBalance(maxBalance.compareTo(userOrder.getAabPrice().multiply(userOrder.getAvailableTradeAmount())) == -1 
						? maxBalance : userOrder.getAabPrice().multiply(userOrder.getAvailableTradeAmount()));
				ctir.setMaxPlatformCurrencyBalance(ctir.getMaxPlatformCurrencyBalance().divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_DOWN));
				
				maxBalance = maxBalance.divide(userOrder.getAabPrice(), 2, BigDecimal.ROUND_DOWN);
				maxBalance = maxBalance.compareTo(userOrder.getAvailableTradeAmount()) == -1 ? maxBalance : userOrder.getAvailableTradeAmount();
				ctir.setMaxBalance(maxBalance);
				
			} else {//卖
				if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(currencySymbol).equals(userRepository.getMerchantIdByUser(userId))) {
					maxBalance = merchantAssertStatisticsRepository.queryAssertStaticSellRest(currencySymbol, userId, MerchantAssertStatisticsKey.SELL_REST.name());
				} else {
					maxBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, currencySymbol);
				}
				maxBalance = null == maxBalance ? BigDecimal.ZERO : maxBalance;
				maxBalance = maxBalance.compareTo(userOrder.getAvailableTradeAmount()) == -1 ? maxBalance : userOrder.getAvailableTradeAmount();
				ctir.setMaxBalance(maxBalance.divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_DOWN));
				
				ctir.setMaxPlatformCurrencyBalance(maxBalance.multiply(userOrder.getAabPrice()).divide(BigDecimal.valueOf(1), 2, BigDecimal.ROUND_DOWN));
				
				//卖单判断当前账号是否当前货币商家
				if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(userOrder.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userId))) {
					ctir.setIsMerchant(StateType.OK_STATE.code());
					
					BigDecimal serviceCharge = platformServiceChargeStrategyRepository.getByMerchantMobile(userRepository.getUserById(userId).getMobileNumber()).getTradeChargeRate();
					ctir.setTradeChargeRate((null == serviceCharge) ? BigDecimal.ZERO : serviceCharge);
				}
			}
			
		}
		
		return ctir;
	}
	
	@Override
	public PlatformCurrencyOrderInfoResponse getPlatformCurrencyTradingInfo(Long orderId,Long userId, BigDecimal transactionNum) throws Exception {
		PlatformAssertSellingOrder paso = platformAssertSellingOrderRepository.getSellerOrderInfoById(orderId);
		PlatformAssertSeller pas = platformAssertSellerRepository.getSellerById(paso.getSellerId());
		
		//挂单人基本信息
		PlatformCurrencyOrderInfoResponse aabOrder = new PlatformCurrencyOrderInfoResponse(
				orderId, pas.getId(), pas.getName(), pas.getSupportAlipay(), pas.getSupportWechat(),
				pas.getSupportBankCard(), pas.getAlipayAccount(), pas.getAlipayQrCodePath(),
				pas.getWechatAccount(), pas.getWechatQrCodePath(), 
				pas.getBankCardNumber());
		
		if (!StringUtils.isEmpty(aabOrder.getAlipayQrCodePath())) {
			aabOrder.setAlipayQrCodePath(fileStorageHandler.genDownloadUrl(aabOrder.getAlipayQrCodePath()));
		}
		
		if (!StringUtils.isEmpty(aabOrder.getWechatQrCodePath())) {
			aabOrder.setWechatQrCodePath(fileStorageHandler.genDownloadUrl(aabOrder.getWechatQrCodePath()));
		}
		//2.押金交易金额
		aabOrder.setTransactionNum(transactionNum);
		
		//3.AAB当前单价锁数据
		LockMoneyTransaction lockMoney = lockMoneyTransactionRepository.getLockMoneyTransaction(userId, orderId);
		aabOrder.setUnitPrice(lockMoney.getPrice());
		
		//4.付款参考号
		boolean condition = true;
		while(condition){
			try{
				aabOrder.setReferenceNumber(RandomUtil.createNumber(6));
				orderReferenceNumberRepository.saveReferenceNumber(new OrderReferenceNumber(aabOrder.getReferenceNumber()));
				condition = false;
			} catch(DuplicateKeyException du){
				condition = true;
				log.info("付款参考码重复，重新生成！");
			}
		}
		//5.订单时间
		aabOrder.setOrderTime(System.currentTimeMillis());
		
		User user = userRepository.getUserById(userId);
		aabOrder.setInitiatorId(user.getId());
		aabOrder.setInitiatorName(user.getName());
		aabOrder.setTradeType(PlatformAssertTradeType.BUY.code());
		
		UserAssert uAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		aabOrder.setWalletAddress(uAssert.getWalletAddress());
		return aabOrder;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean submitPlatformCurrencyOrder(PlatformCurrencyOrderSubRequest orderSubRequest, Long userId) throws Exception {
		CurrencyTradingInfoResponse ctir = getCurrencyTradingInfo(Misc.PLATFORM_CURRENCY_SYMBOL.value(), orderSubRequest.getOrderId(), userId);
		if (orderSubRequest.getTransactionNum().compareTo(ctir.getAvailableTradeAmount()) == 1) {
			throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "购买失败，库存不足！");
		}
		
		PlatformAssertSellingOrder paso = platformAssertSellingOrderRepository.getSellerOrderInfoById(orderSubRequest.getOrderId());
		PlatformAssertSeller pas = platformAssertSellerRepository.getSellerById(paso.getSellerId());
		
		User user = userRepository.getUserById(userId);
		
		UserAssert uAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		BigDecimal validBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		
		//AAB当前单价锁数据
		LockMoneyTransaction lockMoney = lockMoneyTransactionRepository.getLockMoneyTransaction(userId, orderSubRequest.getOrderId());

		PlatformAssertTradeRecord patr = new PlatformAssertTradeRecord(null,
						user.getId(), user.getName(),
						PlatformAssertTradeType.BUY.code(), orderSubRequest.getOrderTime(), lockMoney.getPrice(),paso.getAvailableTradeAmount(),
						orderSubRequest.getTransactionNum(), paso.getMinAmountLimit(), paso.getMaxAmountLimit(), uAssert.getWalletAddress(),
						uAssert.getBalance(), validBalance,
						pas.getId(), pas.getName(),
						orderSubRequest.getReferenceNumber(), PlatformAssertTradeExamineType.TO_BE_TRANSFERRED.code(), paso.getId());
		platformAssertTradeRecordRepository.saveAssertTeadeTrcord(patr);
		
		return true;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
	public MerchantAssertTradeRecordResponse addUserOrder(UserOrderSubRequest userOrderSubRequest, Long userId) throws Exception {
		
		/**
		 * 一.购买
		 * 1.用户资产-AAB 商家资产+AAB
		 * 2.用户商户币+ 商家冻结商户币- 商家商户币-
		 * 
		 * 二.出售
		 * 1.出售方货币- 挂单方货币+
		 * 2.出售方AAB+ 挂单方冻结AAB- 挂单方AAB-
		 * 
		 * 交易流水
		 */
		UserOrder userOrder = userOrderRepository.getUserOrderInfoById(userOrderSubRequest.getOrderId());
		User user = userRepository.getUserById(userId);
		User initiator = userRepository.getUserById(userOrder.getUserId());
		BigDecimal serviceCharge = new BigDecimal(0);//手续费率
		BigDecimal serviceChargeAAB = new BigDecimal(0);//手续费金额
		PlatformAssertIncomeExpenditureRecord paier = null;
		
		Long orderTime = System.currentTimeMillis();//交易时间
		//单价锁数据
		LockMoneyTransaction lockMoney = lockMoneyTransactionRepository.getLockMoneyTransaction(userId, userOrder.getId());
		
		if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {
			userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), userOrderSubRequest.getTransactionNum().multiply(userOrder.getAabPrice()).multiply(new BigDecimal(-1)), BigDecimal.ZERO);
			//卖单判断挂单用户是否当前货币商家扣除手续费
			if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(userOrder.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userOrder.getUserId()))) {
				serviceCharge = platformServiceChargeStrategyRepository.getByMerchantMobile(initiator.getMobileNumber()).getTradeChargeRate();
				serviceChargeAAB = userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice()).multiply(serviceCharge).divide(BigDecimal.valueOf(100.00));
				paier = new PlatformAssertIncomeExpenditureRecord(
						PlatformCurrencyDirectionEnum.ACCOUNT_ADVANCE.code(), initiator.getId(), initiator.getName(), 
						orderTime, serviceChargeAAB, serviceChargeAAB.multiply(BigDecimal.valueOf((Long.valueOf(Misc.PLATFORM_CURRENCY_EXCHANGE_RATE.value())))), PlatformCurrencyAtransactionction.TRANSACTION_CHARGES.code());
			}
			
			userAssertRepository.addUserAssertBalance(userOrder.getUserId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(),
					(userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice())).subtract(serviceChargeAAB),
					(userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice())).subtract(serviceChargeAAB));
			
			userAssertRepository.addUserAssertBalance(userId, userOrder.getCurrencySymbol(), userOrderSubRequest.getTransactionNum(), userOrderSubRequest.getTransactionNum());
			userAssertFreezeRepository.updateFreezeAmount(
					new UserAssertFreeze(userOrder.getUserId(), userOrder.getCurrencySymbol(),
							userOrderSubRequest.getTransactionNum().divide(new BigDecimal(-1)),
							UserRreezeReason.SALE_OF_BILLS.code().intValue(), null));
			userAssertRepository.addUserAssertBalance(userOrder.getUserId(), userOrder.getCurrencySymbol(), userOrderSubRequest.getTransactionNum().divide(new BigDecimal(-1)), BigDecimal.ZERO);
			
		} else {
			//买单判断当前账号是否当前货币商家扣除手续费
			if (merchantAssertIssuanceRepository.queryCurrencyMerchantId(userOrder.getCurrencySymbol()).equals(userRepository.getMerchantIdByUser(userId))) {
				serviceCharge = platformServiceChargeStrategyRepository.getByMerchantMobile(initiator.getMobileNumber()).getTradeChargeRate();
				serviceChargeAAB = userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice()).multiply(serviceCharge).divide(BigDecimal.valueOf(100.00));
				paier = new PlatformAssertIncomeExpenditureRecord(
						PlatformCurrencyDirectionEnum.ACCOUNT_ADVANCE.code(), initiator.getId(), initiator.getName(), 
						orderTime, serviceChargeAAB, serviceChargeAAB.multiply(BigDecimal.valueOf((Long.valueOf(Misc.PLATFORM_CURRENCY_EXCHANGE_RATE.value())))), PlatformCurrencyAtransactionction.TRANSACTION_CHARGES.code());
			}
			
			userAssertRepository.addUserAssertBalance(userId, userOrder.getCurrencySymbol(), userOrderSubRequest.getTransactionNum().multiply(new BigDecimal(-1)), BigDecimal.ZERO);
			userAssertRepository.addUserAssertBalance(userOrder.getUserId(), userOrder.getCurrencySymbol(), userOrderSubRequest.getTransactionNum(), userOrderSubRequest.getTransactionNum());
			
			userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(),
					(userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice())).subtract(serviceChargeAAB),
					(userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice())).subtract(serviceChargeAAB));
			userAssertFreezeRepository.updateFreezeAmount(
					new UserAssertFreeze(userOrder.getUserId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(),
							userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice()).divide(new BigDecimal(-1)),
							UserRreezeReason.PURCHASE_LIST.code().intValue(), null));
			userAssertRepository.addUserAssertBalance(userOrder.getUserId(), Misc.PLATFORM_CURRENCY_SYMBOL.value(),
					userOrderSubRequest.getTransactionNum().multiply(lockMoney.getPrice()).divide(new BigDecimal(-1)),
					BigDecimal.ZERO);
		}
		
		UserAssert uAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		
		BigDecimal validBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		
		BigDecimal merchantBalance = userAssertRepository.getUserAssetsByCurrencySymbol(userId, userOrder.getCurrencySymbol());
		
		BigDecimal merchantValidBalance = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, userOrder.getCurrencySymbol());
		
		MerchantAssertTradeRecord matr = new MerchantAssertTradeRecord(user.getId(), user.getName(), 
				MerchantTradeType.SELL.code().equals(userOrder.getType()) ? MerchantTradeType.BUY.code(): MerchantTradeType.SELL.code(),
				orderTime, userOrder.getCurrencySymbol(), lockMoney.getPrice(),
				userOrderSubRequest.getTransactionNum(), uAssert.getBalance(),
				initiator.getId(), initiator.getName(), validBalance, merchantBalance, merchantValidBalance,
				userOrder.getId());
		
		merchantAssertTradeRecordRepository.saveTradeRecord(matr);
		
		//插入统计数据
		latestDataService.updateCurrencyMailMsg(
				new MerchantAssertTradeRecord(orderTime, userOrder.getCurrencySymbol(), userOrder.getType(),
						lockMoney.getPrice(), userOrderSubRequest.getTransactionNum(), userOrderSubRequest.getOrderId()), MerchantOrderSource.SOURCE_APP);
		
		//添加手续费记录
		if (serviceChargeAAB.compareTo(new BigDecimal(0)) != 0) {
			platformAssertIncomeExpenditureRecordRepository.saveIncomeExpenditureRecord(paier);
		}
		
		//自动减少原挂单买卖数量
		userOrderRepository.updateUserOrderAmountById(userOrderSubRequest.getOrderId(), userOrderSubRequest.getTransactionNum().divide(new BigDecimal(-1)));
		
		return new MerchantAssertTradeRecordResponse(matr.getInitiatorId(), matr.getInitiatorName(),
				matr.getTradeType(), matr.getTradeTime(), matr.getCurrencySymbol(),
				matr.getPlatformPrice(), matr.getTradeAmount(), matr.getBalance(),
				matr.getPartnerId(), matr.getPartnerName());
	}
	
	@Override
	public PageResult<CurrencyTradingListResponse> getMyCurrencyTradingList(Integer pageNo, Integer pageSize, Long userId) throws Exception {
		List<CurrencyTradingListResponse> ctList = new ArrayList<CurrencyTradingListResponse>();
		
		Page<CurrencyTradingListResponse> page = PageHelper.startPage(pageNo, pageSize, true);
        
		ctList = userOrderRepository.getMyOrderList(userId, new Integer[]{UserOrderStatus.UPPER_SHELF.code()});
		
		return new PageResult<CurrencyTradingListResponse>(pageNo, pageSize, page.getTotal(), page.getPages(), ctList);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
	public boolean updateSalesOrder(SalesOrderRequest salesOrderRequest, Long userId) throws Exception {
		
		UserOrder userOrder = userOrderRepository.getUserOrderInfoById(salesOrderRequest.getOrderId());
		
		//冻结相应资产
		if (MerchantTradeType.SELL.code().equals(userOrder.getType())) {
			
//			userAssertRepository.addUserAssertBalance(userId, userOrder.getCurrencySymbol(), userOrder.getAvailableTradeAmount().subtract(salesOrderRequest.getAvailableTradeAmount()));
			userAssertFreezeRepository.updateFreezeAmount(
					new UserAssertFreeze(userId, userOrder.getCurrencySymbol(),
							salesOrderRequest.getAvailableTradeAmount().subtract(userOrder.getAvailableTradeAmount()),
							UserRreezeReason.SALE_OF_BILLS.code().intValue(), null));
			
		} else {
			
//			userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), userOrder.getAvailableTradeAmount().multiply(userOrder.getAabPrice()).subtract((salesOrderRequest.getAvailableTradeAmount().multiply(salesOrderRequest.getPrice()))));
			userAssertFreezeRepository.updateFreezeAmount(
					new UserAssertFreeze(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(),
							salesOrderRequest.getAvailableTradeAmount().multiply(salesOrderRequest.getPrice()).subtract((userOrder.getAvailableTradeAmount().multiply(userOrder.getAabPrice()))),
							UserRreezeReason.PURCHASE_LIST.code().intValue(), null));
			
		}
		
		//修改挂单
		userOrderRepository.updateUserOrderById(salesOrderRequest.getOrderId(), salesOrderRequest.getAvailableTradeAmount(), 
				salesOrderRequest.getPrice(), salesOrderRequest.getRemark());
		
		return true;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateSalesOrderStatus(UserOrderStatusRequest userOrderStatusRequest, Long userId) throws Exception {
		
		userOrderStatusRequest.setStatus((null == userOrderStatusRequest.getStatus()) ? LOWER_SHELF.code() : userOrderStatusRequest.getStatus());
		
		userOrderRepository.updateUserOrderStatusById(userOrderStatusRequest.getOrderId(), userOrderStatusRequest.getStatus());

		// 添加针对商户的取消卖单信息
		UserOrder order = this.userOrderRepository.getUserOrderInfoById(userOrderStatusRequest.getOrderId());
		if (order!=null) {
			Long merchantId = this.merchantAssertIssuanceRepository.queryCurrencyMerchantId(order.getCurrencySymbol());
			if (order.getUserId().equals(this.userRepository.getUserIdByMerchantId(merchantId)) && userOrderStatusRequest.getStatus().equals(LOWER_SHELF.code())) {
				this.merchantServiceImpl.addMerchantTradeRecord(new MerchantTradeRecord(
						merchantId,
						userId,
						CANCEL_SELL_​​ORDER.code(),
						CANCEL_SELL_​​ORDER.flowDirectionEnum().code(),
						order.getAvailableTradeAmount(),
						order.getAabPrice(),
						System.currentTimeMillis(),
						CANCEL_SELL_​​ORDER.addSubtractEnum().code())

				);
			}
		}
		return true;
	}
	
}
