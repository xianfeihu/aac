package com.yz.aac.wallet.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.UserBirthdayLoginFailRecord;

@Mapper
public interface UserBirthdayLoginFailRecordRepository {

    String SAVE_USER_BIRTHDAY_LOGIN_FAIL_RECORD = "INSERT INTO user_birthday_login_fail_record(user_id, record_time) values(#{userId}, #{recordTime})";
    String QUERY_WITHIN_TWENTY_FOUR_HOURS = "select * from user_birthday_login_fail_record where user_id = #{userId} and record_time >=(unix_timestamp(NOW() - interval 24 hour)*1000)";

    String DELETE_LONGIN_FAIL_RECORD = "delete FROM user_birthday_login_fail_record WHERE TO_DAYS( FROM_UNIXTIME(record_time/1000,'%Y-%m-%d')) < (TO_DAYS(NOW()) - 1)";
    
    String DELETE_RECORD_BY_USERID = "delete FROM user_birthday_login_fail_record WHERE user_id = #{userId}";
 
    @Insert(SAVE_USER_BIRTHDAY_LOGIN_FAIL_RECORD)
    int saveUserBirthdayLoginFailRecord(UserBirthdayLoginFailRecord userBirthdayLoginFailRecord);

    /**
     * 获取当前24小时数据
     * @param userId
     */
    @Select(QUERY_WITHIN_TWENTY_FOUR_HOURS)
    List<UserBirthdayLoginFailRecord> getUserTwentyFourDate(@Param("userId") Long userId);
    
    /**
     * 删除两天前数据
     * @return
     */
    @Delete(DELETE_LONGIN_FAIL_RECORD)
    int deleteBirthdayLonginFailRecord();
    
    @Delete(DELETE_RECORD_BY_USERID)
    int deleteRecordByUserId(@Param("userId") Long userId);
    
}
