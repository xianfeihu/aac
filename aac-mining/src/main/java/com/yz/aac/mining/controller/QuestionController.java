package com.yz.aac.mining.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.mining.model.request.SubmitAnswerRequest;
import com.yz.aac.mining.model.response.AnswerGameResponse;
import com.yz.aac.mining.model.response.QuestionResponse;
import com.yz.aac.mining.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.yz.aac.common.model.response.RootResponse.buildSuccess;

@Api(tags = "答题游戏")
@RequestMapping("/question")
@RestController
public class QuestionController extends BaseController {
	
	@Autowired
	private QuestionService questionService;
	
	@ApiOperation("答题游戏首页")
	@GetMapping("/answerGameHomePage")
	public RootResponse<AnswerGameResponse> answerGameHomePage() throws Exception{
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
		return buildSuccess(questionService.answerGameHomePage(userId));
		
	}
	
	@ApiOperation("获取题目")
	@GetMapping("/getQuestionList")
	public RootResponse<List<QuestionResponse>> getQuestionList() throws Exception{
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
		return buildSuccess(questionService.getQuestionList(userId));
		
	}
	
	@ApiOperation("提交答案")
	@PostMapping("/submitAnswer")
	public RootResponse<String> submitAnswer(@RequestBody SubmitAnswerRequest submitAnswerRequest) throws Exception{
		this.validateRequired("题目ID", submitAnswerRequest.getQuestionId());
		this.validateRequired("答案ID", submitAnswerRequest.getAnswerId());
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
		return buildSuccess(questionService.submitAnswer(submitAnswerRequest, userId));
		
	}
	
	@ApiOperation("返回答题挑战结果")
	@PostMapping("/getAnswerResult")
	public RootResponse<String> getAnswerResult() throws Exception{
		
		LoginInfo userInfo = getLoginInfo();
		Long userId = userInfo.getId();
		
		return buildSuccess(questionService.getAnswerResult(userId));
		
	}
	
}
