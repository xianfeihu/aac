package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.UserRole;

@Mapper
public interface UserRoleRepository {

    String SAVE_USER_ROLE = "INSERT INTO user_role(user_id, is_merchant, is_advertiser) values(#{userId}, #{isMerchant}, #{isAdvertiser})";
    String QUERY_USER_ROLE_BY_UID = "SELECT * FROM user_role WHERE user_id = #{userId}";
    
    
    @Insert(SAVE_USER_ROLE)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserRole(UserRole userrole); 
    
    @Select(QUERY_USER_ROLE_BY_UID)
    UserRole getByUserId(@Param("userId") Long userId);
    
}
