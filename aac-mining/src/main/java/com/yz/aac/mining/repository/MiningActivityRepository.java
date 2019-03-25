package com.yz.aac.mining.repository;

import com.yz.aac.mining.model.response.ExcavationGameIndexInfoResponse;
import com.yz.aac.mining.repository.domian.MiningActivity;
import com.yz.aac.mining.repository.domian.MiningActivityItem;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MiningActivityRepository {

    String FIND_MINING_ACTIVITY_BY_ID = " SELECT id,begin_time,end_time,status FROM mining_activity WHERE status=#{status} AND id=#{activityId } ";

    String FIND_EXCAVATION_GAME_INDEX_INFO = " SELECT ma.id,ma.begin_time,ma.end_time,GROUP_CONCAT(ul.`name` ORDER BY mai.user_level_id) AS level_limit FROM mining_activity ma LEFT JOIN mining_activity_item mai ON ma.id=mai.activity_id LEFT JOIN user_level ul ON ul.id=mai.user_level_id WHERE ma.begin_time>=#{nowTime} AND ma.`status`=#{status} GROUP BY ma.id ORDER BY ma.begin_time LIMIT 3 ";

    String FIND_ITEM_BY_LEVEL_AND_ACTIVITY_ID = " SELECT mai.id,mai.activity_id,mai.user_level_id,mai.total_bonus,mai.lucky_rate,mai.lucky_times,mai.hit_ad_number,mai.order_number FROM mining_activity ma LEFT JOIN mining_activity_item mai ON ma.id=mai.activity_id WHERE ma.id=#{activityId} AND (mai.user_level_id=#{userLevelId} OR mai.user_level_id IS NULL ) ";

    String FIND_VALID_MINING_ACTIVITY_ITEM_BY_ITEM_ID = " SELECT mai.id,mai.activity_id,mai.user_level_id,mai.total_bonus,mai.lucky_rate,mai.lucky_times,mai.hit_ad_number,mai.order_number FROM mining_activity ma JOIN mining_activity_item mai ON ma.id=mai.activity_id WHERE mai.id=#{itemId} AND ma.status=#{status} AND ma.end_time>=#{nowTime} ";

    String FIND_ITEM_BY_ITEM_ID = " SELECT mai.id,mai.activity_id,mai.user_level_id,mai.total_bonus,mai.lucky_rate,mai.lucky_times,mai.hit_ad_number,mai.order_number FROM mining_activity_item mai WHERE mai.id=#{itemId} ";

    String FIND_ACTIVITY_BY_ITEM_ID = " SELECT ma.id,ma.begin_time,ma.end_time,ma.status FROM mining_activity ma JOIN mining_activity_item mai ON ma.id=mai.activity_id WHERE mai.id=#{itemId} ";

    @Select(FIND_EXCAVATION_GAME_INDEX_INFO)
    List<ExcavationGameIndexInfoResponse> getExcavationGameIndexInfo(@Param("nowTime") Long nowTime, @Param("status") Integer status);

    @Select(FIND_MINING_ACTIVITY_BY_ID)
    MiningActivity findMiningActivityById(@Param("activityId") Long activityId, @Param("status") Integer status);

    @Select(FIND_ITEM_BY_LEVEL_AND_ACTIVITY_ID)
    MiningActivityItem findItemByLevelAndActivityId(@Param("userLevelId") Long userLevelId, @Param("activityId") Long activityId);

    @Select(FIND_VALID_MINING_ACTIVITY_ITEM_BY_ITEM_ID)
    MiningActivityItem findValidMiningActivityItemByItemId(@Param("itemId") Long itemId, @Param("nowTime") Long nowTime,@Param("status") Integer status);

    @Select(FIND_ITEM_BY_ITEM_ID)
    MiningActivityItem findItemByItemId(@Param("itemId") Long itemId);

    @Select(FIND_ACTIVITY_BY_ITEM_ID)
    MiningActivity findActivityByItemId(@Param("itemId") Long itemId);
}
