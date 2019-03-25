package com.yz.aac.mining.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.repository.domian.IncreaseStrategy;

@Mapper
public interface IncreaseStrategyRepository {

    String QUERY_BY_USERID = "SELECT * FROM increase_strategy ins "
    		+ "left join user_property up on up.increase_strategy_id = ins.id "
    		+ "where up.user_id = #{userId}";

    @Select(QUERY_BY_USERID)
    IncreaseStrategy getByUserId(@Param("userId") Long userId);
 
}
