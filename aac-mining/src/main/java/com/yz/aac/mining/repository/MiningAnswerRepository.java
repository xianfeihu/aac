package com.yz.aac.mining.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.MiningAnswer;

@Mapper
public interface MiningAnswerRepository {

    String QUERY_ANSWER_BY_QUESTION_ID = "select * from mining_answer where question_id = #{questionId} ORDER BY order_number asc";
    
    String QUERY_ANSWER_BY_QUESTION_AND_ID = "select * from mining_answer where question_id = #{questionId} and id = #{id}";
    
    @Select(QUERY_ANSWER_BY_QUESTION_ID)
    List<MiningAnswer> getAnswerByQuestionId(@Param("questionId") Long questionId);
    
    @Select(QUERY_ANSWER_BY_QUESTION_AND_ID)
    MiningAnswer getAnswerByQuestionIdToAnswer(@Param("questionId") Long questionId, @Param("id") Long id);
}
