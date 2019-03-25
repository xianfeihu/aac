package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserBehaviourStatisticsRepository {

    String ADD_VALUE_BY_KEY = " UPDATE user_behaviour_statistics SET `value` = `value` + #{value} WHERE user_id = #{userId} and `key` = #{key} ";

    @Update(ADD_VALUE_BY_KEY)
    Integer addBehaviourStatistics(@Param("userId") Long userId, @Param("key") String key, @Param("value") int value);

}
