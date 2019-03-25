package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserBehaviourStatisticsRepository {

    String update_VALUE_BY_KEY = " UPDATE user_behaviour_statistics ubs SET value=value + 1 WHERE user_id=#{userId} and ubs.key = #{key} ";

    /**
     * 用户行为次数统计加1
     * @param userId
     * @param key
     * @return
     */
    @Update(update_VALUE_BY_KEY)
    Integer updateBehaviourStatistics(@Param("userId") Long userId, @Param("key") String key);

}
