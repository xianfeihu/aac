package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.repository.domian.UserRole;

@Mapper
public interface UserRoleRepository {

    String QUERY_USER_ROLE_BY_UID = "SELECT * FROM user_role WHERE user_id = #{userId}";
    
    @Select(QUERY_USER_ROLE_BY_UID)
    UserRole getByUserId(@Param("userId") Long userId);
    
}
