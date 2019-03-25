package com.yz.aac.mining.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.mining.repository.domian.UserAssert;

@Mapper
public interface UserAssertRepository {

    String QUERY_USERS_ASSETS = " select id,user_id,currency_symbol,balance,history_max_balance,wallet_address,synchronized_time from user_assert where user_id=#{userId} and currency_symbol=#{currencySymbol} limit 1 ";
    
    String UPDATE_BALANCE = " UPDATE user_assert SET balance = balance + #{balance},history_max_balance = history_max_balance + #{historyMaxBalance} WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} ";

    String SUBTRACT_USER_EFFECTIVE_ASSETS = " UPDATE user_assert ua,(SELECT balance - (select IFNULL(sum(amount),0) AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) AS balance FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}) uaf SET ua.balance = ua.balance-#{amount} WHERE ua.user_id=#{userId} AND ua.currency_symbol=#{currencySymbol} AND uaf.balance>=#{amount} ";

    String QUERY_USER_HISTORY_MAX_BALANCE_BY_USER_ID = " SELECT IFNULL(history_max_balance,0) FROM user_assert WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} LIMIT 1 ";

    String QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCYSYMBOL = "SELECT balance - ("
    		+ "select CASE WHEN sum(amount) is null then 0 ELSE sum(amount) END AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) as AvailableFunds "
    		+ "FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}";
    
    @Select(QUERY_USERS_ASSETS)
    UserAssert queryUserAssert(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
    
    /**
     * 添加用户资金
     * @param userId 用户ID
     * @param currencySymbol 货币符号
     * @param balance 资金 （注意要区分正负号）
     * @param historyMaxBalance 资金
     */
    @Update(UPDATE_BALANCE)
    int addUserAssertBalance(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("balance") BigDecimal balance, @Param("historyMaxBalance") BigDecimal historyMaxBalance);

    /**
     * 扣除用户资金
     * @param userId
     * @param currencySymbol
     * @param amount
     * @return
     */
    @Update(SUBTRACT_USER_EFFECTIVE_ASSETS)
    int subtractUserEffectiveAssets(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("amount") BigDecimal amount);

    /**
     * 获取用户可用资金
     * @param userId
     * @param currencySymbol
     * @return
     */
    @Select(QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCYSYMBOL)
    BigDecimal getUserAvailableFundsByCurrencySymbol(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);


    /**
     * 获取用户历史最大余额
     * @param userId
     * @return
     */
    @Select(QUERY_USER_HISTORY_MAX_BALANCE_BY_USER_ID)
    BigDecimal getUserHistoryMaxBalanceByUserId (@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
}
