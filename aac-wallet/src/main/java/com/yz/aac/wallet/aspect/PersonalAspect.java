package com.yz.aac.wallet.aspect;

import com.yz.aac.common.Constants;
import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.wallet.Constants.*;
import com.yz.aac.wallet.model.request.ExchangeRequest;
import com.yz.aac.wallet.repository.*;
import com.yz.aac.wallet.repository.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Component
@Aspect
public class PersonalAspect {

	@Autowired
	private ExchangeRepository exchangeRepository;

	@Autowired
	private ExchangeRecordRepository exchangeRecordRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAssetsRepository userAssetsRepository;

	@Autowired
	private ExchangeItemRepository exchangeItemRepository;

	@Autowired
	private PlatformAssertTradeRecordRepository platformAssertTradeRecordRepository;

	@Autowired
	private PlatformAssertIncomeExpenditureRecordRepository platformAssertIncomeExpenditureRecordRepository;

	@Before("execution(* com.yz.aac.wallet.service.impl.PersonalServiceImpl.exchange(..))")
	public void exchangeBefore(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		ExchangeRequest exchangeRequest = (ExchangeRequest)args[0];
    	Long userId = Long.valueOf(args[1].toString());

		//次数判断
		Exchange exchange = exchangeRepository.getExchangeById(exchangeRequest.getExchangeId());
		List<ExchangeRecord> recordList = exchangeRecordRepository.getExchangeRecord(exchangeRequest.getExchangeId(), userId);

		if (((null == recordList || recordList.size() ==0) ? 0 : recordList.size()) >= exchange.getLimitInMonth()) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "充值失败，本月剩余该充值类型0次！");
		}

		//密码判断
		User user = userRepository.getUserById(userId);
		try {
			if (!user.getPaymentPassword().equals(exchangeRequest.getPayPass())) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您输入的密码错误，请重新输入");
			}
		} catch(NullPointerException e){
			throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您未设置支付密码！");
		}

		//余额判断
		ExchangeRecord ExchangeRecord = calculationRechargeAmount(exchangeRequest);
		BigDecimal balance = userAssetsRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		if (ExchangeRecord.getPlatformAmount().compareTo(balance) == 1) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "平台币余额不足！");
		}

		//充值类型判断
		if(StateType.NO_STATE.code() == exchange.getCustomized() && ExchangeRechargeTypeEnum.MANUAL.code() == exchangeRequest.getTagId()) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "当前业务不支持手动充值！");
		}

	}

	/**
	 * 根据充值类型换算平台币金额
	 * @param exchangeRequest
	 * @return
	 */
	public ExchangeRecord calculationRechargeAmount(ExchangeRequest exchangeRequest){
		Integer rmbAmount;
		BigDecimal platformAmount;

		if (ExchangeRechargeTypeEnum.OPTION.code() == exchangeRequest.getTagId()) {
			ExchangeItem exchangeItem = exchangeItemRepository.getExchangeItem(exchangeRequest.getOptionId());
			rmbAmount = exchangeItem.getRmbAmount();
			platformAmount = exchangeItem.getPlatformAmount();
		} else {
			rmbAmount = exchangeRequest.getRmbAmount();
			platformAmount = BigDecimal.valueOf(exchangeRequest.getRmbAmount())
					.divide(BigDecimal.valueOf(Integer.valueOf(Constants.Misc.PLATFORM_CURRENCY_EXCHANGE_RATE.value())), 2,BigDecimal.ROUND_HALF_UP);
		}

		ExchangeRecord exchangeRecord = new ExchangeRecord();
		exchangeRecord.setRmbAmount(rmbAmount);
		exchangeRecord.setPlatformAmount(platformAmount);

		return exchangeRecord;
	}

	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.wallet.service.impl.PersonalServiceImpl.exchange(..))")
	@Transactional(rollbackFor = Exception.class)
	public void register(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		ExchangeRequest exchangeRequest = (ExchangeRequest)args[0];
    	Long userId = Long.valueOf(args[1].toString());

		User user = userRepository.getUserById(userId);
		UserAssets userAssets = userAssetsRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());

    	//扣除余额
		ExchangeRecord exchangeRecord = calculationRechargeAmount(exchangeRequest);
		userAssetsRepository.addUserAssertBalance(userId, Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value(), exchangeRecord.getPlatformAmount().multiply(BigDecimal.valueOf(-1)), BigDecimal.ZERO);

		//平台币交易记录
		Integer tradeType ;
		if (exchangeRequest.getExchangeType() == ExchangeTypeEnum.TELEPHONE_BILL.code()) {
			tradeType = PlatformAssertTradeType.EXCHANGE_CALLS.code();
		} else {
			tradeType = PlatformAssertTradeType.EXCHANGE_CARD.code();
		}

		BigDecimal validBalance = userAssetsRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());

		BigDecimal balance = userAssetsRepository.getUserAssetsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());

		PlatformAssertTradeRecord patr =
				new PlatformAssertTradeRecord(
						user.getId(), user.getName(),
						tradeType, System.currentTimeMillis(), BigDecimal.valueOf(Integer.valueOf(Misc.PLATFORM_CURRENCY_EXCHANGE_RATE.value())),
						exchangeRecord.getPlatformAmount(), userAssets.getWalletAddress(), balance, validBalance,
						(long)Constants.ExternalServiceNumber.LOCAL_APP.code(), String.valueOf(Constants.ExternalServiceNumber.LOCAL_APP.code()),
						null, PlatformAssertTradeExamineType.FINISH.code(),
						null, null,
						null,null);

			platformAssertTradeRecordRepository.saveAssertTeadeTrcord(patr);

		//平台币进出帐记录
		Integer action;
		if (exchangeRequest.getExchangeType() == ExchangeTypeEnum.TELEPHONE_BILL.code()) {
			action = PlatformCurrencyAtransactionction.RECHARGE_TELEPHONE_IN.code();
		} else if (exchangeRequest.getExchangeType() == ExchangeTypeEnum.CNPC.code()) {
			action = PlatformCurrencyAtransactionction.RECHARGE_CNPC_IN.code();
		} else {
			action = PlatformCurrencyAtransactionction.RECHARGE_SINOPEC_IN.code();
		}
		platformAssertIncomeExpenditureRecordRepository.saveIncomeExpenditureRecord(
				new PlatformAssertIncomeExpenditureRecord(PlatformCurrencyDirectionEnum.ACCOUNT_ADVANCE.code(),
						user.getId(), user.getName(), System.currentTimeMillis(), exchangeRecord.getPlatformAmount(),
						BigDecimal.valueOf(exchangeRecord.getRmbAmount()), action));

		
	}
	
}
