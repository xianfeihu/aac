package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.CurrencyKeyValuePair;
import com.yz.aac.opadmin.repository.domain.Merchant;
import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MerchantRepository {

    String ALL_FIELDS = "id, name, merchant_name, gender, id_number, mobile_number, create_time";
    String QUERY_MERCHANTS = "<script>SELECT " + ALL_FIELDS + " FROM merchant"
            + "<where>"
            + "<if test=\"id != null\"> AND id = #{id}</if>"
            + "</where>"
            + "</script>";

    String QUERY_MERCHANTS_FOR_LIST = "<script>SELECT"
            + " freeze.amount AS issuanceFreezingAmount,"
            + " up.increase_strategy_id AS increaseStrategyId,"
            + " u.id AS userId,"
            + " m.id AS merchantId,"
            + " up.status AS status,"
            + " m.merchant_name AS merchantName,"
            + " IFNULL(ua.balance, 0) AS balance,"
            + " (SELECT IFNULL(SUM(amount), 0) FROM user_assert_freeze uaf WHERE uaf.user_id = u.id AND uaf.currency_symbol = #{platformCurrencySymbol}) AS freezingAmount,"
            + " mai.issuing_date AS issuingDate,"
            + " mai.total AS total,"
            + " mai.sell_rate AS sellRate,"
            + " mai.mining_rate AS miningRate,"
            + " mai.currency_symbol AS currencySymbol,"
            + " (SELECT x.recent_price FROM merchant_assert_latest_data x WHERE x.count_type = 1 AND x.currency_symbol = mai.currency_symbol ORDER BY create_time LIMIT 1) AS rmbPrice,"
            + " IFNULL((SELECT value FROM merchant_assert_statistics x WHERE x.merchant_id = m.id AND x.currency_symbol = mai.currency_symbol AND x.key = 'SELL_REST'), mai.total * (mai.sell_rate / 100.0)) AS sellRest,"
            + " IFNULL((SELECT value FROM merchant_assert_statistics x WHERE x.merchant_id = m.id AND x.currency_symbol = mai.currency_symbol AND x.key = 'SELL_SOLD'), 0) AS sellSold,"
            + " IFNULL((SELECT value FROM merchant_assert_statistics x WHERE x.merchant_id = m.id AND x.currency_symbol = mai.currency_symbol AND x.key = 'MINING_MIND'), 0) AS miningMind,"
            + " IFNULL((SELECT value FROM merchant_assert_statistics x WHERE x.merchant_id = m.id AND x.currency_symbol = mai.currency_symbol AND x.key = 'TRADED'), 0) AS tradeCount"
            + " FROM merchant m"
            + " INNER JOIN merchant_assert_issuance mai ON mai.merchant_id = m.id"
//            + " INNER JOIN merchant_assert_issuance_audit maia ON maia.issuance_id = mai.id AND maia.status = 5"
            + " INNER JOIN merchant_assert_issuance_audit maia ON maia.issuance_id = mai.id"
            + " LEFT JOIN user u ON m.mobile_number = u.mobile_number"
            + " LEFT JOIN user_role ur ON u.id = ur.user_id AND ur.is_merchant = 1"
            + " LEFT JOIN user_property up ON up.user_id = u.id"
            + " LEFT JOIN user_assert ua ON ua.user_id = u.id AND ua.currency_symbol = #{platformCurrencySymbol}"
            + " LEFT JOIN user_assert_freeze freeze ON freeze.user_id = u.id AND freeze.currency_symbol = #{platformCurrencySymbol} AND freeze.reason = 1"
            + "<where>"
            + "<if test=\"merchantId != null\"> AND m.id = #{merchantId}</if>"
            + "<if test=\"currencySymbol != null\"> AND mai.currency_symbol = #{currencySymbol}</if>"
            + "<if test=\"mobileNumber != null\"> AND m.mobile_number = #{mobileNumber}</if>"
            + "<if test=\"beginTime != null\"> AND mai.issuing_date &gt;= #{beginTime}</if>"
            + "<if test=\"endTime != null\"> AND mai.issuing_date &lt;= #{endTime}</if>"
            + "<if test=\"status != null\"> AND up.status = #{status}</if>"
            + "<if test=\"merchantName != null and merchantName != ''\"><bind name=\"fixedName\" value=\"'%' + merchantName + '%'\" /> AND m.merchant_name LIKE #{fixedName}</if>"
            + "</where>"
            + "</script>";

    String STATISTIC_MERCHANTS_FOR_LIST = "<script>"
            + "SELECT uis.key AS `key`, IFNULL(SUM(uis.value), 0) AS value"
            + " FROM user_income_statistics uis"
            + " WHERE uis.currency_symbol = #{platformCurrencySymbol}"
            + " AND uis.user_id IN ("
            + "   SELECT DISTINCT(u.id)"
            + "   FROM merchant m"
            + "   INNER JOIN merchant_assert_issuance mai ON mai.merchant_id = m.id"
//            + "   INNER JOIN merchant_assert_issuance_audit maia ON maia.issuance_id = mai.id AND maia.status = 5"
            + "   INNER JOIN merchant_assert_issuance_audit maia ON maia.issuance_id = mai.id"
            + "   LEFT JOIN user u ON m.mobile_number = u.mobile_number"
            + "   LEFT JOIN user_role ur ON u.id = ur.user_id AND ur.is_merchant = 1"
            + "   LEFT JOIN user_property up ON up.user_id = u.id"
            + "   <where>"
            + "     <if test=\"merchantId != null\"> AND m.id = #{merchantId}</if>"
            + "     <if test=\"currencySymbol != null\"> AND mai.currency_symbol = #{currencySymbol}</if>"
            + "     <if test=\"mobileNumber != null\"> AND m.mobile_number = #{mobileNumber}</if>"
            + "     <if test=\"beginTime != null\"> AND mai.issuing_date &gt;= #{beginTime}</if>"
            + "     <if test=\"endTime != null\"> AND mai.issuing_date &lt;= #{endTime}</if>"
            + "     <if test=\"status != null\"> AND up.status = #{status}</if>"
            + "     <if test=\"merchantName != null and merchantName != ''\"><bind name=\"fixedName\" value=\"'%' + merchantName + '%'\" /> AND m.merchant_name LIKE #{fixedName}</if>"
            + "   </where>"
            + " )"
            + " GROUP BY uis.key"
            + "</script>";

    String QUERY_MERCHANT_DETAIL = "SELECT"
            + " u.id AS userId,"
            + " m.id AS merchantId,"
            + " m.merchant_name AS merchantName,"
            + " m.name AS name,"
            + " m.mobile_number AS mobileNumber,"
            + " m.id_number AS idNumber,"
            + " up.status AS status,"
            + " up.status_description AS statusDescription,"
            + " mai.currency_symbol AS currencySymbol,"
            + " mai.total AS total,"
            + " mai.mining_rate AS miningRate,"
            + " mai.fixed_Income_Rate AS fixedIncomeRate,"
            + " mai.sto_dividend_rate AS stoDividendRate,"
            + " mai.other_mode AS otherMode,"
            + " mai.income_period AS incomePeriod,"
            + " mai.restriction_period AS restrictionPeriod,"
            + " strategy.issuance_deposit,"
            + " mai.white_paper_url AS whitePaperUrl,"
            + " IFNULL((SELECT value FROM merchant_assert_statistics x WHERE x.merchant_id = m.id AND x.currency_symbol = mai.currency_symbol AND x.key = 'MINING_REST'), mai.total * (mai.mining_rate / 100.0)) AS miningRest,"
            + " IFNULL((SELECT value FROM merchant_assert_statistics x WHERE x.merchant_id = m.id AND x.currency_symbol = mai.currency_symbol AND x.key = 'MINING_MIND'), 0) AS miningMind"
            + " FROM merchant m"
            + " INNER JOIN merchant_assert_issuance mai ON mai.merchant_id = m.id"
//            + " INNER JOIN merchant_assert_issuance_audit maia ON maia.issuance_id = mai.id AND maia.status = 5"
            + " INNER JOIN merchant_assert_issuance_audit maia ON maia.issuance_id = mai.id"
            + " LEFT JOIN user u ON m.mobile_number = u.mobile_number"
            + " LEFT JOIN user_property up ON up.user_id = u.id"
            + " LEFT JOIN platform_service_charge_strategy strategy ON mai.service_charge_id = strategy.id"
            + " WHERE m.id = #{merchantId}";

    @Select(QUERY_MERCHANTS)
    List<Merchant> query(Merchant merchant);

    @Select(QUERY_MERCHANTS_FOR_LIST)
    List<MerchantAssertIssuance> queryForList(MerchantAssertIssuance merchant);

    @Select(STATISTIC_MERCHANTS_FOR_LIST)
    List<CurrencyKeyValuePair> statisticForList(MerchantAssertIssuance merchant);

    @Select(QUERY_MERCHANT_DETAIL)
    MerchantAssertIssuance queryDetail(MerchantAssertIssuance param);

}
