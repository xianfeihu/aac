package com.yz.aac.mining.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yz.aac.mining.model.response.PacketCommentResponse;
import com.yz.aac.mining.repository.domian.RedPacketInteraction;

@Mapper
public interface RedPacketInteractionRepository {

	String QUERY_RED_PACKET_INTERACTION = "SELECT * FROM red_packet_interaction WHERE red_packet_id = #{redPacketId} AND action = #{action} AND parent_id IS NULL";
	
	String QUERY_COMMENT_BY_USERID = "<script>"
			+ "SELECT count(*) FROM red_packet_interaction WHERE red_packet_id = #{redPacketId} AND action = #{action} AND parent_id IS NULL "
			+ "<if test=\"userIds != null and userIds.size() > 0\"> "
			+ "AND action_user_id NOT IN "
			+ "<foreach collection='userIds' item='uid' open='(' close=')' separator=','>"
    		+ "#{uid}"
    		+ "</foreach>"
    		+ "</if>"
    		+ " GROUP BY action_user_id"
    		+ "</script>";
	
	String QUERY_RED_PACKET_INTERACTION_BY_USERID = "<script>"
			+ "SELECT * FROM red_packet_interaction WHERE red_packet_id = #{redPacketId} AND action = #{action} "
			+ "<choose>"
			+ "<when test = \"parentId != null\"> AND parent_id = #{parentId} </when>"
			+ "<otherwise>  AND parent_id IS NULL </otherwise>"
			+ "</choose>"
			+ "AND action_user_id = #{userId} "
			+ "</script>";
	
	String QUERY_LEAVING_MESSAGE = "select rpi.id as commentId,u.`name`,rpi.action_description as description,rpi.action_time as actionTime, tableB.likes,tableB.isLikes from ("
			+ "select tableA.id, count(tableA.rpiaId) as likes,min(tableA.isLikes) as isLikes from ("
			+ "SELECT  rpi.id, rpia.id as rpiaId,"
			+ "case when rpia.action_user_id = #{userId} then 1 else 2 end as isLikes "
			+ "FROM red_packet_interaction rpi "
			+ "left join red_packet_interaction rpia on (rpia.parent_id = rpi.id and rpia.action = #{likes}) "
			+ "where rpi.action = #{action} and rpi.red_packet_id = #{redPacketId} and rpi.parent_id is null "
			+ ") as tableA "
			+ "GROUP BY tableA.id "
			+ ") as tableB "
			+ "left join red_packet_interaction rpi on rpi.id = tableB.id "
			+ "left join `user` u on u.id = rpi.action_user_id ";
	
	String QUERY_REPLY_LIST = "select u.`name`,rpi.action_description as description,rpi.action_time as actionTime from red_packet_interaction rpi "
			+ "left join `user` u on u.id = rpi.action_user_id "
			+ "where parent_id = #{parentId} and action = #{action} ";
	
	String SAVE_RED_PACKET_INTERACTION = "INSERT INTO red_packet_interaction (red_packet_id, parent_id, action, action_description, action_user_id, action_time)"
			+ "VALUES(#{redPacketId}, #{parentId}, #{action}, #{actionDescription}, #{actionUserId}, #{actionTime})";
	/**
	 * 获取最顶层互动
	 * @param redPacketId
	 * @return
	 */
	@Select(QUERY_RED_PACKET_INTERACTION)
	List<RedPacketInteraction> getPacketInteraction(@Param("redPacketId") Long redPacketId, @Param("action") Integer action);
	
	/**
	 * 获取红包评论人数（可以排除某些用户）
	 * @param redPacketId
	 * @param action
	 * @param userIds
	 * @return
	 */
	@Select(QUERY_COMMENT_BY_USERID)
	List<Integer> getCommentByUserId(@Param("redPacketId") Long redPacketId, @Param("action") Integer action, @Param("userIds") List<Long> userIds);
	
	/**
	 * 根据用户ID获取最顶层互动行为
	 * @param redPacketId
	 * @param action
	 * @param userId
	 * @return
	 */
	@Select(QUERY_RED_PACKET_INTERACTION_BY_USERID)
	RedPacketInteraction getPacketInteractionByUserId(@Param("redPacketId") Long redPacketId, @Param("action") Integer action,
			@Param("userId") Long userId, @Param("parentId") Long parentId);
	
	/**
	 * 获取红包顶级留言（包含总点赞数，和当前用户是否点赞标记）
	 * @param redPacketId
	 * @param action
	 * @param userId
	 * @return
	 */
	@Select(QUERY_LEAVING_MESSAGE)
	List<PacketCommentResponse> getLeavingMessage(@Param("redPacketId") Long redPacketId, @Param("action") Integer action,
			@Param("userId") Long userId, @Param("likes") Integer likes);
	
	/**
	 * 根据评论ID获取回复列表
	 * @param parentId
	 * @param action
	 * @return
	 */
	@Select(QUERY_REPLY_LIST)
	List<PacketCommentResponse> getReplyList(@Param("parentId") Long parentId, @Param("action") Integer action);
	
	@Insert(SAVE_RED_PACKET_INTERACTION)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveRedPacketIssuance(RedPacketInteraction redPacketInteraction);
	
}
