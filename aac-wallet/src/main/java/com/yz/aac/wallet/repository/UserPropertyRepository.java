package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.wallet.repository.domain.UserProperty;

@Mapper
public interface UserPropertyRepository {

    String SAVE_USER_PROPERTY = "INSERT INTO user_property(user_id, power_point, increase_strategy_id, status) "
    		+ "values(#{userId}, #{powerPoint}, #{increaseStrategyId}, #{status})";
    
    String QUERY_USER_BY_UID = "SELECT * FROM user_property WHERE user_id = #{userId}";
    
    String UPDATE_POWER_POINT = "UPDATE user_property SET power_point = power_point + #{powerPoint} WHERE user_id = #{userId}";
    
    String UPDATE_STATUS = "UPDATE user_property SET status = #{status},status_description = #{statusDescription} WHERE user_id = #{userId}";
    
    String UPDATE_REAL_NAME_CRT_TIME = "UPDATE user_property SET real_name_crt_time = #{realNameCrtTime} WHERE user_id = #{userId}";

    @Insert(SAVE_USER_PROPERTY)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserProperty(UserProperty userProperty);
    
    @Select(QUERY_USER_BY_UID)
    UserProperty getByUserId(@Param("userId") Long userId);
    
    @Update(UPDATE_POWER_POINT)
    void updatePowerPoint(@Param("userId") Long userId, @Param("powerPoint") Integer powerPoint);
    
    @Update(UPDATE_STATUS)
    void updateStatus(@Param("userId") Long userId, @Param("status") Integer status, @Param("statusDescription") String statusDescription);

    @Update(UPDATE_REAL_NAME_CRT_TIME)
    void updateRealNameCrtTime(@Param("userId") Long userId, @Param("realNameCrtTime") Long realNameCrtTime);

}
