package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import com.yz.aac.exchange.repository.domian.PlatformAssertTradeRecord;

import java.util.List;

@Mapper
public interface PlatformAssertTradeRecordRepository {

    String SAVE_ASSERT_TRADE_RECORD = "INSERT INTO platform_assert_trade_record(initiator_id, initiator_name, trade_type, trade_time, rmb_price, "
    		+ "available_trade_amount, trade_amount, min_amount_limit, max_amount_limit, wallet_address, balance, valid_balance, partner_id, partner_name, pay_number, status, selling_order_id) "
    		+ "values(#{initiatorId}, #{initiatorName}, #{tradeType}, #{tradeTime}, #{rmbPrice}, #{availableTradeAmount}, #{tradeAmount}, "
    		+ "#{minAmountLimit}, #{maxAmountLimit}, #{walletAddress}, #{balance}, #{validBalance}, #{partnerId}, #{partnerName}, #{payNumber}, #{status}, #{sellingOrderId})";

    String ADD_USER_DIVIDEND_RECORD_INFO = " INSERT INTO platform_assert_trade_record (initiator_id,initiator_name,trade_type,trade_time,rmb_price,trade_amount,wallet_address,balance,valid_balance,partner_id,partner_name,`status`) " +
            " SELECT #{initiatorId}, #{initiatorName}, #{tradeType}, #{tradeTime}, #{rmbPrice}, #{tradeAmount}, #{walletAddress}, #{balance}, #{validBalance}, #{partnerId}, u.name, #{status} FROM `user` u WHERE u.id = #{partnerId}; ";

    @Insert(SAVE_ASSERT_TRADE_RECORD)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveAssertTeadeTrcord(PlatformAssertTradeRecord platformAssertTradeRecord);

    @Insert(ADD_USER_DIVIDEND_RECORD_INFO)
    int addUserDividendRecordInfo(PlatformAssertTradeRecord list);
}