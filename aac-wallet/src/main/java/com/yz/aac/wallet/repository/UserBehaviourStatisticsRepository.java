package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.UserBehaviourStatistics;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserBehaviourStatisticsRepository {

    String INSERT_USER_BEHAVIOUR_STATISTICS_BATCH = " <script>"
            +"INSERT INTO user_behaviour_statistics (user_id,`key`,value) VALUES "
            + "<foreach collection=\"keyList\" item=\"ky\" separator=\",\">"
            + "(#{userId},#{ky},#{value}) "
            + "</foreach> "
            + "</script>";

    String INSERT_USER_BEHAVIOUR_STATISTICS = "INSERT INTO user_behaviour_statistics (user_id,`key`,value) VALUES (#{userId},#{ky},#{value})";

    String ADD_VALUE_BY_KEY = " UPDATE user_behaviour_statistics ubs SET value=value+#{addValue} WHERE user_id=#{userId} and ubs.key = #{key} ";

    @Update(ADD_VALUE_BY_KEY)
    Integer addBehaviourStatistics(@Param("userId") Long userId, @Param("key") String key, @Param("addValue") int addValue);

    @Insert(INSERT_USER_BEHAVIOUR_STATISTICS_BATCH)
    void batchAdd(@Param("userId") Long userId, @Param("keyList") List<String> keyList, @Param("value") Integer value);

    @Insert(INSERT_USER_BEHAVIOUR_STATISTICS)
    void add(UserBehaviourStatistics userBehaviourStatistics);
}
