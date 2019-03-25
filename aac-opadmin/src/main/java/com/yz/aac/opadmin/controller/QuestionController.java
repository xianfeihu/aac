package com.yz.aac.opadmin.controller;

import com.yz.aac.common.controller.BaseController;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.opadmin.model.request.CreateQuestionRequest;
import com.yz.aac.opadmin.model.request.PagingRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionParticipatorRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionRequest;
import com.yz.aac.opadmin.model.response.QueryQuestionParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryQuestionResponse;
import com.yz.aac.opadmin.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.ResponseMessageInfo.*;
import static com.yz.aac.common.model.response.RootResponse.buildSuccess;
import static com.yz.aac.opadmin.Constants.RequestFiled.*;
import static com.yz.aac.opadmin.model.request.CreateQuestionRequest.QuestionAnswer;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/questions")
@Slf4j
@Api(tags = "答题（app内容管理-每日答题）")
public class QuestionController extends BaseController {

    @Autowired
    private QuestionService questionService;

    @ApiOperation("创建题目")
    @PutMapping
    @ResponseBody
    public RootResponse<?> createQuestion(@RequestBody CreateQuestionRequest request) throws Exception {
        validateRequired(NAME.value(), request.getName());
        validateRequired(POWER_POINT_BONUS.value(), request.getPowerPointBonus());
        validateStringLength(NAME.value(), request.getName(), 1, 30);
        validateIntRange(POWER_POINT_BONUS.value(), request.getPowerPointBonus(), 1, 999);
        if (CollectionUtils.isEmpty(request.getAnswers()) || request.getAnswers().size() < 2 || request.getAnswers().size() > 4) {
            throw new BusinessException(MSG_INVALID_FILED_LENGTH.code(), MSG_INVALID_FILED_LENGTH.message()
                    .replaceFirst(PLACEHOLDER.value(), ANSWERS.value())
                    .replaceFirst(PLACEHOLDER.value(), "2")
                    .replaceFirst(PLACEHOLDER.value(), "4")
            );
        }
        for (int i = 0; i < request.getAnswers().size(); i++) {
            QuestionAnswer answer = request.getAnswers().get(i);
            String answerNameFiled = buildFiledName(ANSWERS.value(), i, NAME.value());
            String answerCorrectFiled = buildFiledName(ANSWERS.value(), i, CORRECT.value());
            validateRequired(answerNameFiled, answer.getName());
            validateRequired(answerCorrectFiled, answer.getCorrect());
            validateStringLength(answerNameFiled, answer.getName(), 1, 10);
        }
        questionService.createQuestion(request);
        return buildSuccess(null);
    }

    @ApiOperation("查询题目")
    @GetMapping
    @ResponseBody
    public RootResponse<QueryQuestionResponse> queryQuestions(
            @ApiParam(name = "keyword", value = "题目关键字") @RequestParam(value = "keyword", required = false) String keyword,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        if (StringUtils.isNotBlank(keyword)) {
            validateStringLength(KEYWORD.value(), keyword, 1, 30);
            validateSpecialChar(KEYWORD.value(), keyword);
        }
        return buildSuccess(questionService.queryQuestions(new QueryQuestionRequest(keyword, new PagingRequest(pageNumber, pageSize))));
    }

    @ApiOperation("删除题目")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RootResponse<?> deleteQuestion(
            @ApiParam(name = "id", value = "题目ID", required = true) @PathVariable(value = "id") Long id
    ) throws Exception {
        questionService.deleteQuestion(id);
        return buildSuccess(null);
    }

    @ApiOperation("查询参与用户")
    @GetMapping("/participators")
    @ResponseBody
    public RootResponse<QueryQuestionParticipatorResponse> queryParticipators(
            @ApiParam(name = "name", value = "用户姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(name = "minCorrect", value = "答对题数下限") @RequestParam(value = "minCorrect", required = false) Integer minCorrect,
            @ApiParam(name = "maxCorrect", value = "答对题数上限") @RequestParam(value = "maxCorrect", required = false) Integer maxCorrect,
            @ApiParam(name = "pageNumber", value = "页号", required = true) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(name = "pageSize", value = "每页数据量", required = true) @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) throws Exception {
        if (StringUtils.isBlank(name) && null == minCorrect && null == maxCorrect) {
            throw new BusinessException(MSG_ONE_REQUIRED.code(), MSG_ONE_REQUIRED.message());
        }
        validateRequired(PAGE_NUMBER.value(), pageNumber);
        validateRequired(PAGE_SIZE.value(), pageSize);
        if (StringUtils.isNotBlank(name)) {
            validateStringLength(NAME.value(), name, 1, 10);
            validateSpecialChar(NAME.value(), name);
        }
        if (null != minCorrect && minCorrect < 0) {
            throw new BusinessException(MSG_INVALID_FILED.code(), MSG_INVALID_FILED.message()
                    .replaceFirst(PLACEHOLDER.value(), MIN_CORRECT.value())
                    .replaceFirst(PLACEHOLDER.value(), String.valueOf(minCorrect))
            );
        }
        if (null != maxCorrect && maxCorrect < 0) {
            throw new BusinessException(MSG_INVALID_FILED.code(), MSG_INVALID_FILED.message()
                    .replaceFirst(PLACEHOLDER.value(), MAX_CORRECT.value())
                    .replaceFirst(PLACEHOLDER.value(), String.valueOf(maxCorrect))
            );
        }
        if (null != minCorrect && null != maxCorrect && maxCorrect < minCorrect) {
            throw new BusinessException(MSG_INVALID_RANGE.code(), MSG_INVALID_RANGE.message()
                    .replaceFirst(PLACEHOLDER.value(), MIN_CORRECT.value())
                    .replaceFirst(PLACEHOLDER.value(), MAX_CORRECT.value())
            );
        }
        return buildSuccess(questionService.queryParticipators(new QueryQuestionParticipatorRequest(
                name, minCorrect, maxCorrect, new PagingRequest(pageNumber, pageSize)
        )));
    }

    private String buildFiledName(String objName, int index, String propName) {
        return String.format("%s[%d].%s", objName, index, propName);
    }

}