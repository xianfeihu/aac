package com.yz.aac.wallet.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.UserMiningRecord;

@Mapper
public interface UserMiningRecordRepository {
    
    String SAVE_MINING_RECORD = "INSERT INTO user_mining_record(user_id, inviter_id, action, action_time, bonus, bonus_type)"
    		+ "VALUES(#{userId}, #{inviterId}, #{action}, #{actionTime}, #{bonus}, #{bonusType})";
    
    String QUERY_RECORD_TODAY ="SELECT * FROM user_mining_record WHERE user_id = #{userId} and  action = #{action} "
    		+ "AND from_unixtime(action_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
    
    @Insert(SAVE_MINING_RECORD)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveMiningRecord(UserMiningRecord userMiningRecord);
    
    /**
     * 获取当天类型记录
     * @param userId
     * @param action
     * @return
     */
    @Select(QUERY_RECORD_TODAY)
    List<UserMiningRecord> getRecordToDay(@Param("userId") Long userId, @Param("action") Integer action);
}
