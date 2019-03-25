package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.model.response.MerchantFreezeStockDetailsResponse;
import com.yz.aac.exchange.model.response.MerchantFreezeStockResponse;
import com.yz.aac.exchange.repository.domian.UserAssertFreeze;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserAssertFreezeRepository {

    String SAVE_USER_ASSERT_FREEZE = "INSERT INTO user_assert_freeze(user_id, currency_symbol, amount, reason, action_time) "
			+ "values(#{userId}, #{currencySymbol}, #{amount}, #{reason}, #{actionTime})";
    
    String UPDATE_FREEZE_AMOUNT = "UPDATE user_assert_freeze SET amount = amount + #{amount} WHERE user_id = #{userId} AND currency_symbol = #{currencySymbol} AND reason = #{reason}";
    
    String QUERY_ASSET_FREEZE_BY_USERID = "SELECT * FROM user_assert_freeze WHERE user_id = #{userId} AND currency_symbol = #{currencySymbol} AND reason = #{reason}";
    
    String DELETE_ASSET_FREEZE = "delete from user_assert_freeze where amount = 0";

    String ASSET_FREEZE_FOR_MERCHANT = " SELECT id,available_trade_amount,original_limit,aab_price FROM user_order WHERE user_id=#{userId} AND type=#{type} AND status=#{status} ";

    String ASSET_FREEZE_DETAILS_FOR_MERCHANT = " SELECT uo.type,matr.trade_time,u.mobile_number AS account_number,matr.trade_amount FROM user_order uo JOIN merchant_assert_trade_record matr ON matr.order_id=uo.id LEFT JOIN `user` u ON matr.initiator_id=u.id WHERE uo.id=#{orderId} AND uo.user_id=#{userId}; ";

    String ASSET_FREEZE_SUM_FOR_MERCHANT = " SELECT SUM(available_trade_amount) FROM user_order WHERE user_id=#{userId} AND type=#{type} AND status=#{status} ";

	@Insert(SAVE_USER_ASSERT_FREEZE)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserAssertFreeze(UserAssertFreeze userAssertFreeze);
	
	@Update(UPDATE_FREEZE_AMOUNT)
	void updateFreezeAmount(UserAssertFreeze userAssertFreeze);
	
	@Select(QUERY_ASSET_FREEZE_BY_USERID)
	UserAssertFreeze getAssetFreezeByUserId(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("reason") Integer reason);
	
	@Delete(DELETE_ASSET_FREEZE)
	void deleteAssetFreeze();

	@Select(ASSET_FREEZE_FOR_MERCHANT)
	List<MerchantFreezeStockResponse> getOrderAssetFreezeForMerchant(@Param("userId") Long userId,@Param("type") Integer type,@Param("status") Integer status);

	@Select(ASSET_FREEZE_DETAILS_FOR_MERCHANT)
	List<MerchantFreezeStockDetailsResponse> getOrderAssetFreezeDetailsForMerchant(@Param("userId") Long userId, @Param("orderId") Long orderId);

	@Select(ASSET_FREEZE_SUM_FOR_MERCHANT)
	BigDecimal getOrderAssetFreezeSumForMerchant(@Param("userId") Long userId,@Param("type") Integer type,@Param("status") Integer status);

}
