package com.yz.aac.mining.repository;

import com.yz.aac.mining.model.response.PacketHistoryResponse;
import com.yz.aac.mining.repository.domian.RedPacketDividingGrabbing;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface RedPacketDividingGrabbingRepository {

	String SAVE_PACKET_GRABBING = "INSERT INTO red_packet_dividing_grabbing(red_packet_id, dividing_id, grabber_id, grabbing_time) "
			+ "VALUES(#{redPacketId}, #{dividingId}, #{grabberId}, #{grabbingTime})";
	
	String QUERY_PACKET_GRABBING = "SELECT * FROM red_packet_dividing_grabbing WHERE red_packet_id = #{redPacketId} AND grabber_id = #{grabberId}";
	
	String QUERY_GRABBERS = "select u.`name` from red_packet_dividing_grabbing rpdg "
			+ "left join `user` u on u.id = rpdg.grabber_id where red_packet_id = #{redPacketId}";
    
	String QUERY_RECEIVE_TOTAL_AMOUNT = "select IFNULL(sum(rpd.dividing_amount),0) as amount from red_packet_dividing_grabbing rpdg "
			+ "left join red_packet_dividing rpd on rpd.id = rpdg.dividing_id "
			+ "where rpdg.grabber_id = #{userId}";
	
	String QUERY_BEST_LUCK = "select IFNULL(max(rpd.dividing_amount),0) as amount from red_packet_dividing_grabbing rpdg "
			+ "left join red_packet_dividing rpd on rpd.id = rpdg.dividing_id "
			+ "where rpdg.grabber_id = #{userId}";
	
	@Select(QUERY_PACKET_GRABBING)
	RedPacketDividingGrabbing getPacketGrabbing(@Param("redPacketId") Long redPacketId, @Param("grabberId") Long grabberId);
	
	String QUERY_RECEIVE_RECOED = "select rpi.id,rpi.description,temp.receiveNum,temp.commentNum,temp.likeNum,temp.imageUrl,u.`name` as issuerName from ( "
			+ "select id,sum(receiveNum) as receiveNum,sum(commentNum) as commentNum,sum(likeNum) as likeNum,max(image_url) as imageUrl from ( "
			+ "select rpi.id, count(rp.id) as receiveNum,0 as commentNum,0 as likeNum,'' as image_url from red_packet_dividing_grabbing rpdg  "
			+ "left join red_packet_issuance rpi on rpi.id = rpdg.red_packet_id "
			+ "left join red_packet_dividing_grabbing rp on rp.red_packet_id =  rpi.id "
			+ "where rpdg.grabber_id = #{userId} group by rpi.id "
			+ "UNION "
			+ "select id, 0 as receiveNum, sum(commentNum) as commentNum, sum(likeNum) as likeNum, '' as image_url from ( "
			+ "select rpi.id, "
			+ "case when rpit.action = #{commentType} then 1 else 0 end commentNum, "
			+ "case when rpit.action = #{likeType} then 1 else 0 end likeNum "
			+ "from red_packet_dividing_grabbing rpdg "
			+ "left join red_packet_issuance rpi on rpi.id = rpdg.red_packet_id "
			+ "left join red_packet_interaction rpit on rpit.red_packet_id = rpi.id "
			+ "where rpdg.grabber_id = #{userId} ) as tableA group by tableA.id "
			+ "UNION "
			+ "select rpi.id, 0 as receiveNum, 0 as commentNum, 0 as likeNum, max(rpimg.image_url) as image_url "
			+ "from red_packet_dividing_grabbing rpdg "
			+ "left join red_packet_issuance rpi on rpi.id = rpdg.red_packet_id "
			+ "left join red_packet_image rpimg on rpimg.red_packet_id = rpi.id and rpimg.order_number = 1 "
			+ "where rpdg.grabber_id = #{userId} group by rpi.id "
			+ ") as tableB "
			+ "GROUP BY tableB.id "
			+ ") temp "
			+ "left join red_packet_issuance rpi on rpi.id = temp.id "
			+ "left join `user` u on u.id = rpi.issuer_id "
			+ "order by issuance_time desc ";
	
	String QUERY_TODAY_RECEIVE_COUNT = "select IFNULL(count(*),0) from red_packet_dividing_grabbing where grabber_id = #{userId} "
			+ "and from_unixtime(grabbing_time/1000,'%Y-%m-%d') = date_format(now() ,'%Y-%m-%d')";
	
	String QUERY_LATEST_COLLECTION_TIME = "select grabbing_time from red_packet_dividing_grabbing where red_packet_id = #{packetId} ORDER BY grabbing_time desc limit 1";
	
	/**
	 * 获取红包领取人姓名
	 * @param redPacketId
	 * @return
	 */
	@Select(QUERY_GRABBERS)
	List<String> getGrabbers(@Param("redPacketId") Long redPacketId);
	
	/**
	 * 根据用户ID获取领取总红包金额
	 * @param userId
	 * @return
	 */
	@Select(QUERY_RECEIVE_TOTAL_AMOUNT)
	BigDecimal getReceiveTotalAmount(@Param("userId") Long userId);
	
	/**
	 * 历史最佳手气
	 * @param userId
	 * @return
	 */
	@Select(QUERY_BEST_LUCK)
	BigDecimal getBestLuck(@Param("userId") Long userId);
	
	/**
	 * 领取红包记录（包含图片，领取人，点赞，评论统计数）
	 * @param userId
	 * @param commentType
	 * @param likeType
	 * @return
	 */
	@Select(QUERY_RECEIVE_RECOED)
	List<PacketHistoryResponse> getReceiveRecordList(@Param("userId") Long userId, @Param("commentType") Integer commentType, @Param("likeType") Integer likeType);
	
	@Insert(SAVE_PACKET_GRABBING)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveRedPacketGrabbing(RedPacketDividingGrabbing redPacketDividingGrabbing);
	
	/**
	 * 当日用户领取红包个数
	 * @param userId
	 * @return
	 */
	@Select(QUERY_TODAY_RECEIVE_COUNT)
	Integer getTodayReceiveCount(@Param("userId") Long userId);
	
	/**
	 * 红包最新领取时间
	 * @param packetId
	 * @return
	 */
	@Select(QUERY_LATEST_COLLECTION_TIME)
	Long getLatestCollectionTime(@Param("packetId") Long packetId);
}
