package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.repository.domain.ExchangeRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExchangeRecordRepository {

    String SAVE_EXCHANGE_RECORD = "INSERT INTO exchange_record(user_id, exchange_id, charging_number, rmb_amount, platform_amount, record_time, status)"
            + "VALUES(#{userId}, #{exchangeId}, #{chargingNumber}, #{rmbAmount}, #{platformAmount}, #{recordTime}, #{status})";

    String QUERY_EXCHANGE_RECORD = "SELECT * FROM exchange_record WHERE exchange_id = #{exchangeId} AND user_id = #{userId}";

    @Insert(SAVE_EXCHANGE_RECORD)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveExchangeRecord(ExchangeRecord exchangeRecord);

    @Select(QUERY_EXCHANGE_RECORD)
    List<ExchangeRecord> getExchangeRecord(@Param("exchangeId") Long exchangeId, @Param("userId") Long userId);
}
