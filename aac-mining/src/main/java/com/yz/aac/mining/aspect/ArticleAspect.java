package com.yz.aac.mining.aspect;

import com.github.pagehelper.util.StringUtil;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.mining.Constants;
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
import java.util.List;

/**
 * 资讯aop
 */
@Order(1)
@Component
@Aspect
public class ArticleAspect {
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;

	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;

	@Autowired
	private AccountAspect accountAspect;

	@Autowired
	private UserAssertRepository userAssertRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleInteractionRepository articleInteractionRepository;

	@Autowired
	private ArticlePersonalRepository articlePersonalRepository;

	@Before("execution(* com.yz.aac.mining.service.impl.ArticleServiceImpl.setReadingAward(..))")
	public void setReadingAwardBefore(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long userId = Long.valueOf(args[0].toString());

		//是否达到当日阅读最高奖励数
		List<UserMiningRecord> miningList = userMiningRecordRepository.getRecordToDay(userId, Constants.MiningActionEnum.READ.code());
		int max = (null == miningList || miningList.size() == 0) ? 0 : miningList.size();
		ParamConfig maxReadingPerDay = paramConfigRepository.getParamConfig(Constants.ParamConfigSubclassEnum.MAX_READING_PER_DAY.maxCode(),Constants. ParamConfigSubclassEnum.MAX_READING_PER_DAY.minCode(), Constants.ParamConfigSubclassEnum.MAX_READING_PER_DAY.name());

		if (max >= Integer.valueOf(maxReadingPerDay.getValue())) {
			throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "今日阅读文章奖励次数达到上限！");
		}

	}

	/**
	 *评论之前是否实名认证
	 * @param pjp
	 * @throws Throwable
	 */
	@Before("execution(* com.yz.aac.mining.service.impl.ArticleServiceImpl.comment(..))")
	public void comment(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long userId = Long.valueOf(args[1].toString());

		User user  = userRepository.getUserById(userId);

		if (StringUtil.isEmpty(user.getIdNumber()) || StringUtil.isEmpty(user.getName())) {
			throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您没有实名认证，无法评论！");
		}

	}

	/**
	 * 文章个性化设置只能设置一次
	 * @param pjp
	 * @throws Throwable
	 */
	@Before("execution(* com.yz.aac.mining.service.impl.ArticleServiceImpl.personalizedSettings(..))")
	public void personalizedSettings(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long articleId = Long.valueOf(args[0].toString());
		Long userId = Long.valueOf(args[1].toString());
		Integer policy = Integer.valueOf(args[2].toString());

		ArticlePersonal articlePersonal = articlePersonalRepository.getArticlePersonal(userId, articleId, policy);
		if (null != articlePersonal) {
			throw new BusinessException(com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(),
					Constants.ArticlePersonalType.FOLLOW.code() == policy ? "文章已关注" : "文章已屏蔽！");
		}

	}

	/**
	 * 阅读文章完成事件
	 * @param pjp
	 * @throws Throwable
	 */
	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.ArticleServiceImpl.articleInfo(..))")
	@Transactional(rollbackFor = Exception.class)
	public void articleInfoAfterReturn(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long articleId = (Long) args[0];
		Long userId = (Long) args[1];

		articleInteractionRepository.saveArticleInteraction(
		new ArticleInteraction(null, articleId, null,
				Constants.ArticleActionType.READING.code(), null, userId, System.currentTimeMillis()));

		//用户行为统计
		accountAspect.addAccountBehaviour(userId, Constants.UserBehaviourStatisticsKey.READ.name());
	}

	@Order(1)
	@AfterReturning(pointcut = "execution(* com.yz.aac.mining.service.impl.ArticleServiceImpl.issuance(..))")
	@Transactional(rollbackFor = Exception.class)
	public void issuanceAfterReturn(JoinPoint pjp) throws Throwable{
		Object[] args = pjp.getArgs();
		Long userId = (Long) args[3];

		//是否达到当日发帖最高奖励数
		List<UserMiningRecord> miningList = userMiningRecordRepository.getRecordToDay(userId, Constants.MiningActionEnum.POSTING.code());
		int max = (null == miningList || miningList.size() == 0) ? 0 : miningList.size();
		ParamConfig maxPublishingPerDay = paramConfigRepository.getParamConfig(Constants.ParamConfigSubclassEnum.MAX_PUBLISHING_PER_DAY.maxCode(),Constants. ParamConfigSubclassEnum.MAX_PUBLISHING_PER_DAY.minCode(), Constants.ParamConfigSubclassEnum.MAX_PUBLISHING_PER_DAY.name());

		if (max < Integer.valueOf(maxPublishingPerDay.getValue())) {
			ParamConfig publishingCurrency = paramConfigRepository.getParamConfig(Constants.ParamConfigSubclassEnum.PUBLISHING_CURRENCY.maxCode(), Constants.ParamConfigSubclassEnum.PUBLISHING_CURRENCY.minCode(), Constants.ParamConfigSubclassEnum.PUBLISHING_CURRENCY.name());
			int upset = userAssertRepository.addUserAssertBalance(userId, com.yz.aac.common.Constants.Misc.PLATFORM_CURRENCY_SYMBOL.value(), BigDecimal.valueOf(Integer.valueOf(publishingCurrency.getValue())), BigDecimal.valueOf(Integer.valueOf(publishingCurrency.getValue())));
			if (upset > 0) {
				//挖矿记录
				User user = userRepository.getUserById(userId);
				userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), Constants.MiningActionEnum.POSTING.code(), System.currentTimeMillis(), BigDecimal.valueOf(Integer.valueOf(publishingCurrency.getValue())), Constants.MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));

			}
		}

		//用户行为统计
		accountAspect.addAccountBehaviour(userId, Constants.UserBehaviourStatisticsKey.POST_COMMENT.name());
	}

}
