package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserAssert;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserAssertRepository {

    String ALL_FIELDS = "id, user_id, currency_symbol, balance, history_max_balance, wallet_address";
    String QUERY_ASSERTS = "<script>SELECT " + ALL_FIELDS + " FROM user_assert"
            + "<where>"
            + "<if test=\"userId != null\"> AND user_id = #{userId}</if>"
            + "<if test=\"currencySymbol != null\"> AND currency_symbol = #{currencySymbol}</if>"
            + "</where>"
            + "</script>";


    String QUERY_ASSERTS_FOR_USER_LIST = "<script>SELECT"
            + " ua.user_id AS userId,"
            + " ua.currency_symbol AS currencySymbol,"
            + " ua.balance AS balance,"
            + " IFNULL(mas.value, 2) AS unRestricted,"
            + " (SELECT IFNULL(SUM(uaf.amount), 0) from user_assert_freeze uaf WHERE uaf.user_id = ua.user_id AND uaf.currency_symbol = ua.currency_symbol) AS freezingAmount,"
            + " ua.wallet_address AS walletAddress"
            + " FROM user_assert ua"
            + " LEFT JOIN merchant_assert_statistics mas ON mas.currency_symbol = ua.currency_symbol AND mas.key = 'UNRESTRICTED'"
            + "<where>"
            + "<if test=\"userIds != null\"> AND ua.user_id IN <foreach collection='userIds' item='userId' open='(' close=')' separator=','> #{userId} </foreach></if>"
            + "</where>"
            + " ORDER BY (balance - freezingAmount) DESC"
            + "</script>";

    String UPDATE_ASSERT = "UPDATE user_assert SET balance = #{balance}, history_max_balance = #{historyMaxBalance}, wallet_address = #{walletAddress} WHERE id = #{id}";

    String STORE_ASSERT = "INSERT INTO user_assert(" + ALL_FIELDS + ") VALUES(#{id}, #{userId}, #{currencySymbol}, #{balance}, #{historyMaxBalance}, #{walletAddress})";

    @Select(QUERY_ASSERTS)
    List<UserAssert> query(UserAssert condition);

    @Select(QUERY_ASSERTS_FOR_USER_LIST)
    List<UserAssert> queryForUserList(UserAssert condition);

    @Update(UPDATE_ASSERT)
    void update(UserAssert userAssert);

    @Insert(STORE_ASSERT)
    void store(UserAssert userAssert);

}
