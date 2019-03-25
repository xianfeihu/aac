package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.repository.domian.User;

@Mapper
public interface UserRepository {

    String QUERY_USER_BY_ID = "SELECT * FROM user WHERE id = #{id}";

    String QUERY_USER_ID_BY_MERCHANT_ID = " SELECT u.id FROM `user` u JOIN merchant m USING (mobile_number) WHERE m.id=#{merchantId} ";

    String QUERY_MERCHANT_ID_BY_USER = "select m.id from user u "
    		+ "left join merchant m on m.mobile_number = u.mobile_number "
    		+ "where u.id = #{userId} ";
    
    String QUERY_USER_BY_MOBILE = "SELECT * FROM user WHERE mobile_number = #{mobile}";

    @Select(QUERY_USER_BY_ID)
    User getUserById(@Param("id") Long userId);
    
    @Select(QUERY_MERCHANT_ID_BY_USER)
    Long getMerchantIdByUser(@Param("userId") Long userId);
    
    @Select(QUERY_USER_BY_MOBILE)
    User getUserByMobile(@Param("mobile") Long mobile);

    @Select(QUERY_USER_ID_BY_MERCHANT_ID)
    Long getUserIdByMerchantId(@Param("merchantId") Long merchantId);
}
