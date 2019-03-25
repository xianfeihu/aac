package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.model.request.MobileEntityRequest;
import com.yz.aac.wallet.model.response.PersonalIndexMsgResponse;
import com.yz.aac.wallet.repository.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserRepository {

    String QUERY_USER_BY_MOBILE = "SELECT * FROM user WHERE mobile_number = #{mobile}";
    String SAVE_USER = "INSERT INTO user(mobile_number, source, reg_time, inviter_id, inviter_code, registration_type) values(#{mobileNumber}, #{source}, #{regTime}, #{inviterId}, #{inviterCode}, #{registrationType})";
    String QUERY_USER_BY_ID = "SELECT * FROM user WHERE id = #{id}";
    String QUERY_USER_BY_WALLET_ADDRESS = "SELECT u.* FROM user u,user_assert ua WHERE ua.user_id=u.id and ua.wallet_address=#{sendAddr}";
    String UPDATE_USER_IDCARD = "UPDATE user SET id_number = #{idNumber}, name = #{name}, gender = #{gender} where id = #{userId}";
    String QUERY_USER_BY_INVITERCODE = "SELECT * FROM user WHERE inviter_code = #{inviterCode}";
    String UPDATE_USER_PASSWORD = "UPDATE user SET payment_password = #{passWord} where mobile_number = #{mobile}";
    String QUERY_INDEX_BASE_MSG = " SELECT u.id,u.name,IFNULL(up.power_point,0) power_point,IFNULL(ua.balance,0) plat_coin,IFNULL(ubs.value,0) transactions_count FROM user u LEFT JOIN user_assert ua ON ua.user_id=u.id AND ua.currency_symbol=#{currencySymbol} LEFT JOIN user_property up ON up.user_id=ua.user_id LEFT JOIN user_behaviour_statistics ubs ON ubs.user_id=up.user_id AND ubs.key=#{statisticsKey} WHERE u.id=#{userId} ";
    String QUERY_LEVEL_BY_PLAT_COIN = " SELECT `name` FROM user_level WHERE match_condition<=(SELECT history_max_balance FROM user_assert WHERE currency_symbol=#{currencySymbol} AND user_id=#{userId} LIMIT 1) ORDER BY match_condition desc LIMIT 1 ";
    String QUERY_NAME_BY_WALLET_ADDRESS = " SELECT u.name FROM user u JOIN user_assert ua ON ua.user_id=u.id WHERE ua.wallet_address=#{walletAddress} ";
    String QUERY_USER_ID_BY_MERCHANT_ID = " SELECT u.id FROM `user` u JOIN merchant m USING (mobile_number) WHERE m.id=#{merchantId} ";

    String QUERY_USER_BY_IDNUMBER = "SELECT * FROM user WHERE id_number = #{idNumber}";

    String QUERY_USER_BY_MOBILE_OR_ID_NUMBER = "SELECT * FROM user WHERE mobile_number = #{mobile} OR id_number = #{idNumber} LIMIT 1";

    @Select(QUERY_USER_BY_MOBILE)
    User getUserByMobile(@Param("mobile") Long mobile);
 
    @Insert(SAVE_USER)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUser(User user);

    @Select(QUERY_USER_BY_ID)
    User getUserById(@Param("id") Long userId);

    @Select(QUERY_USER_BY_WALLET_ADDRESS)
    User getUserByWalletAddress(@Param("sendAddr") String sendAddr);
    @Update(UPDATE_USER_IDCARD)
    void updateUserIDCard(@Param("idNumber") String idNumber, @Param("name") String name,
    		@Param("gender") Integer gender, @Param("userId") Long userId);
    
    @Select(QUERY_USER_BY_INVITERCODE)
    User getUserByInviterCode(@Param("inviterCode") String inviterCode);

    @Update(UPDATE_USER_PASSWORD)
    int updatePassWord(MobileEntityRequest mobileEntityRequest);

    @Select(QUERY_INDEX_BASE_MSG)
    PersonalIndexMsgResponse getIndexMsg(@Param("currencySymbol") String currencySymbol,@Param("statisticsKey") String statisticsKey,@Param("userId") Long userId);

    @Select(QUERY_LEVEL_BY_PLAT_COIN)
    String getUserLevel(@Param("currencySymbol") String currencySymbol,@Param("userId") Long userId);

    @Select(QUERY_NAME_BY_WALLET_ADDRESS)
    String getNameByWalletAddress(@Param("walletAddress") String walletAddress);
    
    @Select(QUERY_USER_BY_IDNUMBER)
    User queryUserByIdNumber(@Param("idNumber") String idNumber);

    @Select(QUERY_USER_ID_BY_MERCHANT_ID)
    Long getUserIdByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_USER_BY_MOBILE_OR_ID_NUMBER)
    User getUserByMobileOrIdCard(@Param("mobile") Long mobile, @Param("idNumber") String idNumber);
}
