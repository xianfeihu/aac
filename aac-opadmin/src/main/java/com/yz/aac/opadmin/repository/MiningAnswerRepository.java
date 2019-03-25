package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MiningAnswer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MiningAnswerRepository {

    String ALL_FIELDS = "id, question_id, name, order_number, is_correct";
    String QUERY = "<script>SELECT " + ALL_FIELDS + " FROM mining_answer"
            + "<where>"
            + "<if test=\"questionIds != null\"> AND question_id IN <foreach collection='questionIds' item='questionId' open='(' close=')' separator=','> #{questionId} </foreach></if>"
            + "</where>"
            + "ORDER BY order_number"
            + "</script>";
    String STORE_ANSWER = "INSERT INTO mining_answer(" + ALL_FIELDS + ") VALUES(#{id}, #{questionId}, #{name}, #{orderNumber}, #{isCorrect})";
    String DELETE_BY_QUESTION = "DELETE FROM mining_answer WHERE question_id = #{questionId}";

    @Select(QUERY)
    List<MiningAnswer> query(MiningAnswer condition);

    @Insert(STORE_ANSWER)
    void store(MiningAnswer answer);

    @Delete(DELETE_BY_QUESTION)
    void deleteByQuestionId(Long questionId);

}
