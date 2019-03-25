package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.model.response.UserTransferRecordResponse;
import com.yz.aac.wallet.repository.domain.PlatformAssertTradeRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlatformAssertTradeRecordRepository {

    String SAVE_ASSERT_TRADE_RECORD = "INSERT INTO platform_assert_trade_record(initiator_id, initiator_name, trade_type, trade_time, rmb_price, "
    		+ "available_trade_amount,trade_amount,min_amount_limit,max_amount_limit, wallet_address, balance, valid_balance, partner_id, partner_name, pay_number, status, selling_order_id) "
    		+ "values(#{initiatorId}, #{initiatorName}, #{tradeType}, #{tradeTime}, #{rmbPrice},#{availableTradeAmount}, #{tradeAmount}, "
    		+ "#{minAmountLimit},#{maxAmountLimit}, #{walletAddress}, #{balance}, #{validBalance}, #{partnerId}, #{partnerName}, #{payNumber}, #{status}, #{sellingOrderId})";
    
    String QUERY_ASSERT_TRADE_RECORD= "select * from platform_assert_trade_record where trade_type = #{tradeType} and initiator_id = #{initiatorId}";

    String ADD_USER_TRANSFER_RECORD_INFO= " INSERT INTO platform_assert_trade_record (initiator_id,initiator_name,trade_type,trade_time,rmb_price,trade_amount,wallet_address,balance,valid_balance,partner_id,partner_name,`status`)  " +
            " VALUES (#{initiatorId}, #{initiatorName}, #{tradeType}, #{tradeTime}, #{rmbPrice}, #{tradeAmount}, #{walletAddress}, #{balance}, #{validBalance}, #{partnerId}, #{partnerName}, #{status}) ";

    String UPDATE_ASSERT_TRADE_RECORD= "update platform_assert_trade_record set initiator_name = #{initiatorName}, "
    		+ "trade_time = #{tradeTime}, rmb_price = #{rmbPrice}, "
    		+ "available_trade_amount = #{availableTradeAmount}, trade_amount = #{tradeAmount}, min_amount_limit = #{minAmountLimit}, "
    		+ "max_amount_limit = #{maxAmountLimit}, wallet_address = #{walletAddress}, balance = #{balance}, "
    		+ "valid_balance = #{validBalance}, partner_id = #{partnerId}, partner_name = #{partnerName},"
    		+ " pay_number = #{payNumber}, status = #{status}, selling_order_id = #{sellingOrderId} "
    		+ " where trade_type = #{tradeType} and initiator_id = #{initiatorId}";

    String QUERY_TRANSACTION_RECORD = " SELECT #{flag}, #{userId}, initiator_id, initiator_name, partner_id, partner_name, trade_type * 1, trade_time, trade_amount,\n" +
            "CASE\n" +
            "\tWHEN trade_type = #{dividend} THEN\n" +
            "\tmai.currency_symbol ELSE #{currencySymbol} \n" +
            "\tEND AS currency_symbol\n" +
            "FROM\n" +
            "\tplatform_assert_trade_record JOIN `user` u ON initiator_id=u.id LEFT JOIN merchant m ON m.mobile_number=u.mobile_number LEFT JOIN merchant_assert_issuance mai ON mai.merchant_id=m.id\n" +
            "WHERE\n" +
            "\t( initiator_id = #{userId} OR partner_id = #{userId} ) \n" +
            "AND trade_type IN ( #{transfer}, #{dividend} ) ";

    @Insert(SAVE_ASSERT_TRADE_RECORD)
    @Options(useGeneratedKeys = true)
    int saveAssertTeadeTrcord(PlatformAssertTradeRecord platformAssertTradeRecord);
    
    @Select(QUERY_ASSERT_TRADE_RECORD)
    PlatformAssertTradeRecord getAssertTradeRecord(@Param("tradeType") Integer tradeType, @Param("initiatorId") Long initiatorId);

    /**
     * 用于缴纳押金只允许生成一条类型数据
     * @param platformAssertTradeRecord
     * @return
     */
    @Update(UPDATE_ASSERT_TRADE_RECORD)
    int updatePlatformAssertTrade(PlatformAssertTradeRecord platformAssertTradeRecord);

    @Insert(ADD_USER_TRANSFER_RECORD_INFO)
    int addUserTransferRecordInfo(PlatformAssertTradeRecord platformAssertTradeRecord);

    @Select(QUERY_TRANSACTION_RECORD)
    List<UserTransferRecordResponse> getTransactionRecordHistory(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol, @Param("transfer") Integer transfer, @Param("dividend") Integer dividend, @Param("flag") Boolean flag);
}
