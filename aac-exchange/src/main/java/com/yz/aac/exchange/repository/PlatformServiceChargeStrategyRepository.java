package com.yz.aac.exchange.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.exchange.repository.domian.PlatformServiceChargeStrategy;

@Mapper
public interface PlatformServiceChargeStrategyRepository {

    String QUERY_BY_MOBILE_NUMBER = "select pscs.* from platform_service_charge_strategy pscs"
    		+ " left join merchant_assert_issuance msi on msi.service_charge_id = pscs.id"
    		+ " left join merchant m on m.id = msi.merchant_id"
    		+ " where m.mobile_number = #{mobileNumber}";
    
    @Select(QUERY_BY_MOBILE_NUMBER)
    PlatformServiceChargeStrategy getByMerchantMobile(@Param("mobileNumber") Long mobileNumber);
}
