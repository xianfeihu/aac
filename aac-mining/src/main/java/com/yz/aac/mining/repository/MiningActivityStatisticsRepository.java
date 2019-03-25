package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.MiningActivityStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiningActivityStatisticsRepository {

    String SAVE = " INSERT INTO mining_activity_statistics (activity_item_id,user_id,gained,ad_clicked) VALUES ( #{activityItemId}, #{userId}, #{gained}, #{adClicked} )  ";

    String FIND_STATISTICS_BY_USER_ID_AND_ACTIVITY_ID = " SELECT id,activity_item_id,user_id,gained,ad_clicked FROM mining_activity_statistics WHERE user_id=#{userId} AND activity_item_id=#{itemId} ";

    @Select(FIND_STATISTICS_BY_USER_ID_AND_ACTIVITY_ID)
    MiningActivityStatistics findStatisticsByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @Select(SAVE)
    void save(MiningActivityStatistics miningActivityStatistics);
}
