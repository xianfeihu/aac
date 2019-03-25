package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.UserSmsCode;

@Mapper
public interface UserSmsCodeRepository {

    String SAVE_USER_SMS_CODE = "INSERT INTO user_sms_code(mobile_number, code, type, send_time) values(#{mobileNumber}, #{code}, #{type}, #{sendTime})";
    String QUERY_BY_MOBILE = "SELECT * FROM user_sms_code WHERE mobile_number = #{mobile} AND type = #{type}";
    String DELETE_BY_MOBILE = "DELETE FROM user_sms_code WHERE mobile_number = #{mobile} AND type = #{type}";
    
    String DELETE_SMS_CODE_REGULAR = "delete FROM user_sms_code WHERE TO_DAYS( FROM_UNIXTIME(send_time/1000,'%Y-%m-%d')) < TO_DAYS(NOW())";
    
    @Insert(SAVE_USER_SMS_CODE)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserSmsCode(UserSmsCode userSmsCode); 
    
    @Select(QUERY_BY_MOBILE)
    UserSmsCode getByMobile(@Param("mobile") Long mobile, @Param("type") Integer type);
    
    @Delete(DELETE_BY_MOBILE)
    void deleteByMobile(@Param("mobile") Long mobile, @Param("type") Integer type);
    
    /**
     * 删除昨天前数据
     * @return
     */
    @Delete(DELETE_SMS_CODE_REGULAR)
    int deleteSmsCodeRegular();

}
