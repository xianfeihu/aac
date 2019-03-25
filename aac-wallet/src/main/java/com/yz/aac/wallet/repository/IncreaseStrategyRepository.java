package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IncreaseStrategyRepository {

    String QUERY_BY_ENABLED = "SELECT id FROM increase_strategy WHERE is_default = #{isDefault}";

    @Select(QUERY_BY_ENABLED)
    Long getByEnabled(@Param("isDefault") Integer isDefault);
 
}
