package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.ParamConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ParamConfigRepository {

    String ALL_FIELDS = "id, category, sub_category, `key`, value";
    String QUERY_CONFIGS = "<script>SELECT " + ALL_FIELDS + " FROM param_config"
            + "<where>"
            + "<if test=\"category != null\"> AND category = #{category}</if>"
            + "<if test=\"subCategory != null\"> AND sub_category = #{subCategory}</if>"
            + "<if test=\"key != null\"> AND `key` = #{key}</if>"
            + "</where>"
            + "</script>";

    String UPDATE_CONFIG = "UPDATE param_config SET value = #{value} WHERE category = #{category} AND sub_category = #{subCategory} AND `key` = #{key}";

    @Select(QUERY_CONFIGS)
    List<ParamConfig> query(ParamConfig config);

    @Update(UPDATE_CONFIG)
    void update(ParamConfig config);

}
