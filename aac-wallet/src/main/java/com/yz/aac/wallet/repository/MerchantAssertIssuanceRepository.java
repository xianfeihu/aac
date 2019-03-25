package com.yz.aac.wallet.repository;

import com.yz.aac.wallet.model.response.MerchantIssueCurrencyResponse;
import com.yz.aac.wallet.repository.domain.MerchantAssertIssuance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MerchantAssertIssuanceRepository {

    String All_BY_MERCHANTID = "  SELECT currency_symbol FROM merchant_assert_issuance a JOIN merchant_assert_issuance_audit a1 ON a1.issuance_id=a.id WHERE a1.status=#{status} ";

    String BY_MERCHANTID = " SELECT id FROM merchant_assert_issuance WHERE merchant_id = #{merchantId} LIMIT 1 ";

    String DELETE_BY_MERCHANT_ID = " DELETE FROM merchant_assert_issuance WHERE merchant_id = #{merchantId}";

    String QUERY_ALL_CURRENCY_SYMBOL = " SELECT currency_symbol,service_charge_id FROM merchant_assert_issuance WHERE merchant_id = #{merchantId}";

    String QUERY_MERCHANT_ID_CURRENCY_SYMBOL = " SELECT merchant_id FROM merchant_assert_issuance where currency_symbol=#{currencySymbol} ";

    String INSERT_MERCHANT_ASSET_ISSUANCE = " INSERT INTO merchant_assert_issuance (merchant_id,currency_symbol,total,sell_rate,mining_rate,fixed_income_rate,sto_dividend_rate,other_mode,income_period,restriction_period,introduction,white_paper_url,issuing_date) VALUES (#{merchantId},#{currencySymbol},#{total},100-#{miningRate},#{miningRate},#{fixedIncomeRate},#{stoDividendRate},#{otherMode},#{incomePeriod},#{restrictionPeriod},#{introduction},#{whitePaperUrl},#{issuingDate}) ";

    String QUERY_CURRENCY_BY_MERCHANT_ID = " SELECT currency_symbol FROM merchant_assert_issuance WHERE merchant_id = #{merchantId} LIMIT 1 ";

    String QUERY_MERCHANT_ISSUE_CURRENCY_DETAILS = " SELECT mai.sto_dividend_rate,mai.fixed_income_rate,mai.income_period,mai.restriction_period,m.merchant_name,mai.total,mai.white_paper_url,mai.introduction,maia.audit_time FROM merchant m JOIN merchant_assert_issuance mai ON m.id=mai.merchant_id JOIN merchant_assert_issuance_audit maia ON mai.id=maia.issuance_id WHERE m.id=#{merchantId} ";


    /**
     * 根据商家查找发币审批ID
     * @param merchantId 商家ID
     * @return
     */
    @Select(BY_MERCHANTID)
    Long getByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_ALL_CURRENCY_SYMBOL)
    MerchantAssertIssuance getAllByMerchantId(@Param("merchantId") Long merchantId);

    @Select(All_BY_MERCHANTID)
    List<String> getAllCurrencySymbol(@Param("status") Integer status);

    @Select(QUERY_MERCHANT_ID_CURRENCY_SYMBOL)
    Long getMerchantAssertIssuanceByCurrencySymbol(@Param("currencySymbol") String currencySymbol);

    @Insert(INSERT_MERCHANT_ASSET_ISSUANCE)
    @Options(useGeneratedKeys = true)
    int addMerchantAssertIssuance(MerchantAssertIssuance issuanceMessageRequest);

    @Delete(DELETE_BY_MERCHANT_ID)
    void deleteByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_CURRENCY_BY_MERCHANT_ID)
    String queryCurrencyByMerchantId(@Param("merchantId") Long merchantId);

    @Select(QUERY_MERCHANT_ISSUE_CURRENCY_DETAILS)
    MerchantIssueCurrencyResponse getMerchantIssueCurrencyDetails(@Param("merchantId") Long merchantId);
}
