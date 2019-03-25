package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserBehaviourStatistics;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserBehaviourStatisticsRepository {

    String ALL_FIELDS = "id, user_id, `key`, value";
    String QUERY_STATS = "<script>SELECT " + ALL_FIELDS + " FROM user_behaviour_statistics"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"userId != null\"> AND user_id = #{userId}</if>"
            + "<if test=\"key != null\"> AND `key` = #{key}</if>"
            + "</where>"
            + "</script>";

    String STORE_STAT = "INSERT INTO user_behaviour_statistics(" + ALL_FIELDS + ") VALUES(#{id}, #{userId}, #{key}, #{value})";
    String UPDATE_STAT = "UPDATE user_behaviour_statistics SET user_id = #{userId}, `key` = #{key}, value = #{value} WHERE id = #{id}";

    @Select(QUERY_STATS)
    List<UserBehaviourStatistics> query(UserBehaviourStatistics condition);

    @Insert(STORE_STAT)
    void store(UserBehaviourStatistics item);


    @Update(UPDATE_STAT)
    void update(UserBehaviourStatistics item);

}
