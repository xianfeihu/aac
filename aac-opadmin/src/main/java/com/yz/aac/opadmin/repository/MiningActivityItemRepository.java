package com.yz.aac.opadmin.repository;

import com.yz.aac.opadmin.repository.domain.MiningActivityItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MiningActivityItemRepository {

    String ALL_FIELDS = "id, activity_id, user_level_id, total_bonus, lucky_rate, lucky_times, hit_ad_number, order_number";
    String STORE = "INSERT INTO mining_activity_item(" + ALL_FIELDS + ") VALUES(#{id}, #{activityId}, #{userLevelId}, #{totalBonus}, #{luckyRate}, #{luckyTimes}, #{hitAdNumber}, #{orderNumber})";
    String DELETE = "<script>DELETE FROM mining_activity_item"
            + "<where>"
            + "<if test=\"activityIds != null\"> AND activity_id IN <foreach collection='activityIds' item='activityId' open='(' close=')' separator=','> #{activityId} </foreach></if>"
            + "</where>"
            + "</script>";
    String QUERY = "<script>SELECT " + ALL_FIELDS + " FROM mining_activity_item"
            + "<where>"
            + "<if test=\"activityIds != null\"> AND activity_id IN <foreach collection='activityIds' item='activityId' open='(' close=')' separator=','> #{activityId} </foreach></if>"
            + "</where>"
            + " ORDER BY order_number"
            + "</script>";

    @Delete(DELETE)
    void delete(MiningActivityItem item);

    @Insert(STORE)
    void store(MiningActivityItem item);

    @Select(QUERY)
    List<MiningActivityItem> query(MiningActivityItem item);
}
