package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.UserLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserLevelRepository {

    String QUERY_USER_LEVEL = "SELECT * FROM user_level ORDER BY match_condition asc";
    
    String QUERY_LEVEL_BY_MATCH_CONDITION = "SELECT * FROM user_level where match_condition <= #{matchCondition} ORDER BY match_condition desc LIMIT 1";
    
    String QUERY_NEXT_LEVEL_BY_MATCH_CONDITION = "SELECT * FROM user_level where match_condition > #{matchCondition} ORDER BY match_condition asc LIMIT 1";

    String QUERY_USER_LEVEL_NAME_BY_USER_ID = " SELECT id,`name`,icon_path,match_condition FROM user_level where match_condition <= (SELECT history_max_balance FROM user_assert ua WHERE ua.user_id=#{userId} AND ua.currency_symbol=#{currencySymbol}) ORDER BY match_condition desc LIMIT 1 ";

    @Select(QUERY_USER_LEVEL)
    List<UserLevel> getUserLevel();
    
    /**
     * 当前额度等级
     * @param matchCondition
     * @return
     */
    @Select(QUERY_LEVEL_BY_MATCH_CONDITION)
    UserLevel getLevelByMatchCondition(@Param("matchCondition") BigDecimal matchCondition);

    /**
     * 当前额度下一等级
     * @param matchCondition
     * @return
     */
    @Select(QUERY_NEXT_LEVEL_BY_MATCH_CONDITION)
    UserLevel getNextLevelByMatchCondition(@Param("matchCondition") BigDecimal matchCondition);

    @Select(QUERY_USER_LEVEL_NAME_BY_USER_ID)
    UserLevel getUserLevelNameByUserId(@Param("userId") Long userId, @Param("currencySymbol") String currencySymbol);
}
