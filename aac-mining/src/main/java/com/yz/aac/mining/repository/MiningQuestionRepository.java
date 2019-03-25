package com.yz.aac.mining.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.MiningQuestion;

@Mapper
public interface MiningQuestionRepository {

    String QUERY_QUESTION_RAND = "select * FROM mining_question ORDER BY RAND() LIMIT #{randNum}";
    
    String QUERY_QUESTION_BY_ID = "select * FROM mining_question where id = #{id}";
    
    @Select(QUERY_QUESTION_RAND)
    List<MiningQuestion> getQuestionRand(@Param("randNum") Integer randNum);
    
    @Select(QUERY_QUESTION_BY_ID)
    MiningQuestion getQuestionById(@Param("id") Long id);
}
