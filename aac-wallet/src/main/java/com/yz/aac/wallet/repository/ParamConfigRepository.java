package com.yz.aac.wallet.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.wallet.repository.domain.ParamConfig;

@Mapper
public interface ParamConfigRepository {

    String QUERY_PARAM_CONFIG = "select * from param_config WHERE category = #{category} AND sub_category = #{subCategory} AND `key` = #{key}";
    
    @Select(QUERY_PARAM_CONFIG)
    ParamConfig getParamConfig(@Param("category") Integer category, @Param("subCategory") Integer subCategory, @Param("key") String key);
}
