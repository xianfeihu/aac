package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.Operator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OperatorRepository {

    String ALL_FIELDS = "id, login_name, password, name, department, status, role, create_time, update_time";
    String QUERY_OPERATORS = "<script>SELECT " + ALL_FIELDS + " FROM operator"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"loginName != null and loginName != ''\"> AND login_name = #{loginName}</if>"
            + "<if test=\"name != null and name != ''\"><bind name=\"fixedName\" value=\"'%' + name + '%'\" /> AND name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY create_time DESC"
            + "</script>";
    String STORE_OPERATOR = "INSERT INTO operator(" + ALL_FIELDS + ") VALUES(#{id}, #{loginName}, #{password}, #{name}, #{department}, #{status}, #{role}, #{createTime}, #{updateTime})";
    String UPDATE_OPERATOR = "UPDATE operator SET login_name = #{loginName}, password = #{password}, name = #{name}, department = #{department}, status = #{status}, role = #{role}, update_time = #{updateTime} WHERE id = #{id}";

    @Select(QUERY_OPERATORS)
    List<Operator> query(Operator operator);

    @Insert(STORE_OPERATOR)
    void store(Operator operator);

    @Update(UPDATE_OPERATOR)
    void update(Operator operator);

}
