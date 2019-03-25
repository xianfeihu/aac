package com.yz.aac.mining.aspect;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.mining.Constants.MiningActionEnum;
import com.yz.aac.mining.Constants.MiningBonusTypeEnum;
import com.yz.aac.mining.Constants.ParamConfigSubclassEnum;
import com.yz.aac.mining.Constants.UserBehaviourStatisticsKey;
import com.yz.aac.mining.model.request.SubmitAnswerRequest;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 答题aop
 */
@Order(1)
@Component
@Aspect
public class QuestionAspect {
	
	@Autowired
	private UserChallengeQuestionsRecordRepository userChallengeQuestionsRecordRepository;
	
	@Autowired
	private MiningQuestionRepository miningQuestionRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private UserPropertyRepository userPropertyRepository;
	
	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;
	
	@Autowired
	private AccountAspect accountAspect;
	
	@Autowired
	private UserRepository userRepository;

	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.QuestionServiceImpl.getQuestionList(..))")
	@Transactional(rollbackFor = Exception.class)
	public void getQuestionListAfterReturn(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
    	Long userId = Long.valueOf(args[0].toString());
    	
		//修改用户答题挑战次数
		UserChallengeQuestionsRecord record = userChallengeQuestionsRecordRepository.getTodayRecord(userId);
		if (null == record) {
			userChallengeQuestionsRecordRepository.saveUserChallengeQuestionsRecord(
					new UserChallengeQuestionsRecord(userId, 1, 0, 0,System.currentTimeMillis(), 0));
		} else {
			ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.name());
			if (record.getFrequency() >= (Integer.valueOf(paramConfig.getValue()))) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "今日机会已用完，明日再来哦！");
			}
			userChallengeQuestionsRecordRepository.updateUserChallengeQuestionsRecord(userId);
		}
	}
	
	/**
	 * 答题前作答次数+1
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@Before("execution(* com.yz.aac.mining.service.impl.QuestionServiceImpl.submitAnswer(..))")
	@Transactional(rollbackFor = Exception.class)
	public void submitAnswerBefore(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
    	Long userId = Long.valueOf(args[1].toString());
    	
    	userChallengeQuestionsRecordRepository.updateAnswerNumber(userId);

		accountAspect.addAccountBehaviour(userId, UserBehaviourStatisticsKey.ANSWER.name());
	}
	
	/**
	 * 答题成功奖励计算
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.QuestionServiceImpl.submitAnswer(..))")
	@Transactional(rollbackFor = Exception.class)
	public void submitAnswerAfterReturn(JoinPoint pjp) throws Throwable{
		
		Object[] args = pjp.getArgs();
		SubmitAnswerRequest submitAnswerRequest = (SubmitAnswerRequest) args[0];
    	Long userId = Long.valueOf(args[1].toString());
    	
    	User user = userRepository.getUserById(userId);
    	UserChallengeQuestionsRecord record = userChallengeQuestionsRecordRepository.getTodayRecord(userId);
    	if (record.getAnswerNumber() > 3) {
    		//答题成功领取奖励
    		MiningQuestion question = miningQuestionRepository.getQuestionById(submitAnswerRequest.getQuestionId());
    		int upset = userPropertyRepository.updatePowerPoint(userId, question.getPowerPointBonus());
    		if (upset > 0) {
    			//累计本次挑战奖励值
    			userChallengeQuestionsRecordRepository.updatePowerPointBonus(userId, question.getPowerPointBonus());
    			
    			userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.ANSWER.code(), System.currentTimeMillis(), BigDecimal.valueOf(question.getPowerPointBonus()), MiningBonusTypeEnum.POWER_POINT.code()));
    		}
    	} else {
    		userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.ANSWER.code(), System.currentTimeMillis(), BigDecimal.valueOf(0.0), MiningBonusTypeEnum.POWER_POINT.code()));
			//记录正确答题次数
    		userChallengeQuestionsRecordRepository.updatePowerPointBonus(userId, 0);
    	}

	}
	
}
