package com.yz.aac.mining.service;

import java.util.List;

import com.yz.aac.mining.model.request.SubmitAnswerRequest;
import com.yz.aac.mining.model.response.AnswerGameResponse;
import com.yz.aac.mining.model.response.QuestionResponse;


/**
 * APP答题服务
 *
 */
public interface QuestionService {
	
	/**
	 * 答题活动首页信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	AnswerGameResponse answerGameHomePage(Long userId) throws Exception;
	
	/**
	 * 答题列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<QuestionResponse> getQuestionList(Long userId) throws Exception;
	
	/**
	 * 题目作答
	 * @param submitAnswerRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String submitAnswer(SubmitAnswerRequest submitAnswerRequest, Long userId) throws Exception;
	
	/**
	 * 获取本次作答题目结果
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getAnswerResult(Long userId) throws Exception;
	
}
