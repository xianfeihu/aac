package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MerchantDividendRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantDividendRecordRepository {

    String INSERT_MERCHANT_DIVIDEND_RECORD = " INSERT INTO merchant_dividend_record(merchant_id,dividend_date,dividend_Issue_date,profit_amount,dividend_amount,`status`) VALUES ( #{merchantId},#{dividendDate},#{dividendIssueDate},#{profitAmount},#{dividendAmount},#{status} ) ";

    @Insert(INSERT_MERCHANT_DIVIDEND_RECORD)
    void addMerchantDividendRecord(MerchantDividendRecord dividendRecord);

}
