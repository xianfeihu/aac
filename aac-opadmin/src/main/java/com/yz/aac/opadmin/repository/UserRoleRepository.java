package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRoleRepository {

    String ALL_FIELDS = "id, user_id, is_merchant, is_advertiser";
    String QUERY_USER_ROLES = "<script>SELECT " + ALL_FIELDS + " FROM user_role"
            + "<where>"
            + "<if test=\"userIds != null\"> AND user_id IN <foreach collection='userIds' item='userId' open='(' close=')' separator=','> #{userId} </foreach></if>"
            + "</where>"
            + "</script>";
    String STORE_USER_ROLE = "INSERT INTO user_role(" + ALL_FIELDS + ") VALUES(#{id}, #{userId}, #{isMerchant}, #{isAdvertiser})";
    String UPDATE_USER_ROLE = "UPDATE user_role SET user_id = #{userId}, is_merchant = #{isMerchant}, is_advertiser = #{isAdvertiser} WHERE id = #{id}";

    @Select(QUERY_USER_ROLES)
    List<UserRole> query(UserRole condition);

    @Update(UPDATE_USER_ROLE)
    void update(UserRole role);

    @Insert(STORE_USER_ROLE)
    void store(UserRole role);


}
