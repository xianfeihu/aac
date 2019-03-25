package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.CreateQuestionRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionParticipatorRequest;
import com.yz.aac.opadmin.model.request.QueryQuestionRequest;
import com.yz.aac.opadmin.model.response.QueryQuestionParticipatorResponse;
import com.yz.aac.opadmin.model.response.QueryQuestionResponse;

public interface QuestionService {

    /**
     * 创建题目
     */
    void createQuestion(CreateQuestionRequest request) throws Exception;

    /**
     * 查询题目
     */
    QueryQuestionResponse queryQuestions(QueryQuestionRequest request) throws Exception;

    /**
     * 查询参与用户
     */
    QueryQuestionParticipatorResponse queryParticipators(QueryQuestionParticipatorRequest request) throws Exception;

    /**
     * 删除题目
     */
    void deleteQuestion(Long id) throws Exception;


}
