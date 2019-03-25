package com.yz.aac.mining.repository;

import com.yz.aac.mining.model.response.PacketHistoryResponse;
import com.yz.aac.mining.model.response.PacketMapDateResponse;
import com.yz.aac.mining.repository.domian.RedPacketIssuance;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface RedPacketIssuanceRepository {

	String SAVE_RED_PACKET_ISSUANCE = "INSERT INTO red_packet_issuance(issuer_id, description, dividing_number, dividing_amount, lng, lat, "
			+ "location, radius, grabbing_limit, link_title, link_url, issuance_time)"
    		+ "VALUES(#{issuerId}, #{description}, #{dividingNumber}, #{dividingAmount}, #{lng}, #{lat}, "
    		+ "#{location}, #{radius}, #{grabbingLimit}, #{linkTitle}, #{linkUrl}, #{issuanceTime})";
	
	String QUERY_RED_PACKET_ISSUANCE_BY_ID = "SELECT * FROM red_packet_issuance WHERE id = #{id}";
	
	String QUERY_ISSUANCE_HISTORY = "select rpi.id,rpi.description,temp.receiveNum,temp.commentNum,temp.likeNum,temp.imageUrl from red_packet_issuance rpi "
			+ "left join ("
			+ "select id,sum(receiveNum) as receiveNum,sum(commentNum) as commentNum,sum(likeNum) as likeNum,max(image_url) as imageUrl from ("
			+ "select rpi.id, count(rpdg.id) as receiveNum,0 as commentNum,0 as likeNum,'' as image_url from red_packet_issuance rpi "
			+ "left join red_packet_dividing_grabbing rpdg on rpdg.red_packet_id = rpi.id "
			+ "where issuer_id = #{userId} group by rpi.id "
			+ "UNION "
			+ "select id, 0 as receiveNum, sum(commentNum) as commentNum, sum(likeNum) as likeNum, '' as image_url from ( "
			+ "select rpi.id,"
			+ "case when rpit.action = #{commentType} then 1 else 0 end commentNum,"
			+ "case when rpit.action = #{likeType} then 1 else 0 end likeNum "
			+ "from red_packet_issuance rpi "
			+ "left join red_packet_interaction rpit on rpit.red_packet_id = rpi.id "
			+ "where issuer_id = #{userId} and rpit.parent_id is null "
			+ ") as tableA group by tableA.id "
			+ "UNION "
			+ "select rpi.id, 0 as receiveNum, 0 as commentNum, 0 as likeNum, max(rpimg.image_url) as image_url from red_packet_issuance rpi "
			+ "left join red_packet_image rpimg on rpimg.red_packet_id = rpi.id and rpimg.order_number = 1 "
			+ "where issuer_id = #{userId} "
			+ "group by rpi.id ) as tableB GROUP BY tableB.id "
			+ ") temp on temp.id = rpi.id "
			+ "where rpi.issuer_id = #{userId} order by issuance_time desc ";
	
	String QUERY_TOTAL_AMOUNT = "select IFNULL(sum(dividing_amount),0) as totalAmount from red_packet_issuance where issuer_id = #{userId}";
	
	String QUERY_TOTAL_RECEIVE_NUM = "select count(*) as receiveNum from red_packet_issuance rpi "
			+ "left join red_packet_dividing_grabbing rpdg on rpdg.red_packet_id = rpi.id "
			+ "where rpi.issuer_id = #{userId} ";
	
	String QUERY_PACKET_MAPLIST = "<script> "
    		+ "SELECT rpi.id as packetId,u.id as issuanceId,u.`name` as issuanceName,rpi.lng,rpi.lat "
    		+ "FROM red_packet_issuance rpi "
    		+ "left join `user` u on u.id = rpi.issuer_id "
    		+ "WHERE (SQRT(POW(ABS((#{lng} - lng)),2) + POW(ABS((#{lat} - lat)),2)) * #{reducedUnit}) &lt; rpi.radius "
    		+ "<if test=\"grabbingLimit != null\"> "
    		+ " AND (grabbing_limit is null or grabbing_limit = #{grabbingLimit}) "
    		+ "</if> "
    		+ "</script>";
	
	@Insert(SAVE_RED_PACKET_ISSUANCE)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveRedPacketIssuance(RedPacketIssuance RedPacketIssuance);
	
	@Select(QUERY_RED_PACKET_ISSUANCE_BY_ID)
	RedPacketIssuance getPacketIssuanceById(@Param("id") Long id);
	
	/**
	 * 用户发布红包总金额
	 * @param userId
	 * @return
	 */
	@Select(QUERY_TOTAL_AMOUNT)
	BigDecimal getTotalAmount(@Param("userId") Long userId);
	
	/**
	 * 累计领取红包人数
	 * @param userId
	 * @return
	 */
	@Select(QUERY_TOTAL_RECEIVE_NUM)
	Integer getTotalReceiveNum(@Param("userId") Long userId);
	
	/**
	 * 红包发布历史数据
	 * @param userId
	 * @param commentType
	 * @param likeType
	 * @return
	 */
	@Select(QUERY_ISSUANCE_HISTORY)
	List<PacketHistoryResponse> getHistoryList(@Param("userId") Long userId, @Param("commentType") Integer commentType, @Param("likeType") Integer likeType);
	
	/**
	 * 根据经纬度获取一定范围红包
	 * @param lng
	 * @param lat
	 * @param reducedUnit
	 * @return
	 */
	@Select(QUERY_PACKET_MAPLIST)
	List<PacketMapDateResponse> getPacketMapDte(@Param("lng") BigDecimal lng, @Param("lat") BigDecimal lat, 
			@Param("reducedUnit") Integer reducedUnit, @Param("grabbingLimit") Integer grabbingLimit);

}
