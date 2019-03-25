package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuance;
import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuanceAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MerchantAssertIssuanceRepository {

    String ALL_FIELDS = "id, merchant_id, currency_symbol, total, sell_rate, mining_rate, fixed_income_rate, sto_dividend_rate, other_mode, income_period, restriction_period, introduction, white_paper_url, issuing_date, service_charge_id";
    String QUERY_MERCHANT_ASSERT_ISSUANCE = "<script>SELECT " + ALL_FIELDS + " FROM merchant_assert_issuance"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "<if test=\"serviceChargeId != null\"> AND service_charge_id = #{serviceChargeId}</if>"
            + "</where>"
            + "</script>";
    String QUERY_ALL_SYMBOLS = "SELECT mi.id, m.name AS merchant_name, mi.currency_symbol FROM merchant_assert_issuance mi"
            + " INNER JOIN merchant_assert_issuance_audit a ON mi.id = a.issuance_id AND a.status IN (2, 3, 4, 5)"
            + " INNER JOIN merchant m ON mi.merchant_id = m.id"
            + " ORDER BY mi.currency_symbol";

    String QUERY_ISSUANCE_REQUESTS = "<script>SELECT"
            + " a.id AS requestId, m.id AS merchantId, a.status, m.merchant_name, i.currency_symbol,"
            + " IFNULL(ua.balance, 0) AS balance,"
            + " (SELECT IFNULL(SUM(amount), 0) FROM user_assert_freeze uaf WHERE uaf.user_id = u.id AND uaf.currency_symbol = #{platformCurrencySymbol}) AS freezingAmount,"
            + " a.request_time, i.total, i.sell_rate, i.mining_rate, i.fixed_income_rate, i.sto_dividend_rate, i.income_period, i.restriction_period, i.white_paper_url, a.audit_comment"
            + " FROM merchant m"
            + " INNER JOIN merchant_assert_issuance i ON i.merchant_id = m.id"
            + " INNER JOIN merchant_assert_issuance_audit a ON i.id = a.issuance_id"
            + " LEFT JOIN user u ON u.mobile_number = m.mobile_number"
            + " LEFT JOIN user_assert ua ON u.id = ua.user_id AND ua.currency_symbol = #{platformCurrencySymbol}"
            + "<where>"
            + "<if test=\"merchantId != null\"> AND m.id = #{merchantId}</if>"
            + "<if test=\"currencySymbol != null and currencySymbol != ''\"> AND i.currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"status != null\"> AND a.status = #{status}</if>"
            + "<if test=\"mobileNumber != null\"> AND m.mobile_number = #{mobileNumber}</if>"
            + "<if test=\"merchantName != null and merchantName != ''\"><bind name=\"fixedName\" value=\"'%' + merchantName + '%'\" /> AND m.merchant_name LIKE #{fixedName}</if>"
            + "</where>"
            + " ORDER BY a.status, a.request_time"
            + "</script>";

    String UPDATE_ISSUANCE = "UPDATE merchant_assert_issuance SET issuing_date = #{issuingDate}, service_charge_id = #{serviceChargeId} WHERE id = #{id}";


    @Select(QUERY_MERCHANT_ASSERT_ISSUANCE)
    List<MerchantAssertIssuance> query(MerchantAssertIssuance issuance);

    @Select(QUERY_ALL_SYMBOLS)
    List<MerchantAssertIssuance> queryAllSymbols();

    @Select(QUERY_ISSUANCE_REQUESTS)
    List<MerchantAssertIssuance> queryIssuanceRequests(MerchantAssertIssuance issuance);

    @Update(UPDATE_ISSUANCE)
    void update(MerchantAssertIssuance issuance);
}
