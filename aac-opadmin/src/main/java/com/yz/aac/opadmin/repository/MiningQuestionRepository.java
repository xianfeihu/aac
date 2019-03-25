package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MiningQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MiningQuestionRepository {

    String ALL_FIELDS = "id, is_single_choice, name, power_point_bonus";
    String QUERY_QUESTIONS = "<script>SELECT " + ALL_FIELDS + " FROM mining_question"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"isSingleChoice != null\"> AND is_single_choice = #{isSingleChoice}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND name LIKE #{fixedName}</if>"
            + "<if test=\"accurateName != null and accurateName != ''\"> AND name = #{accurateName}</if>"
            + "</where>"
            + "ORDER BY id"
            + "</script>";
    String STORE_QUESTION = "INSERT INTO mining_question(" + ALL_FIELDS + ") VALUES(#{id}, #{isSingleChoice}, #{name}, #{powerPointBonus})";
    String DELETE_QUESTION = "DELETE FROM mining_question WHERE id = #{id}";


    @Select(QUERY_QUESTIONS)
    List<MiningQuestion> query(MiningQuestion condition);

    @Insert(STORE_QUESTION)
    @Options(useGeneratedKeys = true)
    void store(MiningQuestion question);

    @Delete(DELETE_QUESTION)
    void delete(Long id);

}
