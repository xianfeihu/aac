package com.yz.aac.mining.service.impl;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.mining.Constants.*;
import com.yz.aac.mining.model.request.SubmitAnswerRequest;
import com.yz.aac.mining.model.response.AnswerGameResponse;
import com.yz.aac.mining.model.response.AnswerResponse;
import com.yz.aac.mining.model.response.QuestionResponse;
import com.yz.aac.mining.repository.*;
import com.yz.aac.mining.repository.domian.*;
import com.yz.aac.mining.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService{
	
	@Autowired
	private BriefTextRepository briefTextRepository;
	
	@Autowired
	private UserChallengeQuestionsRecordRepository userChallengeQuestionsRecordRepository;
	
	@Autowired
	private UserMiningRecordRepository userMiningRecordRepository;
	
	@Autowired
	private ParamConfigRepository paramConfigRepository;
	
	@Autowired
	private MiningQuestionRepository miningQuestionRepository;
	
	@Autowired
	private MiningAnswerRepository miningAnswerRepository;
	
	@Autowired
	private UserPropertyRepository userPropertyRepository;
	
	@Override
	public AnswerGameResponse answerGameHomePage(Long userId) throws Exception {
		//答题活动规则
		BriefText briefText = briefTextRepository.getBriefText(BriefTextKeyEnum.ANSWER_GAME.name());
		
		//用户今日挑战数
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.maxCode(), ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.minCode(), ParamConfigSubclassEnum.MAX_PARTICIPATION_PER_DAY.name());
		UserChallengeQuestionsRecord record = userChallengeQuestionsRecordRepository.getTodayRecord(userId);
		Integer frequency = 0;
		if (null != record) {
			frequency = record.getFrequency();
		}
		//用户今日答题获得奖励值
		BigDecimal rewardVal = userMiningRecordRepository.getMiningRecord(userId, new Integer[]{MiningActionEnum.ANSWER.code()}, MiningBonusTypeEnum.POWER_POINT.code());
		
		return new AnswerGameResponse(
				(Integer.valueOf(paramConfig.getValue()) - frequency) < 0 ? 0 : Integer.valueOf(paramConfig.getValue()) - frequency,
						rewardVal.intValue(),
						briefText.getContent());
	}
	
	@Override
	public List<QuestionResponse> getQuestionList(Long userId) throws Exception {
		//随机获取系统设定的题目
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.QUESTIONS_PER_PARTICIPATION.maxCode(), ParamConfigSubclassEnum.QUESTIONS_PER_PARTICIPATION.minCode(), ParamConfigSubclassEnum.QUESTIONS_PER_PARTICIPATION.name());
		List<MiningQuestion> questionList = miningQuestionRepository.getQuestionRand(Integer.valueOf(paramConfig.getValue()));
		
		if(null == questionList || questionList.size() == 0){
			throw new BusinessException(ResponseMessageInfo.MSG_INTERACTIVE_EXCEPTION.code(), "敬请期待！");
		}
		List<QuestionResponse> resultList = new ArrayList<QuestionResponse>();
		//答案选项
		for (int i = 0; i < questionList.size(); i++) {
			List<MiningAnswer> answertList = miningAnswerRepository.getAnswerByQuestionId(questionList.get(i).getId());
			
			List<AnswerResponse> arList = answertList.stream().map(e -> new AnswerResponse(e.getId(), e.getQuestionId(), e.getOrderNumber(), e.getName(), e.getIsCorrect())).collect(Collectors.toList());
			resultList.add(new QuestionResponse(
					questionList.get(i).getName(), 
					i+1, 
					(i+1) > 3 ? questionList.get(i).getPowerPointBonus() : null, 
					arList));
		}
		
		return resultList;
	}
	
	@Override
	public String submitAnswer(SubmitAnswerRequest submitAnswerRequest, Long userId) throws Exception {
		//随机获取系统设定的题目
		MiningAnswer answert = miningAnswerRepository.getAnswerByQuestionIdToAnswer(submitAnswerRequest.getQuestionId(), submitAnswerRequest.getAnswerId());
		
		if (answert.getIsCorrect() == (StateType.OK_STATE.code())) {
			return "答题成功！";
		}
		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "答题失败");
	}
	
	@Override
	public String getAnswerResult(Long userId) throws Exception {
		
		UserChallengeQuestionsRecord record = userChallengeQuestionsRecordRepository.getTodayRecord(userId);
			
		ParamConfig paramConfig = paramConfigRepository.getParamConfig(ParamConfigSubclassEnum.QUESTIONS_PER_PARTICIPATION.maxCode(), ParamConfigSubclassEnum.QUESTIONS_PER_PARTICIPATION.minCode(), ParamConfigSubclassEnum.QUESTIONS_PER_PARTICIPATION.name());
    	if (record.getAnswerTimes() >= Integer.valueOf(paramConfig.getValue())) {
    		//答题成功领取奖励
    		return String.format("恭喜您获得“%d”元力，厉害！请再接再厉!", record.getPowerPointBonus());
    	} else {
    		String errcode = (record.getPowerPointBonus() > 0 ? String.format("您获得“%d”元力，厉害哦", record.getPowerPointBonus()) : "很遗憾！，本次答题您没有获取到元力，努力！");
    		throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), errcode);
    	}
	}
	
}
