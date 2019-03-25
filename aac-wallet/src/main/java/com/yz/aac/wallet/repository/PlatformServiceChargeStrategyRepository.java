package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.PlatformServiceChargeStrategy;

@Mapper
public interface PlatformServiceChargeStrategyRepository {

    String QUERY_BY_ID = "SELECT * FROM platform_service_charge_strategy WHERE id = #{id}";
    
    @Select(QUERY_BY_ID)
    PlatformServiceChargeStrategy getById(@Param("id") Long id);
}
