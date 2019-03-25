package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.model.request.TransferToWalletAddrRequest;
import com.yz.aac.wallet.model.response.CoinDetailsMsgResponse;

import org.apache.ibatis.annotations.*;

import com.yz.aac.wallet.repository.domain.UserAssets;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserAssetsRepository {

    String QUERY_USERS_ASSETS = " select id,user_id,currency_symbol,balance,history_max_balance,wallet_address from user_assert where user_id=#{userId} and currency_symbol=#{coinType} limit 1 ";

    String QUERY_USERS_OTHER_ASSETS = " SELECT currency_symbol,amount,IFNULL(platform_price,0) platform_price FROM (SELECT currency_symbol,balance amount FROM user_assert WHERE user_id=#{userId} and currency_symbol!=#{ignoreTradeType}) tb1 LEFT JOIN (SELECT currency_symbol,platform_price FROM merchant_assert_trade_record t1 JOIN (SELECT currency_symbol,MAX(trade_time) trade_time FROM merchant_assert_trade_record WHERE 1=1 GROUP BY currency_symbol) t2 USING (currency_symbol,trade_time) WHERE t1.currency_symbol!=#{ignoreTradeType}) tb2 USING (currency_symbol) ";

    String QUERY_USERS_OTHER_ASSETS_SUM = " SELECT SUM(amount*platform_price) sumAssets FROM (SELECT currency_symbol,balance amount FROM user_assert WHERE user_id=#{userId} and currency_symbol!=#{ignoreTradeType}) tb1 LEFT JOIN (SELECT currency_symbol,platform_price FROM merchant_assert_trade_record t1 JOIN (SELECT currency_symbol,MAX(trade_time) trade_time FROM merchant_assert_trade_record WHERE 1=1 GROUP BY currency_symbol) t2 USING (currency_symbol,trade_time)) tb2 USING (currency_symbol) ";

    String QUERY_USER_ASSETS_BY_CURRENCY_SYMBOL = " SELECT balance FROM user_assert WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} ";

    String CHECK_FOR_WALLET_ADDRESS = " select count(0) from user_assert where wallet_address=#{walletAddress}" ;

    String SAVE_USER_ASSERT = "INSERT INTO user_assert(user_id, currency_symbol, balance, history_max_balance, wallet_address) "
    		+ "values(#{userId}, #{currencySymbol}, #{balance}, #{historyMaxBalance}, #{walletAddress})";

    String TRANSFER_FOR_LESS_ASSERT = " UPDATE user_assert set balance=balance-#{amount} WHERE user_id=#{userId} and wallet_address=#{sendAddr} and currency_symbol=#{coinType} and balance>#{amount} " ;

    String TRANSFER_FOR_ADD_ASSERT = " UPDATE user_assert set balance=balance+#{amount},history_max_balance=history_max_balance+#{amount} WHERE wallet_address=#{receiveAddr} and currency_symbol=#{coinType} " ;

    String UPDATE_USER_ASSETS_ADDS = "UPDATE user_assert SET wallet_address = #{walletAddress} where user_id = #{userId} and currency_symbol = #{currencySymbol}";

    String QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCY_SYMBOL = "SELECT balance - (select CASE WHEN sum(amount) is null then 0 ELSE sum(amount) END AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) as AvailableFunds FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}";

    String UPDATE_BALANCE = " UPDATE user_assert SET balance = balance + #{balance},history_max_balance = history_max_balance + #{historyMaxBalance} WHERE user_id=#{userId} AND currency_symbol=#{currencySymbol} ";

    String SUBTRACT_USER_EFFECTIVE_ASSETS = " UPDATE user_assert ua,(SELECT balance - (select IFNULL(sum(amount),0) AS amount from user_assert_freeze where user_id = #{userId} and currency_symbol=#{currencySymbol}) AS balance FROM user_assert WHERE user_id = #{userId} AND currency_symbol=#{currencySymbol}) uaf SET ua.balance = ua.balance-#{amount} WHERE ua.user_id=#{userId} AND ua.currency_symbol=#{currencySymbol} AND uaf.balance>=#{amount} ";

    @Select(QUERY_USERS_ASSETS)
    UserAssets queryUserAssert(@Param("userId") Long userId, @Param("coinType") String coinType);
    
    @Insert(SAVE_USER_ASSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserAssets(UserAssets userAssert);

    @Select(QUERY_USERS_OTHER_ASSETS)
    List<CoinDetailsMsgResponse> getUserOtherAssets(@Param("userId") Long userId , @Param("ignoreTradeType") String ignoreTradeType);

    @Select(QUERY_USERS_OTHER_ASSETS_SUM)
    BigDecimal getUserOtherAssetsSum(@Param("userId") Long userId, @Param("ignoreTradeType") String ignoreTradeType);

    @Select(QUERY_USER_ASSETS_BY_CURRENCY_SYMBOL)
    BigDecimal getUserAssetsByCurrencySymbol(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);

    @Select(CHECK_FOR_WALLET_ADDRESS)
    int checkForWalletAddress(String walletAddress);
    
    @Update(UPDATE_USER_ASSETS_ADDS)
    void updateUserAssetsAdds(@Param("walletAddress") String walletAddress, @Param("userId") Long userId,
    		@Param("currencySymbol") String currencySymbol);

    @Update(TRANSFER_FOR_LESS_ASSERT)
    int lessAssets(TransferToWalletAddrRequest transferMsg);

    @Update(TRANSFER_FOR_ADD_ASSERT)
    int addAssets(TransferToWalletAddrRequest transferMsg);

    /**
     * 获取用户可用资金
     * @param userId
     * @param currencySymbol
     * @return
     */
    @Select(QUERY_USER_AVAILABLE_FUNDS_BY_CURRENCY_SYMBOL)
    BigDecimal getUserAvailableFundsByCurrencySymbol(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);

    /**
     * 添加用户资金
     * @param userId 用户ID
     * @param currencySymbol 货币符号
     * @param balance 资金 （注意要区分正负号）
     * @param historyMaxBalance 资金
     */
    @Update(UPDATE_BALANCE)
    int addUserAssertBalance(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("balance") BigDecimal balance, @Param("historyMaxBalance") BigDecimal historyMaxBalance);

    @Update(SUBTRACT_USER_EFFECTIVE_ASSETS)
    int subtractUserEffectiveAssets(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("amount") BigDecimal amount);

}
