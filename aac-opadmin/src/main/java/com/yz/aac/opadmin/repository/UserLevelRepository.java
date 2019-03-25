package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserLevel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserLevelRepository {

    String ALL_FIELDS = "id, name, icon_path, match_condition";
    String QUERY_USER_LEVELS = "<script>SELECT " + ALL_FIELDS + " FROM user_level"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND name LIKE #{fixedName}</if>"
            + "<if test=\"accurateName != null and accurateName != ''\"> AND name = #{accurateName}</if>"
            + "</where>"
            + "ORDER BY match_condition DESC"
            + "</script>";
    String STORE_USER_LEVEL = "INSERT INTO user_level(" + ALL_FIELDS + ") VALUES(#{id}, #{name}, #{iconPath}, #{matchCondition})";
    String UPDATE_USER_LEVEL = "UPDATE user_level SET name = #{name}, icon_path = #{iconPath}, match_condition = #{matchCondition} WHERE id = #{id}";

    @Select(QUERY_USER_LEVELS)
    List<UserLevel> query(UserLevel userLevel);

    @Insert(STORE_USER_LEVEL)
    void store(UserLevel userLevel);

    @Update(UPDATE_USER_LEVEL)
    void update(UserLevel userLevel);

}
