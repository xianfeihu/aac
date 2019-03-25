package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MerchantAssertIssuance;
import com.yz.aac.opadmin.repository.domain.MerchantAssertStatistics;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MerchantAssertStatisticsRepository {

    String ALL_FIELDS = "id, merchant_id, currency_symbol, `key`, value";
    String STORE_STATISTICS = "INSERT INTO merchant_assert_statistics(" + ALL_FIELDS + ") VALUES(#{id}, #{merchantId}, #{currencySymbol}, #{key}, #{value})";
    String SELECT_RESTRICTED = "SELECT mas.id, mai.issuing_date, mai.restriction_period"
            + " FROM merchant m"
            + " INNER JOIN merchant_assert_issuance mai ON m.id = mai.merchant_id"
            + " INNER JOIN merchant_assert_issuance_audit maia ON mai.id = maia.issuance_id AND maia.status IN (2, 3, 4, 5)"
            + " INNER JOIN merchant_assert_statistics mas ON m.id = mas.merchant_id AND mai.currency_symbol = mas.currency_symbol AND mas.key = 'UNRESTRICTED'"
            + " WHERE mas.value = 2";
    String UPDATE_STATISTICS = "UPDATE merchant_assert_statistics SET value = #{value} WHERE id = #{id}";

    @Update(UPDATE_STATISTICS)
    void update(MerchantAssertStatistics item);

    @Insert(STORE_STATISTICS)
    void store(MerchantAssertStatistics item);

    @Select(SELECT_RESTRICTED)
    List<MerchantAssertIssuance> queryRestricted();
}
