package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.PlatformAssertSeller;

@Mapper
public interface PlatformAssertSellerRepository {

    String PLATFORM_ASSERT_SELLER_RANDOM = "SELECT * FROM platform_assert_seller LIMIT 1";
    
    String PLATFORM_ASSERT_SELLER_BY_ID = "SELECT * FROM platform_assert_seller WHERE id = #{id}";
    /**
     * 随机获取一个挂单人信息
     */
    @Select(PLATFORM_ASSERT_SELLER_RANDOM)
    PlatformAssertSeller getPlatformAssertSellerRandom();
    
    @Select(PLATFORM_ASSERT_SELLER_BY_ID)
    PlatformAssertSeller getPlatformAssertSellerById(@Param("id") Long id);
}
