package com.yz.aac.mining.repository;

import com.yz.aac.mining.repository.domian.MiningActivityParticipant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiningActivityParticipantRepository {

    String ADD = " INSERT INTO mining_activity_participant (activity_item_id,user_id,queuing_code) SELECT #{itemId},#{userId},COUNT(0)+1 FROM mining_activity_participant WHERE activity_item_id=#{itemId} ";

    String FIND_JOIN_EXCAVATION_GAME_NUMBER = " SELECT COUNT(0) FROM mining_activity_participant WHERE activity_item_id=#{itemId} ";

    String FIND_PARTICIPANT_BY_ITEM_ID_AND_USER_ID = " SELECT activity_item_id,user_id,queuing_code FROM mining_activity_participant WHERE user_id=#{userId} AND activity_item_id=#{itemId} LIMIT 1 ";

    @Insert(ADD)
    void add(@Param("itemId") Long itemId, @Param("userId") Long userId);

    @Select(FIND_PARTICIPANT_BY_ITEM_ID_AND_USER_ID)
    MiningActivityParticipant findParticipantByItemIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId);

    @Select(FIND_JOIN_EXCAVATION_GAME_NUMBER)
    Integer findJoinExcavationGameNumber(@Param("itemId") Long itemId);

}
