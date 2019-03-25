package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.ParamConfig;

@Mapper
public interface ParamConfigRepository {

    String QUERY_PARAM_CONFIG = "select * from param_config WHERE category = #{category} AND sub_category = #{subCategory} AND `key` = #{key}";
    
    @Select(QUERY_PARAM_CONFIG)
    ParamConfig getParamConfig(@Param("category") Integer category, @Param("subCategory") Integer subCategory, @Param("key") String key);
}
