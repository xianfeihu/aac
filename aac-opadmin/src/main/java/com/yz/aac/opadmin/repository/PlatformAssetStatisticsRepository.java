package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.CurrencyKeyValuePair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlatformAssetStatisticsRepository {

    String ALL_FIELDS = "id, `key`, value";
    String QUERY = "SELECT " + ALL_FIELDS + " FROM platform_assert_statistics";
    String UPDATE = "UPDATE platform_assert_statistics SET `key` = #{key}, value = #{value} WHERE id = #{id}";

    @Select(QUERY)
    List<CurrencyKeyValuePair> query();

    @Update(UPDATE)
    void update(CurrencyKeyValuePair item);

}
