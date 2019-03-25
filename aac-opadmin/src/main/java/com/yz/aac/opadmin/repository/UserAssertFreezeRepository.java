package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserAssertFreeze;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserAssertFreezeRepository {

    String ALL_FIELDS = "id, user_id, currency_symbol, amount, reason, action_time";
    String QUERY_FREEZING_ASSETS = "<script>SELECT " + ALL_FIELDS + " FROM user_assert_freeze"
            + "<where>"
            + "<if test=\"userId != null\"> AND user_id = #{userId}</if>"
            + "<if test=\"currencySymbol != null\"> AND currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"reason != null\"> AND reason = #{reason}</if>"
            + "</where>"
            + "ORDER BY action_time DESC"
            + "</script>";
    String QUERY_SUM_FREEZING_ASSETS = "<script>SELECT currency_symbol, reason, SUM(amount) AS amount FROM user_assert_freeze"
            + "<where>"
            + "<if test=\"userId != null\"> AND user_id = #{userId}</if>"
            + "</where>"
            + " GROUP BY currency_symbol, reason"
            + " ORDER BY currency_symbol, reason"
            + "</script>";

    String DELETE_FREEZING_ASSET = "DELETE FROM user_assert_freeze WHERE id = #{id}";

    String STORE_FREEZING_ASSET = "INSERT INTO user_assert_freeze(" + ALL_FIELDS + ") VALUES(#{id}, #{userId}, #{currencySymbol}, #{amount}, #{reason}, #{actionTime})";

    @Select(QUERY_FREEZING_ASSETS)
    List<UserAssertFreeze> query(UserAssertFreeze condition);

    @Select(QUERY_SUM_FREEZING_ASSETS)
    List<UserAssertFreeze> querySum(UserAssertFreeze condition);

    @Delete(DELETE_FREEZING_ASSET)
    void delete(Long id);

    @Insert(STORE_FREEZING_ASSET)
    void store(UserAssertFreeze item);

}
