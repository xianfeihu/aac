package com.yz.aac.exchange.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yz.aac.exchange.repository.domian.MerchantAssertTradeRecord;

@Mapper
public interface MerchantAssertTradeRecordRepository {

    String QUERY_MCH_LAST_PLAT_FORM_PRICE = "SELECT platform_price FROM merchant_assert_trade_record WHERE currency_symbol=#{coinType} ORDER BY trade_time DESC LIMIT 1";

    String SAVE_MERCHANT_ASSERT_RECORD = "INSERT INTO merchant_assert_trade_record(initiator_id, initiator_name, trade_type, trade_time, "
			+ "currency_symbol, platform_price, trade_amount, balance, valid_balance, merchant_balance, merchant_valid_balance, partner_id, partner_name, trade_result, order_id) "
			+ "values(#{initiatorId}, #{initiatorName}, #{tradeType}, #{tradeTime}, #{currencySymbol}, #{platformPrice}, "
			+ "#{tradeAmount}, #{balance}, #{validBalance}, #{merchantBalance}, #{merchantValidBalance}, #{partnerId}, #{partnerName}, #{tradeResult}, #{orderId})";
    
    String UPDATE_ORTHER_BALANCE= "UPDATE merchant_assert_trade_record SET valid_balance = #{validBalance}, merchant_balance = #{merchantBalance}, merchant_valid_balance = #{merchantValidBalance} "
    		+ "WHERE id = #{id}";
    
    /**
     * 获取商户最后一笔交易记录的汇率
     */
    @Select(QUERY_MCH_LAST_PLAT_FORM_PRICE)
    BigDecimal getMchLastPlatformPrice(@Param("coinType") String coinType);
    
	@Insert(SAVE_MERCHANT_ASSERT_RECORD)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveTradeRecord(MerchantAssertTradeRecord tradeRecord);
    
	/**
	 * 修改其他资金信息
	 * @param id 交易ID
	 * @param validBalance 可用平台币
	 * @param merchantBalance 总商户币
	 * @param merchantValidBalance 可用商户币
	 */
    @Update(UPDATE_ORTHER_BALANCE)
    void updateOrtherBalance(@Param("id") Long id, @Param("validBalance") BigDecimal validBalance, @Param("merchantBalance") BigDecimal merchantBalance, @Param("merchantValidBalance") BigDecimal merchantValidBalance);
	
}
