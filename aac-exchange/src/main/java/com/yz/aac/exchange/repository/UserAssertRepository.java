package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.repository.domian.UserAssert;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface UserAssertRepository {

    String QUERY_USER_ASSETS_BY_CURRENCY_SYMBOL = " SELECT balance FROM user_assert WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} ";
    
    String UPDATE_BALANCE = " UPDATE user_assert SET balance = balance + #{balance},history_max_balance = history_max_balance + #{historyMaxBalance} WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} ";

    String QUERY_USERS_ASSETS = " select id,user_id,currency_symbol,balance,history_max_balance,wallet_address,synchronized_time from user_assert where user_id=#{userId} and currency_symbol=#{currencySymbol} limit 1 ";
    
    String SAVE_USER_ASSERT = " <script>INSERT INTO user_assert(user_id, currency_symbol, balance, history_max_balance, wallet_address <if test=\"synchronizedTime != null\"> ,synchronized_time</if>) "
    		+ "values(#{userId}, #{currencySymbol}, #{balance}, #{historyMaxBalance}, #{walletAddress} <if test=\"synchronizedTime != null\"> ,#{synchronizedTime}</if>) </script>";
    
    String QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCYSYMBOL = "SELECT CASE WHEN count(*) = 0 then 0 else balance end - ("
    		+ "select CASE WHEN sum(amount) is null then 0 ELSE sum(amount) END AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) as AvailableFunds "
    		+ "FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}";

    String SUBTRACT_USER_EFFECTIVE_ASSETS = " UPDATE user_assert ua,(SELECT balance - (select IFNULL(sum(amount),0) AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) AS balance FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}) uaf SET ua.balance = ua.balance-#{amount} WHERE ua.user_id=#{userId} AND ua.currency_symbol=#{currencySymbol} AND uaf.balance>=#{amount} ";

    String FIND_ALL_USERS_MERCHANT_ASSET = " SELECT IFNULL(SUM(balance),0) FROM user_assert WHERE user_id!=#{merchantUserId} AND currency_symbol=#{currencySymbol} ";

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
    
    /**
     * 增加用户资金
     * @param userId 用户ID
     * @param currencySymbol 货币符号
     * @param balance 金额（注意要区分正负符号）
     */ 
    @Update(UPDATE_BALANCE)
    int addUserAssertBalance(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("balance") BigDecimal balance, @Param("historyMaxBalance") BigDecimal historyMaxBalance);
	
    @Select(QUERY_USERS_ASSETS)
    UserAssert queryUserAssert(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
    
    @Insert(SAVE_USER_ASSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserAssert(UserAssert userAssert);

    @Update(SUBTRACT_USER_EFFECTIVE_ASSETS)
    int subtractUserEffectiveAssets(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("amount") BigDecimal amount);

    @Select(FIND_ALL_USERS_MERCHANT_ASSET)
    BigDecimal findAllUsersMerchantAsset(@Param("currencySymbol") String currencySymbol, @Param("merchantUserId") Long merchantUserId);
}
