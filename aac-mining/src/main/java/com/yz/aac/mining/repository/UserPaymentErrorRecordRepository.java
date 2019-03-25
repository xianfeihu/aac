package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.UserPaymentErrorRecord;

@Mapper
public interface UserPaymentErrorRecordRepository {

    String SAVE_PAYMENT_ERROR_RECORD = "INSERT INTO user_payment_error_record(user_id, create_time) "
			+ "values(#{userId}, #{createTime})";
    
    String DELETE_PAYMENT_ERROR_RECORD_BY_USERID = "DELETE FROM user_payment_error_record WHERE user_id = #{userId}";
    
    String QUERY_PAYMENT_ERROR_RECORD = "SELECT count(*) FROM user_payment_error_record WHERE user_id = #{userId} AND from_unixtime(create_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d') ";
    
    String QUERY_PAYMENT_ERROR_RECORD_BY_TIME = "select * from user_payment_error_record where user_id = #{userId} and "
    		+ "create_time >=(unix_timestamp(NOW() - interval #{minute} MINUTE)*1000) "
    		+ "ORDER BY create_time desc limit 1 ";
    
    @Select(QUERY_PAYMENT_ERROR_RECORD)
    int queryPaymentErrorRecord(@Param("userId") Long userId);
    
    /**
     * minute 分钟内最后一条数据
     * @param minute
     * @return
     */
    @Select(QUERY_PAYMENT_ERROR_RECORD_BY_TIME)
    UserPaymentErrorRecord queryPaymentErrorRecordByTime(@Param("userId") Long userId, @Param("minute") Integer minute);
    
    @Delete(DELETE_PAYMENT_ERROR_RECORD_BY_USERID)
    int deletePaymentErrorRecordByUserId(@Param("userId") Long userId);
    
    @Insert(SAVE_PAYMENT_ERROR_RECORD)
    int savePaymentErrorRecord(UserPaymentErrorRecord userPaymentErrorRecord);
}
