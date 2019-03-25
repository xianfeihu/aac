package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserPropertyRepository {

    String ALL_FIELDS = "id, user_id, power_point, increase_strategy_id, status, status_description";
    String QUERY_USER_PROPERTIES = "<script>SELECT " + ALL_FIELDS + " FROM user_property"
            + "<where>"
            + "<if test=\"userId != null\"> AND user_id = #{userId}</if>"
            + "<if test=\"increaseStrategyId != null\"> AND increase_strategy_id = #{increaseStrategyId}</if>"
            + "</where>"
            + "</script>";
    String UPDATE_USER_PROPERTY = "UPDATE user_property SET user_id = #{userId}, power_point = #{powerPoint}, increase_strategy_id = #{increaseStrategyId}, status = #{status}, status_description = #{statusDescription} WHERE id = #{id}";

    @Select(QUERY_USER_PROPERTIES)
    List<UserProperty> query(UserProperty prop);

    @Update(UPDATE_USER_PROPERTY)
    void update(UserProperty up);
}
