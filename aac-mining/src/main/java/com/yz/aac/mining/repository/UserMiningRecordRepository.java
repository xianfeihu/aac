package com.yz.aac.mining.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.UserMiningRecord;

@Mapper
public interface UserMiningRecordRepository {

    String QUERY_USER_MINING_RECORD = "<script> "
    		+ "SELECT case when sum(bonus) is null then 0 else sum(bonus) end as bonus "
    		+ "FROM user_mining_record "
    		+ "WHERE user_id = #{userId} "
    		+ "<if test=\"actions != null and actions.length > 0\"> "
    		+ "and action in "
    		+ "<foreach collection='actions' item='ac' open='(' close=')' separator=','>"
    		+ "#{ac} "
    		+ "</foreach>"
    		+ "</if> "
    		+ "and  bonus_type = #{bonusType} AND from_unixtime(action_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d') "
    		+ "</script>";
    		
    String QUERY_BY_BONUS_TYPE = "SELECT action ,max(action_time) as actionTime, sum(bonus) as bonus,max(bonus_type) as bonusType FROM( "
    		+ "SELECT * FROM user_mining_record WHERE user_id = #{userId} and  bonus_type = #{bonusType} "
    		+ "AND from_unixtime(action_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d') ORDER BY action_time"
    		+ ") as tableA group by action";
    
    String SAVE_MINING_RECORD = "INSERT INTO user_mining_record(user_id, inviter_id, action, action_time, bonus, bonus_type)"
    		+ "VALUES(#{userId}, #{inviterId}, #{action}, #{actionTime}, #{bonus}, #{bonusType})";
   
    String QUERY_RECORD_BY_ACTION ="SELECT * FROM user_mining_record WHERE user_id = #{userId} and  action = #{action} ";
    
    String QUERY_RECORD_TODAY ="SELECT * FROM user_mining_record WHERE user_id = #{userId} and  action = #{action} "
    		+ "AND from_unixtime(action_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
    
    String QUERY_RECORD_YESTERDAY ="select case when sum(bonus) is null then 0 else sum(bonus) end from user_mining_record where user_id = #{userId} and bonus_type = #{bonusType} "
    		+ "and from_unixtime(action_time/1000,'%Y-%m-%d') = date_format(DATE_SUB(curdate(),INTERVAL 1 DAY) ,'%Y-%m-%d')";
    
    /**
     * 根据类型获取今日奖励值
     * @param userId 用户ID
     * @param actions 挖矿类型
     * @param bonusType 奖励类型
     * @return
     */
    @Select(QUERY_USER_MINING_RECORD)
    BigDecimal getMiningRecord(@Param("userId") Long userId, @Param("actions") Integer[] actions, @Param("bonusType") Integer bonusType);

    /**
     * 当天用户挖矿总和
     * @param userId
     * @param bonusType
     * @return
     */
    @Select(QUERY_BY_BONUS_TYPE)
    List<UserMiningRecord> getByBonusType(@Param("userId") Long userId, @Param("bonusType") Integer bonusType);

    @Insert(SAVE_MINING_RECORD)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveMiningRecord(UserMiningRecord userMiningRecord);
    
    @Select(QUERY_RECORD_BY_ACTION)
    List<UserMiningRecord> getRecordByAction(@Param("userId") Long userId, @Param("action") Integer action);
    
    /**
     * 获取当天类型记录
     * @param userId
     * @param action
     * @return
     */
    @Select(QUERY_RECORD_TODAY)
    List<UserMiningRecord> getRecordToDay(@Param("userId") Long userId, @Param("action") Integer action);
    
    /**
     * 获取昨日某种奖励挖矿总量
     * @param userId
     * @param action
     * @return
     */
    @Select(QUERY_RECORD_YESTERDAY)
    BigDecimal getRecordYesterday(@Param("userId") Long userId, @Param("bonusType") Integer bonusType);
}
