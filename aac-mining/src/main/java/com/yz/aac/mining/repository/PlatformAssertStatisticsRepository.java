package com.yz.aac.mining.repository;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PlatformAssertStatisticsRepository {

    		
    String QUERY_PLATFORM_FORM_ASSERT_STATISTICS = "select `value` from platform_assert_statistics where `key` = #{key}";
    
    @Select(QUERY_PLATFORM_FORM_ASSERT_STATISTICS)
    BigDecimal getPlatformAssertStatistics(@Param("key") String key);

}
