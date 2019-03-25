package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.PlatformAssertIncomeExpenditureRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlatformAssertIncomeExpenditureRecordRepository {

    String ALL_FIELDS = "id, direction, user_id, user_name, action_time, amount, rmb_amount, action";
    String QUERY_RECORDS = "<script>SELECT r.id, r.direction, r.user_id, r.user_name, r.action_time, r.amount, r.rmb_amount, r.action FROM platform_assert_income_expenditure_record r"
            + " INNER JOIN user_role ur ON r.user_id = ur.user_id"
            + "<where>"
            + "<if test=\"id != null\"> AND r.id = #{id}</if>"
            + "<if test=\"direction != null\"> AND r.direction = #{direction}</if>"
            + "<if test=\"userRole == 2\"> AND r.action NOT IN (4) AND ur.is_merchant = 2 AND ur.is_advertiser = 2</if>"
            + "<if test=\"userRole == 3\"> AND r.action NOT IN (4) AND ur.is_merchant = 1</if>"
            + "<if test=\"userRole == 4\"> AND ur.is_advertiser = 1</if>"
            + "<if test=\"beginActionTime != null\"> AND r.action_time &gt;= #{beginActionTime}</if>"
            + "<if test=\"endActionTime != null\"> AND r.action_time &lt;= #{endActionTime}</if>"

            + "<if test=\"business == 1 and direction == 1\"> AND r.action != 1</if>"
            + "<if test=\"business == 2 and direction == 1\"> AND r.action = 1</if>"

            + "<if test=\"userName != null and userName != ''\"><bind name=\"fixedName\" value=\"'%' + userName + '%'\" /> AND r.user_name LIKE #{fixedName}</if>"
            + "</where>"
            + "ORDER BY action_time DESC"
            + "</script>";

    String QUERY_SUM_AMOUNT = "<script>SELECT SUM(r.amount) AS sumAmount, SUM(r.rmb_amount) AS sumRmbAmount FROM platform_assert_income_expenditure_record r"
            + " INNER JOIN user_role ur ON r.user_id = ur.user_id"
            + "<where>"
            + "<if test=\"id != null\"> AND r.id = #{id}</if>"
            + "<if test=\"direction != null\"> AND r.direction = #{direction}</if>"
            + "<if test=\"userRole == 2\"> AND r.action NOT IN (4) AND ur.is_merchant = 2 AND ur.is_advertiser = 2</if>"
            + "<if test=\"userRole == 3\"> AND r.action NOT IN (4) AND ur.is_merchant = 1</if>"
            + "<if test=\"userRole == 4\"> AND ur.is_advertiser = 1</if>"
            + "<if test=\"beginActionTime != null\"> AND r.action_time &gt;= #{beginActionTime}</if>"
            + "<if test=\"endActionTime != null\"> AND r.action_time &lt;= #{endActionTime}</if>"

            + "<if test=\"business == 1 and direction == 1\"> AND r.action != 1</if>"
            + "<if test=\"business == 2 and direction == 1\"> AND r.action = 1</if>"

            + "<if test=\"userName != null and userName != ''\"><bind name=\"fixedName\" value=\"'%' + userName + '%'\" /> AND r.user_name LIKE #{fixedName}</if>"
            + "</where>"
            + "</script>";
    String STORE_RECORD = "INSERT INTO platform_assert_income_expenditure_record(" + ALL_FIELDS + ") VALUES(#{id}, #{direction}, #{userId}, #{userName}, #{actionTime}, #{amount}, #{rmbAmount}, #{action})";

    @Select(QUERY_RECORDS)
    List<PlatformAssertIncomeExpenditureRecord> query(PlatformAssertIncomeExpenditureRecord condition);

    @Select(QUERY_SUM_AMOUNT)
    List<PlatformAssertIncomeExpenditureRecord> querySumAmount(PlatformAssertIncomeExpenditureRecord condition);

    @Insert(STORE_RECORD)
    void store(PlatformAssertIncomeExpenditureRecord record);
}
