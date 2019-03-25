package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.UserIncomeStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserIncomeStatisticsRepository {

    String ALL_FIELDS = "id, user_id, currency_symbol, `key`, value";
    String QUERY_STATS = "<script>SELECT " + ALL_FIELDS + " FROM user_income_statistics"
            + "<where>"
            + "<if test=\"userId != null\"> AND user_id = #{userId}</if>"
            + "<if test=\"currencySymbol != null and currencySymbol != ''\"> AND currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"key != null\"> AND `key` = #{key}</if>"
            + "</where>"
            + "</script>";

    String UPDATE_STAT = "UPDATE user_income_statistics SET user_id = #{userId}, currency_symbol = #{currencySymbol}, `key` = #{key}, value = #{value} WHERE id = #{id}";

    @Select(QUERY_STATS)
    List<UserIncomeStatistics> query(UserIncomeStatistics condition);

    @Update(UPDATE_STAT)
    void update(UserIncomeStatistics item);

}
