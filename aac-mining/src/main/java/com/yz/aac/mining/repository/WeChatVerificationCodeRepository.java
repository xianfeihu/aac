package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WeChatVerificationCodeRepository {

    String SAVE_VERIFICATION_CODE = "INSERT INTO we_chat_verification_code(code) VALUES(#{code})";
    
    String QUERY_VERIFICATION_CODE = "SELECT code FROM we_chat_verification_code WHERE code = #{code}";
    
    String DELETE_VERIFICATION_CODE = "DELETE FROM we_chat_verification_code WHERE code = #{code}";
    
    @Insert(SAVE_VERIFICATION_CODE)
    int saveVerificationCode(@Param("code") String code);
    
    @Select(QUERY_VERIFICATION_CODE)
    String getVerificationCode(@Param("code") String code);
    
    @Delete(DELETE_VERIFICATION_CODE)
    int deleteVerificationCode(@Param("code") String code);
    
}
