package com.yz.aac.exchange.repository;

import com.yz.aac.exchange.model.response.CurrencyIntroductionResponse;
import com.yz.aac.exchange.model.response.MerchantIssueCurrencyResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Set;

@Mapper
public interface MerchantAssertIssuanceRepository {

    String FIND_TOTAL_FOR_CURRENCY_SYMBOL = " select total from  merchant_assert_issuance where currency_symbol=#{currencySymbol} limit 1 ";
    String QUERY_CURRENCY_INTRODUCTION = " <script> SELECT b.sto_dividend_rate,b.fixed_income_rate,b.income_period,b.restriction_period,a.merchant_name,b.total,IFNULL(c.liquidity,0) AS liquidity,b.white_paper_url,b.introduction,b.issuing_date FROM merchant a JOIN merchant_assert_issuance b ON b.merchant_id=a.id LEFT JOIN (SELECT merchant_id,SUM(`value`) AS liquidity FROM merchant_assert_statistics WHERE currency_symbol=#{currencySymbol} " +
            " AND `key` in <foreach collection='liquidityKey' item='liquidity' open='(' close=')' separator=','> #{liquidity.name} </foreach> " +
            " GROUP BY merchant_id) c ON c.merchant_id=b.merchant_id WHERE b.currency_symbol=#{currencySymbol} " +
            "</script>";
    String QUERY_CURRENCY_STATUS = " SELECT b.status*1 FROM merchant_assert_issuance a JOIN merchant_assert_issuance_audit b ON a.id=b.issuance_id WHERE a.currency_symbol=#{currencySymbol} LIMIT 1 ";

    String QUERY_CURRENCY_STATUS_BY_MERCHANT_ID = " SELECT b.status*1 FROM merchant_assert_issuance a JOIN merchant_assert_issuance_audit b ON a.id=b.issuance_id WHERE a.merchant_id=#{merchantId} LIMIT 1 ";

    String QUERY_CURRENCY_MERCHANT_ID = " SELECT merchant_id FROM merchant_assert_issuance WHERE currency_symbol = #{currencySymbol} LIMIT 1";

    String QUERY_CURRENCY_BY_MERCHANT_ID = " SELECT currency_symbol FROM merchant_assert_issuance WHERE merchant_id = #{merchantId} LIMIT 1 ";

    String QUERY_MERCHANT_ISSUE_CURRENCY_DETAILS = " SELECT mai.sto_dividend_rate,mai.fixed_income_rate,mai.income_period,mai.restriction_period,m.merchant_name,mai.total,mai.white_paper_url,mai.introduction,maia.audit_time AS issuing_date FROM merchant m JOIN merchant_assert_issuance mai ON m.id=mai.merchant_id JOIN merchant_assert_issuance_audit maia ON mai.id=maia.issuance_id WHERE m.id=#{merchantId} ";


    @Select(FIND_TOTAL_FOR_CURRENCY_SYMBOL)
    BigDecimal findTotalForCurrencySymbol(@Param("currencySymbol") String currencySymbol);

    @Select(QUERY_CURRENCY_STATUS)
    Integer queryCurrencyStatus(@Param("currencySymbol") String currencySymbol);

    @Select(QUERY_CURRENCY_INTRODUCTION)
    CurrencyIntroductionResponse currencyIntroduction(@Param("currencySymbol") String currencySymbol ,@Param("liquidityKey") Set liquidityKey);

    @Select(QUERY_CURRENCY_MERCHANT_ID)
    Long queryCurrencyMerchantId(@Param("currencySymbol") String currencySymbol);

    @Select(QUERY_CURRENCY_BY_MERCHANT_ID)
    String queryCurrencyByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_CURRENCY_STATUS_BY_MERCHANT_ID)
    Integer queryCurrencyStatusByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_MERCHANT_ISSUE_CURRENCY_DETAILS)
    MerchantIssueCurrencyResponse getMerchantIssueCurrencyDetails(@Param("merchantId") Long merchantId);
}
