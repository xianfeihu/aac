package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.MerchantTradeRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantTradeRecordRepository {

    String ADD_MERCHANT_TRADE_RECORD = " INSERT INTO merchant_trade_record (merchant_id,user_id,transaction_mode,flow_direction,trade_amount,platform_price,add_subtract,trade_time) VALUES (#{merchantId},#{userId},#{transactionMode},#{flowDirection},#{tradeAmount},#{platformPrice},#{addSubtract},#{tradeTime}) ";

    @Insert(ADD_MERCHANT_TRADE_RECORD)
    void addMerchantTradeRecord(MerchantTradeRecord tradeRecord);
}
