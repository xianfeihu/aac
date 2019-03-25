package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.CreateQuestionRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionParticipatorRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionRequest;
import com.yz.aac.opadmin.model.response.QueryQuestionParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryQuestionResponse;
import com.yz.aac.opadmin.repository.MiningAnswerRepository;
import com.yz.aac.opadmin.repository.MiningQuestionRepository;
import com.yz.aac.opadmin.repository.UserMiningRecordRepository;
import com.yz.aac.opadmin.repository.domain.MiningAnswer;
import com.yz.aac.opadmin.repository.domain.MiningQuestion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.DefaultItem.NO;
import static com.yz.aac.opadmin.Constants.DefaultItem.YES;
import static com.yz.aac.opadmin.Constants.ErrorMessage.EXISTS_NAME;
import static com.yz.aac.opadmin.Constants.ErrorMessage.ONE_CORRECT_ANSWER_REQUIRED;
import static com.yz.aac.opadmin.Constants.ErrorMessage.TARGET_DATA_MISSING;
import static com.yz.aac.opadmin.model.request.CreateQuestionRequest.QuestionAnswer;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private MiningQuestionRepository miningQuestionRepository;

    @Autowired
    private MiningAnswerRepository miningAnswerRepository;

    @Autowired
    private UserMiningRecordRepository userMiningRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createQuestion(CreateQuestionRequest request) throws Exception {
        //验证
        if (request.getAnswers().stream().filter(CreateQuestionRequest.QuestionAnswer::getCorrect).count() != 1) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), ONE_CORRECT_ANSWER_REQUIRED.value());
        }
        MiningQuestion param = new MiningQuestion();
        param.setIsSingleChoice(YES.value());
        param.setAccurateName(request.getName().trim());
        if (!miningQuestionRepository.query(param).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), EXISTS_NAME.value());
        }
        //保存题目
        MiningQuestion question = new MiningQuestion(null, YES.value(), request.getName().trim(), null,  request.getPowerPointBonus());
        miningQuestionRepository.store(question);
        //保存答案
        for (int i = 0; i < request.getAnswers().size(); i++) {
            QuestionAnswer answer = request.getAnswers().get(i);
            miningAnswerRepository.store(new MiningAnswer(
                    null, question.getId(), null, answer.getName().trim(), i + 1, answer.getCorrect() ? YES.value() : NO.value()
            ));
        }
    }

    @Override
    public QueryQuestionResponse queryQuestions(QueryQuestionRequest request) {
        MiningQuestion param = new MiningQuestion();
        param.setIsSingleChoice(YES.value());
        param.setName(StringUtils.isBlank(request.getName()) ? null : request.getName().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<MiningQuestion> page = (Page<MiningQuestion>) miningQuestionRepository.query(param);
        Set<Long> questionIds = page.getResult().stream().map(MiningQuestion::getId).collect(Collectors.toSet());
        List<QueryQuestionResponse.Item> items = new ArrayList<>();
        if (!questionIds.isEmpty()) {
            Map<Long, List<MiningAnswer>> answerMap = findAnswerMap(questionIds);
            items = page.getResult()
                    .stream()
                    .map(x -> new QueryQuestionResponse.Item(
                            x.getId(),
                            x.getName(),
                            answerMap.get(x.getId()).stream().map(a -> new QueryQuestionResponse.Item.QuestionAnswer(a.getName(), a.getIsCorrect())).collect(Collectors.toList()),
                            x.getPowerPointBonus()
                    )).collect(Collectors.toList());
        }
        return new QueryQuestionResponse(page.getTotal(), items);
    }

    @Override
    public QueryQuestionParticipatorResponse queryParticipators(QueryQuestionParticipatorRequest request) {
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryQuestionParticipatorResponse.Item> page = (Page<QueryQuestionParticipatorResponse.Item>) userMiningRecordRepository.queryQuestionParticipators(request);
        List<QueryQuestionParticipatorResponse.Item> items = page.getResult().stream()
                .map(x -> new QueryQuestionParticipatorResponse.Item(
                        USER_CODE_PREFIX.value() + x.getUserCode(),
                        x.getUserName(),
                        x.getTotalAnswers(),
                        x.getCorrectAnswers(),
                        x.getPowerPointBonus()
                )).collect(Collectors.toList());
        return new QueryQuestionParticipatorResponse(page.getTotal(), items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQuestion(Long id) throws Exception {
        //验证
        MiningQuestion param = new MiningQuestion();
        param.setId(id);
        if (miningQuestionRepository.query(param).isEmpty()) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), TARGET_DATA_MISSING.value());
        }
        //删除题目
        miningQuestionRepository.delete(id);
        //删除答案
        miningAnswerRepository.deleteByQuestionId(id);
    }

    private Map<Long, List<MiningAnswer>> findAnswerMap(Set<Long> questionIds) {
        MiningAnswer param = new MiningAnswer();
        param.setQuestionIds(questionIds);
        Map<Long, List<MiningAnswer>> resultMap = new HashMap<>();
        miningAnswerRepository.query(param).forEach(x -> {
            List<MiningAnswer> answers = resultMap.containsKey(x.getQuestionId()) ? resultMap.get(x.getQuestionId()) : new ArrayList<>();
            answers.add(x);
            resultMap.put(x.getQuestionId(), answers);
        });
        return resultMap;
    }
}
