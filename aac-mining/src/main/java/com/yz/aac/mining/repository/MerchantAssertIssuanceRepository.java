package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface MerchantAssertIssuanceRepository {

    String FIND_TOTAL_FOR_CURRENCY_SYMBOL = " select total from  merchant_assert_issuance where currency_symbol=#{currencySymbol} limit 1 ";

    String QUERY_CURRENCY_STATUS = " SELECT b.status*1 FROM merchant_assert_issuance a JOIN merchant_assert_issuance_audit b ON a.id=b.issuance_id WHERE a.currency_symbol=#{currencySymbol} LIMIT 1 ";

    String QUERY_CURRENCY_STATUS_BY_MERCHANT_ID = " SELECT b.status*1 FROM merchant_assert_issuance a JOIN merchant_assert_issuance_audit b ON a.id=b.issuance_id WHERE a.merchant_id=#{merchantId} LIMIT 1 ";

    String QUERY_CURRENCY_MERCHANT_ID = " SELECT merchant_id FROM merchant_assert_issuance WHERE currency_symbol = #{currencySymbol} LIMIT 1";

    String QUERY_CURRENCY_BY_MERCHANT_ID = " SELECT currency_symbol FROM merchant_assert_issuance WHERE merchant_id = #{merchantId} LIMIT 1 ";

    @Select(FIND_TOTAL_FOR_CURRENCY_SYMBOL)
    BigDecimal findTotalForCurrencySymbol(@Param("currencySymbol") String currencySymbol);

    @Select(QUERY_CURRENCY_STATUS)
    Integer queryCurrencyStatus(@Param("currencySymbol") String currencySymbol);

    @Select(QUERY_CURRENCY_MERCHANT_ID)
    Long queryCurrencyMerchantId(@Param("currencySymbol") String currencySymbol);

    @Select(QUERY_CURRENCY_BY_MERCHANT_ID)
    String queryCurrencyByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_CURRENCY_STATUS_BY_MERCHANT_ID)
    Integer queryCurrencyStatusByMerchantId(@Param("merchantId") Long merchantId);
}
