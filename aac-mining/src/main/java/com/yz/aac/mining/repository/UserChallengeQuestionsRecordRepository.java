package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.mining.repository.domian.UserChallengeQuestionsRecord;

@Mapper
public interface UserChallengeQuestionsRecordRepository {

    String QUERY_RECORD_BY_TODAY = "SELECT * FROM user_challenge_questions_record WHERE user_id = #{userId}  AND from_unixtime(create_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d') ORDER BY create_time";
    
    String SAVE_USER_QUESTIONS_RECORD = "INSERT INTO user_challenge_questions_record(user_id, frequency, answer_number, power_point_bonus,create_time,answer_times) "
    		+ "values(#{userId}, #{frequency}, #{answerNumber}, #{powerPointBonus}, #{createTime}, #{answerTimes})";
    
    String UPDATE_USER_QUESTIONS_RECORD = "update user_challenge_questions_record set frequency = frequency + 1, answer_number = 0, power_point_bonus = 0, answer_times=0 where user_id =#{userId} AND from_unixtime(create_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
    
    String UPDATE_ANSWER_NUMBER = "update user_challenge_questions_record set answer_number = answer_number + 1 where user_id =#{userId} AND from_unixtime(create_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
    
    String UPDATE_POWER_POINT_BONUS = "update user_challenge_questions_record set power_point_bonus = power_point_bonus + #{powerPointBonus}, answer_times = answer_times + 1 where user_id =#{userId} AND from_unixtime(create_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
    
    @Select(QUERY_RECORD_BY_TODAY)
    UserChallengeQuestionsRecord getTodayRecord(@Param("userId") Long userId);

    @Insert(SAVE_USER_QUESTIONS_RECORD)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserChallengeQuestionsRecord(UserChallengeQuestionsRecord userChallengeQuestionsRecord);
    
    @Update(UPDATE_USER_QUESTIONS_RECORD)
    int updateUserChallengeQuestionsRecord(@Param("userId") Long userId);
    
    @Update(UPDATE_ANSWER_NUMBER)
    int updateAnswerNumber(@Param("userId") Long userId);
    
    @Update(UPDATE_POWER_POINT_BONUS)
    int updatePowerPointBonus(@Param("userId") Long userId, @Param("powerPointBonus") Integer powerPointBonus);
}
