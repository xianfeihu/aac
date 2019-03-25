package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.mining.repository.domian.UserProperty;

@Mapper
public interface UserPropertyRepository {

    String QUERY_USER_BY_UID = "SELECT * FROM user_property WHERE user_id = #{userId}";
    
    String UPDATE_POWER_POINT = "UPDATE user_property SET power_point = power_point + #{powerPoint} WHERE user_id = #{userId}";
    
    @Select(QUERY_USER_BY_UID)
    UserProperty getByUserId(@Param("userId") Long userId);
    
    @Update(UPDATE_POWER_POINT)
    int updatePowerPoint(@Param("userId") Long userId, @Param("powerPoint") Integer powerPoint);

}
