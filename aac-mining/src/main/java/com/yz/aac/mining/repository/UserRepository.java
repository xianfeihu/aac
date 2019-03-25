package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRepository {

    String QUERY_USER_BY_ID = "SELECT * FROM user WHERE id = #{id}";
    
    String QUERY_USER_ALL = "select * from user u "
    		+ "left join user_property up on up.user_id = u.id "
    		+ "where up.status = #{status}";
    
    String QUERY_USER_BY_INVITERCODE = "SELECT * FROM user WHERE inviter_code = #{inviterCode}";

    String QUERY_USER_ID_BY_MERCHANT_ID = " SELECT u.id FROM `user` u JOIN merchant m USING (mobile_number) WHERE m.id=#{merchantId} ";

    String QUERY_INVITER_LIST = "select * from user where inviter_id = #{inviterId} and id_number is not null and from_unixtime(reg_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
    
    @Select(QUERY_USER_ID_BY_MERCHANT_ID)
    Long getUserIdByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_USER_BY_ID)
    User getUserById(@Param("id") Long userId);
    
    @Select(QUERY_USER_ALL)
    List<User> getUserAll(@Param("status") Integer status);
    
    @Select(QUERY_USER_BY_INVITERCODE)
    User getUserByInviterCode(@Param("inviterCode") String inviterCode);
    
    /**
     * 获取当天邀请人数
     * @param inviterCode
     * @return
     */
    @Select(QUERY_INVITER_LIST)
    List<User> getInviterList(@Param("inviterId") Long inviterId);
}
