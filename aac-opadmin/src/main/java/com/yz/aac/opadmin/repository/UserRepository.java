package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.RichUser;
import com.yz.aac.opadmin.repository.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRepository {

    String QUERY_RICH_USERS = "<script>SELECT"
            + " u.id AS userId,"
            + " up.status AS status,"
            + " u.name AS userName,"
            + " ua.wallet_address AS walletAddress,"
            + " ua.balance AS balance,"
            + " (SELECT IFNULL(SUM(amount), 0) FROM user_assert_freeze uaf WHERE uaf.user_id = u.id) AS freezingAmount,"
            + " (SELECT ul.name FROM user_level ul WHERE ul.match_condition &lt;= history_max_balance ORDER BY ul.match_condition DESC LIMIT 1) AS levelName,"
            + " up.power_point AS powerPoint,"
            + " IFNULL(ubs.value, 0) AS tradeCount,"
            + " up.increase_strategy_id AS increaseAlgorithmId,"
            + " IFNULL((SELECT uis.value FROM user_income_statistics uis WHERE uis.user_id = u.id AND uis.currency_symbol = #{currencySymbol} AND `key` = #{incomeRechargeKey}), 0) AS rechargeIncome,"
            + " IFNULL((SELECT uis.value FROM user_income_statistics uis WHERE uis.user_id = u.id AND uis.currency_symbol = #{currencySymbol} AND `key` = #{incomeSellKey}), 0) AS sellIncome,"
            + " IFNULL((SELECT uis.value FROM user_income_statistics uis WHERE uis.user_id = u.id AND uis.currency_symbol = #{currencySymbol} AND `key` = #{incomeTransferKey}), 0) AS transferIncome,"
            + " IFNULL((SELECT uis.value FROM user_income_statistics uis WHERE uis.user_id = u.id AND uis.currency_symbol = #{currencySymbol} AND `key` = #{incomeMiningKey}), 0) AS miningIncome,"
            + " u.mobile_number AS mobileNumber,"
            + " u.id_number AS idNumber,"
            + " u.gender AS gender,"
            + " up.status_description AS statusDescription"
            + " FROM user u"
            + " INNER JOIN user_role ur ON u.id = ur.user_id"
            + " INNER JOIN user_property up ON u.id = up.user_id"
            + " INNER JOIN user_assert ua ON u.id = ua.user_id AND ua.currency_symbol = #{currencySymbol}"
            + " LEFT JOIN user_behaviour_statistics ubs ON ubs.user_id = u.id AND ubs.key = #{tradeCountKey}"
            + "<where>"
            + "<if test=\"userId != null\"> AND u.id = #{userId}</if>"
            + "<if test=\"mobileNumber != null\"> AND u.mobile_number = #{mobileNumber}</if>"
            + "<if test=\"currentLevelIncome != null\"> AND ua.history_max_balance &gt;= #{currentLevelIncome}</if>"
            + "<if test=\"nextLevelIncome != null\"> AND ua.history_max_balance &lt; #{nextLevelIncome}</if>"
            + "<if test=\"beginRegTime != null\"> AND u.reg_time &gt;= #{beginRegTime}</if>"
            + "<if test=\"endRegTime != null\"> AND u.reg_time &lt;= #{endRegTime}</if>"
            + "<if test=\"minBalance != null\"> AND ua.balance &gt;= #{minBalance}</if>"
            + "<if test=\"maxBalance != null\"> AND ua.balance &lt;= #{maxBalance}</if>"
            + "<if test=\"isMerchant != null\"> AND ur.is_merchant = #{isMerchant}</if>"
            + "<if test=\"isAdvertiser != null\"> AND ur.is_advertiser = #{isAdvertiser}</if>"
            + "<if test=\"userName != null and userName != ''\"><bind name=\"fixedName\" value=\"'%' + userName + '%'\" /> AND u.name LIKE #{fixedName}</if>"
            + "</where>"
            + " ORDER BY (balance - freezingAmount) DESC"
            + "</script>";

    String QUERY_USERS = "<script>SELECT id, name, gender, id_number, mobile_number, payment_password, source, reg_time, inviter_id, inviter_code FROM user"
            + "<where>"
            + "<if test=\"mobileNumber != null\"> AND mobile_number = #{mobileNumber}</if>"
            + "</where>"
            + "</script>";

    @Select(QUERY_RICH_USERS)
    List<RichUser> queryRichUsers(RichUser condition);

    @Select(QUERY_USERS)
    List<User> query(User condition);

}
