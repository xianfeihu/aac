package com.yz.aac.mining.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.Constants.Misc;
import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.repository.FileStorageHandler;
import com.yz.aac.mining.Constants.*;
import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.GrowthRequest;
import com.yz.aac.mining.model.request.WechatCheckCodeRequest;
import com.yz.aac.mining.model.response.*;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import com.yz.aac.mining.service.AccountService;
import com.yz.aac.mining.util.DigitalConversionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService{
	
	private final static Integer MINING_TECORD_DEFAULT_PAGENO = 1;//挖矿记录默认开始数
	
	private final static Integer MINING_TECORD_DEFAULT_PAGESIZE = 20;//挖矿记录默认每页显示数
	
	private final static String MINING_TECORD_NEXT_PAGEPATH = "/index/getMiningRecordList?bonusType=%d&pageNo=%d&pageSize=%d";

	private final static String WECHAT_IMG_PATH = "http://gateway.aaccloud.com/download/img/wechat_number.jpg";

	@Autowired
	private UserPropertyRepository userPropertyRepository;
	
	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;
	
	@Autowired
	private BriefTextRepository briefTextRepository;
	
	@Autowired
	private UserLevelRepository userLevelRepository;
	
	@Autowired
	private UserAssertRepository userAssertRepository;
	
	@Autowired
    private FileStorageHandler fileStorageHandler;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserChallengeQuestionsRecordRepository userChallengeQuestionsRecordRepository;
	
	@Autowired
	private UserNaturalGrowthRecordRepository userNaturalGrowthRecordRepository;
	
	@Autowired
	private WeChatVerificationCodeRepository weChatVerificationCodeRepository;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public String collectingEnergy(GrowthRequest growthRequest, Long userId) throws Exception {

		UserNaturalGrowthRecord growth = userNaturalGrowthRecordRepository.getGrowthRecordById(growthRequest.getGrowthId());
		if (null != growth) {
			int upset = userAssertRepository.addUserAssertBalance(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value(), growth.getGrowthAmount(), growth.getGrowthAmount());
			if (upset > 0) {
				//挖矿记录
				User user = userRepository.getUserById(userId);
				userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.NATURAL_GROWTH.code(), System.currentTimeMillis(), growth.getGrowthAmount(), MiningBonusTypeEnum.PLATFORM_CURRENCY.code()));
				return "收取AAB+" + growth.getGrowthAmount();
			}
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "收取AAB能量失败！");
		}
		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "收取AAB能量失败！");

	}

	@Override
	public HomeInfoResponse getMyHomeInfo(Long userId) throws Exception {
		//等级
		UserAssert userAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		UserLevel level = userLevelRepository.getLevelByMatchCondition(userAssert.getHistoryMaxBalance());
			
		//总平台币，和总元力
		UserProperty userProperty = userPropertyRepository.getByUserId(userId);
		
		//今日平台币
		BigDecimal todayPowerPoint = userMiningRecordRepository.getMiningRecord(userId, MiningActionEnum.codes(), MiningBonusTypeEnum.PLATFORM_CURRENCY.code());
		
		//每日增长列表
		List<UserNaturalGrowthRecord> growthList = userNaturalGrowthRecordRepository.getUserNaturalGrowthByTime(userId);
		List<GrowthInfoResponse> growthInfoList = growthList.stream().map(e -> new GrowthInfoResponse(e.getId(), e.getGrowthAmount())).collect(Collectors.toList());
		
		//答题信息
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.name());
		UserChallengeQuestionsRecord record = userChallengeQuestionsRecordRepository.getTodayRecord(userId);
		Integer answerToDayNum = null == record ? 0 : record.getFrequency();
		
		//公众号信息
		Integer weChatIsConcerned = StateType.NO_STATE.code();
		List<UserMiningRecord> recordList = userMiningRecordRepository.getRecordByAction(userId, MiningActionEnum.FOCUS_ON_WECHAT.code());
		if (null != recordList && recordList.size() > 0) {
			weChatIsConcerned = StateType.OK_STATE.code();
		}
		ParamConfig weChatReward = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.maxCode(), ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.minCode(), ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.name());
		
		//签到信息
		Integer isToDaySign = StateType.NO_STATE.code();
		List<UserMiningRecord> signRecordList = userMiningRecordRepository.getRecordByAction(userId, MiningActionEnum.SIGN.code());
		int dayNum = signRecordList.size();
		if (dayNum > SignInEnum.TOTAL_DAY.code()) {
			if (dayNum%SignInEnum.TOTAL_DAY.code() == 0) {
				dayNum = SignInEnum.TOTAL_DAY.code();
			} else {
				dayNum = dayNum%SignInEnum.TOTAL_DAY.code();
			}
		}
		List<UserMiningRecord> signTodayList = userMiningRecordRepository.getRecordToDay(userId, MiningActionEnum.SIGN.code());
		if (null != signTodayList && signTodayList.size() > 0) {
			isToDaySign = StateType.OK_STATE.code();
		}
		ParamConfig signInReward = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.SING_IN_POINT.maxCode(), ParamConfigSubclassEnum.SING_IN_POINT.minCode(), ParamConfigSubclassEnum.SING_IN_POINT.name());
		
		//认证信息
		Integer realNameIsAuth = StateType.NO_STATE.code();
		List<UserMiningRecord> realNameList = userMiningRecordRepository.getRecordByAction(userId, MiningActionEnum.CERTIFICATION.code());
		if (null != realNameList && realNameList.size() > 0) {
			realNameIsAuth = StateType.OK_STATE.code();
		}
		ParamConfig realNameReward = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.REAL_NAME_VERIFICATION_CURRENCY.maxCode(), ParamConfigSubclassEnum.REAL_NAME_VERIFICATION_CURRENCY.minCode(), ParamConfigSubclassEnum.REAL_NAME_VERIFICATION_CURRENCY.name());
		
		List<HomeTagResponse> tags = new ArrayList<HomeTagResponse>();
		int min = 0;
		int max = 0;
		int buttonIsShow = StateType.NO_STATE.code();
		String synopsis = null;
		for (GameTagEnum game : GameTagEnum.values()) {
			
			switch(game){
			case ANSWER:
				min = answerToDayNum;
				max = Integer.valueOf(paramConfig.getValue());
				synopsis = String.format(game.des(), DigitalConversionUtil.numberCapitalization(max));
				buttonIsShow = min < max ? StateType.OK_STATE.code() : StateType.NO_STATE.code();
				break;
			case FOLLOW:
				if (weChatIsConcerned == StateType.OK_STATE.code()) {
					continue;
				}
				min = (weChatIsConcerned == StateType.OK_STATE.code()) ? 1 : 0;
				max = 1;
				synopsis = String.format(game.des(), Integer.valueOf(weChatReward.getValue()));
				buttonIsShow = min < max ? StateType.OK_STATE.code() : StateType.NO_STATE.code();
				break;
			case SIGN_IN:
				min = dayNum;
				max = SignInEnum.TOTAL_DAY.code();
				synopsis = String.format(game.des(), Integer.valueOf(signInReward.getValue())* SignInEnum.TOTAL_DAY.code());
				buttonIsShow = isToDaySign == StateType.NO_STATE.code() ? StateType.OK_STATE.code() : StateType.OK_STATE.code();
				
				break;
			case AUTHENTICATION:
				if (realNameIsAuth == StateType.OK_STATE.code()) {
					continue;
				} 
				min = 0;
				max = 1;
				synopsis = String.format(game.des(), Integer.valueOf(realNameReward.getValue()), Misc.PLATFORM_CURRENCY_SYMBOL.value());
				buttonIsShow = min < max ? StateType.OK_STATE.code() : StateType.NO_STATE.code();
				break;
			default:
				break;
			}
			HomeTagResponse htr = new HomeTagResponse(
					game.key(), 
					String.format(game.ti(), min, max), 
					game.type(), (int)(((float)min)/max*100),
					game.but());
			htr.setSynopsis(synopsis);
			htr.setButtonIsShow(buttonIsShow);
			tags.add(htr);
		}
		return new HomeInfoResponse(level.getName(), fileStorageHandler.genDownloadUrl(level.getIconPath()),
				userAssert.getBalance(),
				userProperty.getPowerPoint(), todayPowerPoint, growthInfoList, tags);
		
	}
	
	@Override
	public PowerPointInfoResponse getPowerPointInfo(Long userId) throws Exception {
		//总元力值
		UserProperty userProperty = userPropertyRepository.getByUserId(userId);
		
		//今日元力
		BigDecimal todayPowerPoint = userMiningRecordRepository.getMiningRecord(userId, MiningActionEnum.codes(), MiningBonusTypeEnum.POWER_POINT.code());
		
		//简介
		BriefText briefText = briefTextRepository.getBriefText(BriefTextKeyEnum.ELEMENTARY_FORCE.name());
		
		//挖矿记录
		PageResult<MiningRecordResponse> pageResult = getMiningRecordList(MINING_TECORD_DEFAULT_PAGENO, MINING_TECORD_DEFAULT_PAGESIZE, userId, MiningBonusTypeEnum.POWER_POINT.code());
		setMiningRecordNextPagePath(pageResult, MiningBonusTypeEnum.POWER_POINT.code());
		
		return new PowerPointInfoResponse(userProperty.getPowerPoint(), todayPowerPoint.intValue(), briefText.getContent(), pageResult);
		
	}
	
	@Override
	public PageResult<MiningRecordResponse> getMiningRecordList(Integer pageNo, Integer pageSize, Long userId, Integer bonusType) throws Exception {
		
		bonusType = null == bonusType ? MiningBonusTypeEnum.POWER_POINT.code() : bonusType;
		
		List<UserMiningRecord> umrpList = new ArrayList<UserMiningRecord>();
		
		Page<UserMiningRecord> page = PageHelper.startPage(pageNo, pageSize, true);
		
		umrpList = userMiningRecordRepository.getByBonusType(userId, bonusType);
		List<MiningRecordResponse> mrList = umrpList.stream().map(e -> new MiningRecordResponse(MiningActionEnum.getValue(e.getAction()), e.getActionTime(), e.getBonus(), e.getBonusType())).collect(Collectors.toList());
		return new PageResult<MiningRecordResponse>(pageNo, pageSize, page.getTotal(), page.getPages(), mrList);
		
	}
	
	@Override
	public PlatformCurrencyInfoResponse getPlatformCurrencyInfo(Long userId) throws Exception {
		//总平台币
		UserAssert userAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		
		//今平台币
		BigDecimal todayPowerPoint = userMiningRecordRepository.getMiningRecord(userId, MiningActionEnum.codes(), MiningBonusTypeEnum.PLATFORM_CURRENCY.code());
		
		//简介
		BriefText briefText = briefTextRepository.getBriefText(BriefTextKeyEnum.PLATFORM_CURRENCY.name());
		
		//挖矿记录
		PageResult<MiningRecordResponse> pageResult = getMiningRecordList(MINING_TECORD_DEFAULT_PAGENO, MINING_TECORD_DEFAULT_PAGESIZE, userId, MiningBonusTypeEnum.PLATFORM_CURRENCY.code());
		setMiningRecordNextPagePath(pageResult, MiningBonusTypeEnum.PLATFORM_CURRENCY.code());
		
		return new PlatformCurrencyInfoResponse(userAssert.getBalance(), todayPowerPoint, briefText.getContent(), pageResult);
		
	}
	
	@Override
	public LevelInfoResponse getLevelInfo(Long userId) throws Exception {
		//当前用户等级信息
		UserAssert userAssert = userAssertRepository.queryUserAssert(userId, Misc.PLATFORM_CURRENCY_SYMBOL.value());
		
		UserLevel level = userLevelRepository.getLevelByMatchCondition(userAssert.getHistoryMaxBalance());

		//等级列表
		List<UserLevel> levelList = userLevelRepository.getUserLevel();
		List<LevelInfoResponse> lirList = new ArrayList<LevelInfoResponse>();
		for (UserLevel le : levelList) {
			lirList.add(new LevelInfoResponse(le.getName(), fileStorageHandler.genDownloadUrl(le.getIconPath()), le.getMatchCondition()));
		}
		BigDecimal amount = BigDecimal.ZERO;
		if (level.getId() !=  levelList.get(levelList.size()-1).getId()) {
			UserLevel nextLevel = userLevelRepository.getNextLevelByMatchCondition(userAssert.getHistoryMaxBalance());
			amount = amount.add(nextLevel.getMatchCondition().subtract(userAssert.getHistoryMaxBalance()));
		}

		return new LevelInfoResponse(
				userAssert.getHistoryMaxBalance(), level.getName(), 
				fileStorageHandler.genDownloadUrl(level.getIconPath()), amount,
				lirList);
		
	}
	
	@Override
	public FocusOnWechatInfoResponse getFocusOnWechatInfo(Long userId) throws Exception {
		List<UserMiningRecord> recordList = userMiningRecordRepository.getRecordByAction(userId, MiningActionEnum.FOCUS_ON_WECHAT.code());
		if (null != recordList && recordList.size() > 0) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您已关注过公众号！");
		}
		
		//简介
		BriefText briefText = briefTextRepository.getBriefText(BriefTextKeyEnum.FOCUS_ON_WECHAT.name());
		
		return new FocusOnWechatInfoResponse(briefText.getContent(), WECHAT_IMG_PATH);
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean wechatCheckCode(WechatCheckCodeRequest wechatCheckCodeRequest, Long userId) throws Exception {
		String code = weChatVerificationCodeRepository.getVerificationCode(wechatCheckCodeRequest.getCode());
		if (null != code) {
			//关注成功领取奖励
			ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.maxCode(), ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.minCode(), ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.name());
			int upset = userPropertyRepository.updatePowerPoint(userId, Integer.valueOf(paramConfig.getValue()));
			if (upset > 0) {
				//挖矿记录
				User user = userRepository.getUserById(userId);
				userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.FOCUS_ON_WECHAT.code(), System.currentTimeMillis(), BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())), MiningBonusTypeEnum.POWER_POINT.code()));
				return true;
			}
		}
		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "微信验证码错误！");
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public SignInPointResponse signInPoint(Long userId) throws Exception {
		List<UserMiningRecord> miningRecordList = userMiningRecordRepository.getRecordToDay(userId, MiningActionEnum.SIGN.code());
		if (null != miningRecordList && miningRecordList.size() > 0) {
			throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "您当天已签到！");
		}
		
		//签到成功领取奖励
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.ATTENTION_PUBLIC_NUMBER_POINT.maxCode(), ParamConfigSubclassEnum.SING_IN_POINT.minCode(), ParamConfigSubclassEnum.SING_IN_POINT.name());
		int upset = userPropertyRepository.updatePowerPoint(userId, Integer.valueOf(paramConfig.getValue()));
		if (upset > 0) {
			//挖矿记录
			User user = userRepository.getUserById(userId);
			userMiningRecordRepository.saveMiningRecord(new UserMiningRecord(userId, user.getInviterId(), MiningActionEnum.SIGN.code(), System.currentTimeMillis(), BigDecimal.valueOf(Double.valueOf(paramConfig.getValue())), MiningBonusTypeEnum.POWER_POINT.code()));
			
			List<UserMiningRecord> recordList = userMiningRecordRepository.getRecordByAction(userId, MiningActionEnum.SIGN.code());
			int dayNum = recordList.size();
			if (dayNum > SignInEnum.TOTAL_DAY.code()) {
				if (dayNum%SignInEnum.TOTAL_DAY.code() == 0) {
					dayNum = SignInEnum.TOTAL_DAY.code();
				} else {
					dayNum = dayNum%SignInEnum.TOTAL_DAY.code();
				}
			}
			
			return new SignInPointResponse(String.format("+%d元力", Integer.valueOf(paramConfig.getValue())), String.format("已签到%s天，继续加油吧", DigitalConversionUtil.numberCapitalization(dayNum)));
//			return String.format("第%s天+%d元力", DigitalConversionUtil.numberCapitalization(dayNum), Integer.valueOf(paramConfig.getValue()));
		} 
		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "签到失败！");
	}
	
	@Override
	public InviteFriendsParamResponse getInviteFriendsParam() throws Exception {
		ParamConfig invitationCurrency = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.INVITATION_CURRENCY.maxCode(), ParamConfigSubclassEnum.INVITATION_CURRENCY.minCode(), ParamConfigSubclassEnum.INVITATION_CURRENCY.name());
		ParamConfig maxInvitationPerDay = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_INVITATION_PER_DAY.name());
		ParamConfig miningRoyaltyRate = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MINING_ROYALTY_RATE.maxCode(), ParamConfigSubclassEnum.MINING_ROYALTY_RATE.minCode(), ParamConfigSubclassEnum.MINING_ROYALTY_RATE.name());
		
		
		return new InviteFriendsParamResponse(
				BigDecimal.valueOf(Double.valueOf(invitationCurrency.getValue())),
				Integer.valueOf(maxInvitationPerDay.getValue()),
				Integer.valueOf(miningRoyaltyRate.getValue()),
				Misc.PLATFORM_CURRENCY_SYMBOL.value());
	}
	
	
	/**
	 * 构建挖矿记录下一页请求路径
	 * @param <T>
	 * @param pageResult
	 * @param bonusType
	 */
	private <T> void setMiningRecordNextPagePath(PageResult<T> pageResult, Integer bonusType){
		pageResult.setNextPagePath(String.format(MINING_TECORD_NEXT_PAGEPATH, bonusType, MINING_TECORD_DEFAULT_PAGENO, MINING_TECORD_DEFAULT_PAGESIZE));
		pageResult.buildNextPagePath();
	}
	
}
