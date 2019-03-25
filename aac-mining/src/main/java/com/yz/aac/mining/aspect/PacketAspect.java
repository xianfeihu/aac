package com.yz.aac.mining.aspect;

import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.mining.Constants.ParamConfigSubclassEnum;
import com.yz.aac.mining.Constants.UserBehaviourStatisticsKey;
import com.yz.aac.mining.model.request.PacketRequest;
import com.yz.aac.mining.model.response.PacketInfoResponse;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import com.yz.aac.mining.util.RedPacketUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 红信aop
 */
@Order(1)
@Component
@Aspect
public class PacketAspect {
	
	@Autowired
	private RedPacketDividingRepository redPacketDividingRepository;
	
	@Autowired
	private UserPaymentErrorRecordRepository userPaymentErrorRecordRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAssertRepository userAssertRepository;
	
	@Autowired
	private RedPacketIssuanceRepository redPacketIssuanceRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private RedPacketDividingGrabbingRepository redPacketDividingGrabbingRepository;
	
	@Autowired
	private AccountAspect accountAspect;

	@Before("execution(* com.yz.aac.mining.service.impl.PacketServiceImpl.openPacket(..))")
	public void openPacketrBefore(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long packetId = Long.valueOf(args[0].toString());
		Long userId = Long.valueOf(args[1].toString());
		
		//校验红包是否存在
		RedPacketIssuance rpi = redPacketIssuanceRepository.getPacketIssuanceById(packetId);
		if (null == rpi) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "红包已过期！");
		}
		
		//红包是否派发完毕
		List<RedPacketDividing> surplus = redPacketDividingRepository.getPacketDividingSurplus(packetId);
		if (null == surplus || surplus.size() == 0) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "红包派发结束！");
		}
		
		//当日领取红包个数限制
		ParamConfig config = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.GRAB_RED_PACKET_PER_DAY.maxCode(), ParamConfigSubclassEnum.GRAB_RED_PACKET_PER_DAY.minCode(), ParamConfigSubclassEnum.GRAB_RED_PACKET_PER_DAY.name());
		Integer receiveNum = redPacketDividingGrabbingRepository.getTodayReceiveCount(userId);
		
		if (receiveNum >= Integer.valueOf(config.getValue())) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(),"今日领取红包次数已用完！");
		}
	}
	
	@Before("execution(* com.yz.aac.mining.service.impl.PacketServiceImpl.savePacket(..))")
	public void savePacketBefore(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		PacketRequest packetRequest = (PacketRequest) args[0];
		Long userId = Long.valueOf(args[2].toString());
		
		//密码校验
		paymentPasswordChecking(userId, packetRequest.getPayPasword());
		
		//余额校验
		BigDecimal currencyLimit = userAssertRepository.getUserAvailableFundsByCurrencySymbol(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		if (packetRequest.getDividingAmount().compareTo(currencyLimit) == 1) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "余额不足！");
		}
		
		//红包金额平均值最小0.01校验
    	if (!RedPacketUtil.isFloorPrice(packetRequest.getDividingNumber(), packetRequest.getDividingAmount().doubleValue())) {
    		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "红包金额不可小于0.01！");
    	}
	}
	
	/**
	 * 发布红信完成事件
	 * @param pjp
	 * @param packetInfoResponse
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(returning = "packetInfoResponse", pointcut = "execution(* com.yz.aac.mining.service.impl.PacketServiceImpl.savePacket(..))")
	@Transactional(rollbackFor = Exception.class)
	public void getQuestionListAfterReturn(JoinPoint pjp, PacketInfoResponse packetInfoResponse) throws Throwable{
		Object[] args = pjp.getArgs();
		PacketRequest packetRequest = (PacketRequest) args[0];
    	Long redPacketId = packetInfoResponse.getId();
    	Long userId = (Long) args[2];
    	
    	double[] randAry = RedPacketUtil.allocateRedPacket(packetRequest.getDividingNumber(), packetRequest.getDividingAmount().doubleValue());
    	if (null == randAry) {
    		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "红包金额不可小于0.01！");
    	}
    	
    	List<RedPacketDividing> dividings = new ArrayList<RedPacketDividing>();
    	for (double d : randAry) {
    		dividings.add(new RedPacketDividing(redPacketId, BigDecimal.valueOf(d)));
    	}
    	redPacketDividingRepository.saveRedPacketDividing(dividings);
    	
    	//扣除用户资金
		userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), packetRequest.getDividingAmount().multiply(BigDecimal.valueOf(-1)), BigDecimal.ZERO);
    	
    	//用户行为统计
    	accountAspect.addAccountBehaviour(userId, UserBehaviourStatisticsKey.SEND_RED_PACKET.name());
	}
	
	/**
	 * 支付密码正确性和失效性
	 * @param userId
	 * @param payPasword
	 * @throws BusinessException 
	 */
	private void paymentPasswordChecking(Long userId, String payPasword) throws BusinessException{
		if (userPaymentErrorRecordRepository.queryPaymentErrorRecord(userId) >= 3) {
			if (null != userPaymentErrorRecordRepository.queryPaymentErrorRecordByTime(userId, 30)) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您输入密码错误过多，请一小时后重试！");
			} else {
				userPaymentErrorRecordRepository.deletePaymentErrorRecordByUserId(userId);
			}
		} 
		
		if (true) {
			User user = userRepository.getUserById(userId);
			try {
				if (!user.getPaymentPassword().equals(payPasword)) {
					userPaymentErrorRecordRepository.savePaymentErrorRecord(new UserPaymentErrorRecord(userId, System.currentTimeMillis()));
					throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您输入的密码错误，请重新输入");
				}
				userPaymentErrorRecordRepository.deletePaymentErrorRecordByUserId(userId);
			} catch(NullPointerException e){
				throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "您未设置支付密码！");
			}
		}
	}
	
}
