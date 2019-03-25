package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.UserAssert;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface UserAssertRepository {

    String QUERY_USER_ASSETS_BY_CURRENCY_SYMBOL = " SELECT balance FROM user_assert WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} ";
    
    String UPDATE_USER_ASSET_SYNCHRONIZED_TIME = " UPDATE user_assert SET synchronized_time = #{synchronizedTime},balance = balance + #{balance},history_max_balance = history_max_balance + #{balance} WHERE id=#{id} ";

    String QUERY_USERS_ASSETS = " select id,user_id,currency_symbol,balance,history_max_balance,wallet_address,synchronized_time from user_assert where user_id=#{userId} and currency_symbol=#{coinType} limit 1 ";
    
    String SAVE_USER_ASSERT = " <script>INSERT INTO user_assert(user_id, currency_symbol, balance, history_max_balance, wallet_address <if test=\"synchronizedTime != null\"> ,synchronized_time</if>) "
    		+ "values(#{userId}, #{currencySymbol}, #{balance}, #{historyMaxBalance}, #{walletAddress} <if test=\"synchronizedTime != null\"> ,#{synchronizedTime}</if>) </script>";
    
    String QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCYSYMBOL = "SELECT balance - ("
    		+ "select CASE WHEN sum(amount) is null then 0 ELSE sum(amount) END AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) as AvailableFunds "
    		+ "FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}";
    
    /**
     * 获取用户用户余额
     * @param userId
     * @param currencySymbol
     * @return
     */
    @Select(QUERY_USER_ASSETS_BY_CURRENCY_SYMBOL)
    BigDecimal getUserAssetsByCurrencySymbol(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
    
    /**
     * 获取用户可用资金
     * @param userId
     * @param currencySymbol
     * @return
     */
    @Select(QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCYSYMBOL)
    BigDecimal getUserAvailableFundsByCurrencySymbol(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
    
    @Select(QUERY_USERS_ASSETS)
    UserAssert queryUserAssert(@Param("userId") Long userId, @Param("coinType") String coinType);
    
    @Insert(SAVE_USER_ASSERT)
    @Options(useGeneratedKeys = true)
    int saveUserAssert(UserAssert userAssert);


    @Update(UPDATE_USER_ASSET_SYNCHRONIZED_TIME)
    int updateUserAssetSynchronizedTime(@Param("id") Long id, @Param("balance") BigDecimal balance, @Param("synchronizedTime") Long synchronizedTime);
}
