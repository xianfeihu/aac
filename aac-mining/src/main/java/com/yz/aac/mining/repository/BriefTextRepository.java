package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.BriefText;

@Mapper
public interface BriefTextRepository {

    String QUERY_BRIEF_TEXT = "select * from brief_text where `key` = #{key}";
    
    @Select(QUERY_BRIEF_TEXT)
    BriefText getBriefText(@Param("key") String key);

}