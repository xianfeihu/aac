package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.Constants;
import com.yz.aac.exchange.repository.domian.MerchantDividendRecord;
import com.yz.aac.exchange.repository.domian.MerchantDividendTempData;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper
public interface MerchantDividendRecordRepository {

    String FIND_RECORD_BY_STATUS = " <script> SELECT id,merchant_id,dividend_date,dividend_Issue_date,profit_amount,dividend_amount,`status` FROM merchant_dividend_record WHERE merchant_id=#{merchantId} " +
            "  AND `status` IN <foreach collection='dividendStatus' item='status' open='(' close=')' separator=','> #{status.code} </foreach> " +
            " ORDER BY `status` DESC, dividend_date ASC LIMIT 1 </script> ";

    String FIND_LATELY_DIVIDEND_RECORD_BY_STATUS = " SELECT id,merchant_id,dividend_date,dividend_Issue_date,profit_amount,dividend_amount,`status` FROM merchant_dividend_record WHERE merchant_id=#{merchantId} ORDER BY `dividend_date` DESC LIMIT 1  ";

    String INSERT_MERCHANT_DIVIDEND_RECORD = " INSERT INTO merchant_dividend_record(merchant_id,dividend_date,dividend_Issue_date,profit_amount,dividend_amount,`status`) VALUES ( #{merchantId},#{dividendDate},#{dividendIssueDate},#{profitAmount},#{dividendAmount},#{status} ) ";

    String FIND_ALL_MERCHANT_DIVIDEND_RECORD = "  <script> SELECT id,merchant_id,dividend_date,dividend_Issue_date,profit_amount,dividend_amount,`status` FROM merchant_dividend_record WHERE " +
            " `status` IN <foreach collection='dividendStatus' item='status' open='(' close=')' separator=','> #{status.code} </foreach>" +
            " AND dividend_date &lt;= #{toDayBeginTime} </script> ";

    String UPDATE_RECORD_BY_ID = " <script> UPDATE `merchant_dividend_record` SET " +
            " <trim suffixOverrides=\",\"> " +
            "<if test=\"dividendIssueDate != null\"> " +
            "    `dividend_Issue_date` = #{dividendIssueDate}, " +
            "</if> " +
            "<if test=\"profitAmount != null\"> " +
            "    `profit_amount` = #{profitAmount}, " +
            "</if> " +
            "<if test=\"dividendAmount != null\"> " +
            "    `dividend_amount` = #{dividendAmount}, " +
            "</if> " +
            "<if test=\"status != null\"> " +
            "    `status` = #{status}, " +
            " </if> " +
            " </trim> " +
            " WHERE id=#{id} </script> ";

    String STATISTICAL_USER_DIVIDEND_INFO = " INSERT INTO merchant_dividend_temp_data SELECT #{merchantUserId}, user_id, TRUNCATE(balance*#{unit},2) FROM user_assert WHERE currency_symbol = #{currencySymbol} AND user_id != #{merchantUserId}; ";

    String FIND_ALL_DIVIDEND_SUM = " SELECT IFNULL(SUM(amount),0) FROM merchant_dividend_temp_data WHERE merchant_user_id = #{merchantUserId} ";

    String FIND_ALL_DIVIDEND_MESSAGE = " SELECT merchant_user_id,user_id,amount FROM merchant_dividend_temp_data WHERE merchant_user_id=#{merchantUserId} ";

    String DIVIDEND_TO_USER_ASSET = " UPDATE user_assert ua, " +
            " ( SELECT user_id, amount FROM merchant_dividend_temp_data WHERE merchant_user_id = #{merchantUserId} ) mdtd " +
            " SET ua.balance = ua.balance + mdtd.amount ,ua.history_max_balance =  " +
            " CASE  " +
            " WHEN ua.history_max_balance < ua.balance + mdtd.amount THEN " +
            " ua.balance + mdtd.amount " +
            " ELSE " +
            " ua.history_max_balance " +
            " END " +
            " WHERE ua.user_id = mdtd.user_id AND ua.currency_symbol = #{currencySymbol} ";

    String TRUNCATE_MERCHANT_DIVIDEND_TEMP_DATA = " TRUNCATE merchant_dividend_temp_data ";

    @Select(FIND_RECORD_BY_STATUS)
    MerchantDividendRecord findRecordByStatus(@Param("merchantId") Long merchantId,@Param("dividendStatus") Constants.MerchantDividendStatus[] dividendStatus);

    @Select(FIND_LATELY_DIVIDEND_RECORD_BY_STATUS)
    MerchantDividendRecord findLatelyDividendRecordByStatus(@Param("merchantId") Long merchantId);

    @Insert(INSERT_MERCHANT_DIVIDEND_RECORD)
    int addMerchantDividendRecord(MerchantDividendRecord dividendRecord);

    @Update(UPDATE_RECORD_BY_ID)
    int updateRecordById(MerchantDividendRecord merchantDividendRecord);

    @Select(FIND_ALL_MERCHANT_DIVIDEND_RECORD)
    List<MerchantDividendRecord> getToDayAllMerchantDividendRecord(@Param("toDayBeginTime") Long toDayBeginTime, @Param("dividendStatus") Constants.MerchantDividendStatus[] dividendStatus);

    @Insert(STATISTICAL_USER_DIVIDEND_INFO)
    int statisticalUserDividendInfo(@Param("merchantUserId") Long merchantUserId, @Param("unit") BigDecimal unit, @Param("currencySymbol") String currencySymbol);

    @Select(FIND_ALL_DIVIDEND_SUM)
    BigDecimal getAllDividendSum(@Param("merchantUserId") Long merchantUserId);

    @Update(DIVIDEND_TO_USER_ASSET)
    int dividendToUserAsset(@Param("merchantUserId") Long merchantUserId, @Param("currencySymbol") String currencySymbol);

    @Select(FIND_ALL_DIVIDEND_MESSAGE)
    List<MerchantDividendTempData> getAllDividendMessage(@Param("merchantUserId") Long merchantUserId);

    @Delete(TRUNCATE_MERCHANT_DIVIDEND_TEMP_DATA)
    void truncateTemp();
}
