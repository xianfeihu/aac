package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.UserNaturalGrowthRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserNaturalGrowthRecordRepository {

    String SAVE_USER_NATURAL_GROWTH = "INSERT INTO user_natural_growth_record(user_id, growth_amount, create_time) "
    		+ "values(#{userId}, #{growthAmount}, #{createTime})";
    
    String QUERY_USER_NATURAL_GROWTH_BY_TIME = "select * from user_natural_growth_record where user_id = #{userId} "
    		+ "and from_unixtime(create_time/1000,'%Y-%m-%d') >= date_format(DATE_SUB(curdate(),INTERVAL 1 DAY) ,'%Y-%m-%d')";
    
    String DELETE_USER_NATURAL_GROWTH = "delete from user_natural_growth_record where from_unixtime(create_time/1000,'%Y-%m-%d') < date_format(DATE_SUB(curdate(),INTERVAL 1 DAY) ,'%Y-%m-%d')";
    
    String QUERY_USER_NATURAL_GROWTH_BY_ID = "SELECT * FROM user_natural_growth_record WHERE id = #{id}";
    
    String DELETE_USER_NATURAL_GROWTH_BY_ID = "DELETE FROM user_natural_growth_record WHERE id = #{id}";
    
    @Insert(SAVE_USER_NATURAL_GROWTH)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUserNaturalGrowthRecord(UserNaturalGrowthRecord userNaturalGrowthRecord);
    
    /**
     * 获取48小时数据（按天算）
     * @param userId
     * @return
     */
    @Select(QUERY_USER_NATURAL_GROWTH_BY_TIME)
    List<UserNaturalGrowthRecord> getUserNaturalGrowthByTime(@Param("userId") Long userId);
    
    /**
     * 清理过期数据
     * @return
     */
    @Delete(DELETE_USER_NATURAL_GROWTH)
    int deleteNaturalGrowth();
    
    @Select(QUERY_USER_NATURAL_GROWTH_BY_ID)
    UserNaturalGrowthRecord getGrowthRecordById(@Param("id") Long id);
    
    @Delete(DELETE_USER_NATURAL_GROWTH_BY_ID)
    int deleteNaturalGrowthById(@Param("id") Long id);
}
